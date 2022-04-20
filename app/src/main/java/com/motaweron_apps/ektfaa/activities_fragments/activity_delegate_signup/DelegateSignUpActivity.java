package com.motaweron_apps.ektfaa.activities_fragments.activity_delegate_signup;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_map.MapActivity;
import com.motaweron_apps.ektfaa.adapter.SpinnerAreaAdapter;
import com.motaweron_apps.ektfaa.adapter.SpinnerCartTypeAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityDelegateSignupBinding;
import com.motaweron_apps.ektfaa.model.AddDelegateModel;
import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.CarType;
import com.motaweron_apps.ektfaa.model.CarTypeDataModel;
import com.motaweron_apps.ektfaa.model.SelectedLocation;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelegateSignUpActivity extends BaseActivity {
    private ActivityDelegateSignupBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private AddDelegateModel addDelegateModel;
    private int req;
    private String phone_code;
    private String phone;
    private SpinnerCartTypeAdapter adapter;
    private List<CarType> list;
    private List<SingleAreaModel> areaList;
    private SpinnerAreaAdapter spinnerAreaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delegate_signup);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.delegate_signup), R.color.white, R.color.black);
        list = new ArrayList<>();
        areaList = new ArrayList<>();

        adapter = new SpinnerCartTypeAdapter(list, this);
        binding.spinnerCarType.setAdapter(adapter);

        addDelegateModel = new AddDelegateModel();
        addDelegateModel.setPhone_code(phone_code);
        addDelegateModel.setPhone(phone);

        if (getUserModel() != null) {
            addDelegateModel.setName(getUserModel().getData().getName());
            addDelegateModel.setAddress(getUserModel().getData().getAddress());
            addDelegateModel.setLat(Double.parseDouble(getUserModel().getData().getLatitude()));
            addDelegateModel.setLng(Double.parseDouble(getUserModel().getData().getLongitude()));
            addDelegateModel.setCar_type(getUserModel().getData().getCar_type().getId() + "");

            if (getUserModel().getData().getLogo()!=null){
                addDelegateModel.setImage(getUserModel().getData().getLogo());
                Picasso.get().load(Uri.parse(getUserModel().getData().getLogo())).into(binding.image);

            }else {
                addDelegateModel.setImage("");

            }
            setUpToolbar(binding.toolbar,getString(R.string.edit_profile), R.color.white, R.color.black);

            binding.btnAdd.setText(getString(R.string.update));
        }

        binding.setModel(addDelegateModel);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {

                    Uri uri = getImageUri(result.getData());
                    binding.image.setImageURI(uri);
                    addDelegateModel.setImage(uri.toString());

                }
            } else if (req == 2) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    SelectedLocation selectedLocation = (SelectedLocation) result.getData().getSerializableExtra("location");
                    if (selectedLocation != null) {
                        addDelegateModel.setAddress(selectedLocation.getAddress());
                        addDelegateModel.setLat(selectedLocation.getLat());
                        addDelegateModel.setLng(selectedLocation.getLng());
                        binding.setModel(addDelegateModel);
                    }


                }
            }

        });


        spinnerAreaAdapter = new SpinnerAreaAdapter(areaList, this);
        binding.spinnerArea.setAdapter(spinnerAreaAdapter);
        binding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addDelegateModel.setArea_id(areaList.get(position).getId() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.flImage.setOnClickListener(v -> {
            req = 1;
            checkReadPermission();
        });


        binding.llMap.setOnClickListener(v -> {
            req = 2;
            Intent intent = new Intent(this, MapActivity.class);
            launcher.launch(intent);
        });

        binding.spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addDelegateModel.setCar_type(list.get(i).getId() + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.btnAdd.setOnClickListener(v -> {
            if (addDelegateModel.isDataValid(this)) {
                if (getUserModel() == null) {
                    driverSignUp();

                } else {
                    updateProfile();
                }
            }
        });

        getCarType();
        getCityData();

    }

    private void driverSignUp() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        MultipartBody.Part imagePart = null;
        if (!addDelegateModel.getImage().isEmpty()){
            imagePart = Common.getMultiPartImage(this,Uri.parse(addDelegateModel.getImage()), "logo");

        }
        RequestBody name_part = Common.getRequestBodyText(addDelegateModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(addDelegateModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(addDelegateModel.getPhone());
        RequestBody area_id_part = Common.getRequestBodyText(addDelegateModel.getArea_id());
        RequestBody car_type_part = Common.getRequestBodyText(addDelegateModel.getCar_type());
        RequestBody address_part = Common.getRequestBodyText(addDelegateModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(addDelegateModel.getLat()+"");
        RequestBody lng_part = Common.getRequestBodyText(addDelegateModel.getLng()+"");

        RequestBody software_part = Common.getRequestBodyText("android");


        Api.getService(Tags.base_url)
                .driverSignUp(name_part,car_type_part,lat_part,lng_part,address_part,area_id_part,phone_code_part,phone_part,software_part, imagePart)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());
                                setResult(RESULT_OK);
                                finish();

                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                //    Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void updateProfile() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        MultipartBody.Part imagePart = null;
        if (!addDelegateModel.getImage().isEmpty()&&!addDelegateModel.getImage().startsWith("http")){
            imagePart = Common.getMultiPartImage(this,Uri.parse(addDelegateModel.getImage()), "logo");

        }

        RequestBody user_id_part = Common.getRequestBodyText(getUserModel().getData().getId());
        RequestBody name_part = Common.getRequestBodyText(addDelegateModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(addDelegateModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(addDelegateModel.getPhone());
        RequestBody area_id_part = Common.getRequestBodyText(addDelegateModel.getArea_id());
        RequestBody car_type_part = Common.getRequestBodyText(addDelegateModel.getCar_type());
        RequestBody address_part = Common.getRequestBodyText(addDelegateModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(addDelegateModel.getLat()+"");
        RequestBody lng_part = Common.getRequestBodyText(addDelegateModel.getLng()+"");

        RequestBody software_part = Common.getRequestBodyText("android");


        Api.getService(Tags.base_url)
                .updateDriverProfile("Bearer "+getUserModel().getData().getToken(),user_id_part,name_part,car_type_part,area_id_part,lat_part,lng_part,address_part,phone_code_part,phone_part,software_part, imagePart)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());
                                setResult(RESULT_OK);
                                finish();

                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void getCityData() {

        Api.getService(Tags.base_url).getArea().enqueue(new Callback<AllAreaModel>() {
            @Override
            public void onResponse(Call<AllAreaModel> call, Response<AllAreaModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        areaList.clear();
                        areaList.addAll(response.body().getData());
                        runOnUiThread(() -> {
                            spinnerAreaAdapter.notifyDataSetChanged();
                        });

                        if (areaList.size() > 0) {
                            if (getUserModel() == null) {
                                addDelegateModel.setArea_id(areaList.get(0).getId() + "");

                            } else {
                                binding.spinnerArea.setSelection(getAreaPos());

                            }


                        }
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onFailure(Call<AllAreaModel> call, Throwable t) {
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                        } else {
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private void getCarType() {
        Api.getService(Tags.base_url).getCarType()
                .enqueue(new Callback<CarTypeDataModel>() {
                    @Override
                    public void onResponse(Call<CarTypeDataModel> call, Response<CarTypeDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                list.clear();
                                list.addAll(response.body().getData());
                                runOnUiThread(() -> {
                                    adapter.notifyDataSetChanged();
                                });
                                if (list.size() > 0) {
                                    if (getUserModel() == null) {
                                        addDelegateModel.setCar_type(list.get(0).getId() + "");

                                    } else {
                                        binding.spinnerCarType.setSelection(getCarTypePos());

                                    }


                                }


                            }
                        } else {


                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CarTypeDataModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private int getCarTypePos() {
        int pos = 0;
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getId() == getUserModel().getData().getCar_type().getId()) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private int getAreaPos() {
        int pos = 0;
        for (int index = 0; index < areaList.size(); index++) {
            if (areaList.get(index).getId() == getUserModel().getData().getArea().getId()) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this, BaseActivity.READ_REQ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, BaseActivity.WRITE_REQ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, BaseActivity.CAM_REQ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{BaseActivity.READ_REQ, BaseActivity.WRITE_REQ, BaseActivity.CAM_REQ}, 100);
        }
    }

    private void openGallery() {
        List<Intent> allIntents = new ArrayList<>();
     /*   Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cameraResolveInfo = getPackageManager().queryIntentActivities(cameraIntent, 0);

        for (ResolveInfo info : cameraResolveInfo) {
            Intent intent = new Intent(cameraIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            allIntents.add(intent);
        }
*/

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo info : resolveInfo) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);

            allIntents.add(intent);
        }


        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }

        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Choose image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        launcher.launch(chooserIntent);


    }

    private Uri getCameraUri() {
        Uri outPutUri = null;
        File file = new File(Environment.getExternalStorageDirectory(), "EktfaaApp");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (req == 1) {
            outPutUri = Uri.fromFile(new File(file, "profile.png"));

        }

        return outPutUri;
    }

    private Uri getImageUri(Intent intent) {
        boolean isCamera = true;
        if (intent != null) {
            String action = intent.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera ? getCameraUri() : intent.getData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length >= 3) {
            openGallery();
        } else {
            Toast.makeText(this, "Access image denied", Toast.LENGTH_SHORT).show();
        }
    }


}