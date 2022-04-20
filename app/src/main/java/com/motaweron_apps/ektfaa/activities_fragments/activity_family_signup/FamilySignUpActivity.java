package com.motaweron_apps.ektfaa.activities_fragments.activity_family_signup;

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
import com.motaweron_apps.ektfaa.adapter.SpinnerDepartmentAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityFamilySignupBinding;
import com.motaweron_apps.ektfaa.model.AddFamilyModel;
import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.DepartmentDataModel;
import com.motaweron_apps.ektfaa.model.DepartmentModel;
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

public class FamilySignUpActivity extends BaseActivity {
    private ActivityFamilySignupBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private AddFamilyModel addFamilyModel;
    private int req;
    private List<SingleAreaModel> areaList;
    private SpinnerAreaAdapter spinnerAreaAdapter;
    private List<DepartmentModel.BasicDepartmentFk> deptList, subDeptList;
    private SpinnerDepartmentAdapter spinnerDepartmentAdapter, spinnerSubDepartmentAdapter;
    private String phone_code;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family_signup);
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
        setUpToolbar(binding.toolbar, getString(R.string.family_signup), R.color.white, R.color.black);

        deptList = new ArrayList<>();
        subDeptList = new ArrayList<>();
        areaList = new ArrayList<>();
        addFamilyModel = new AddFamilyModel();
        addFamilyModel.setPhone_code(phone_code);
        addFamilyModel.setPhone(phone);

        if (getUserModel()!=null){
            setUpToolbar(binding.toolbar, getString(R.string.edit_profile), R.color.white, R.color.black);
            binding.btnAdd.setText(getString(R.string.update));
            addFamilyModel.setName(getUserModel().getData().getName());
            addFamilyModel.setArea_id(getUserModel().getData().getArea().getId()+"");
            addFamilyModel.setAddress(getUserModel().getData().getAddress());
            addFamilyModel.setLat(Double.parseDouble(getUserModel().getData().getLatitude()));
            addFamilyModel.setLat(Double.parseDouble(getUserModel().getData().getLongitude()));
            addFamilyModel.setPhone_code(getUserModel().getData().getPhone_code());
            addFamilyModel.setPhone(getUserModel().getData().getPhone());
            addFamilyModel.setDepartment_id(getUserModel().getData().getUser_basic_department().getId());
            addFamilyModel.setSub_department_id(getUserModel().getData().getUser_sub_department().getId());
            if (getUserModel().getData().getLogo()!=null){
                addFamilyModel.setImage(getUserModel().getData().getLogo());
                Picasso.get().load(Uri.parse(getUserModel().getData().getLogo())).into(binding.image);
                binding.avatar.setVisibility(View.GONE);
            }else {
                addFamilyModel.setImage("");
                binding.avatar.setVisibility(View.VISIBLE);


            }

        }
        binding.setModel(addFamilyModel);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {

                    Uri uri = getImageUri(result.getData());

                    binding.avatar.setVisibility(View.GONE);
                    binding.image.setImageURI(uri);
                    addFamilyModel.setImage(uri.toString());

                }
            } else if (req == 2) {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = getImageUri(result.getData());

                    binding.imageBanner.setImageURI(uri);
                    addFamilyModel.setImageBanner(uri.toString());

                }
            } else if (req == 3) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    SelectedLocation selectedLocation = (SelectedLocation) result.getData().getSerializableExtra("location");
                    if (selectedLocation != null) {
                        addFamilyModel.setAddress(selectedLocation.getAddress());
                        addFamilyModel.setLat(selectedLocation.getLat());
                        addFamilyModel.setLng(selectedLocation.getLng());
                        Log.e("address",selectedLocation.getAddress());
                        binding.setModel(addFamilyModel);
                    }


                }
            }

        });

        spinnerDepartmentAdapter = new SpinnerDepartmentAdapter(deptList, this);
        binding.spinnerDept.setAdapter(spinnerDepartmentAdapter);

        binding.spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addFamilyModel.setDepartment_id(deptList.get(i).getId());
                updateSubDepartmentData(deptList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerSubDepartmentAdapter = new SpinnerDepartmentAdapter(subDeptList, this);
        binding.spinnerSubDept.setAdapter(spinnerSubDepartmentAdapter);
        binding.spinnerSubDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addFamilyModel.setSub_department_id(subDeptList.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerAreaAdapter = new SpinnerAreaAdapter(areaList, this);
        binding.spinnerArea.setAdapter(spinnerAreaAdapter);
        binding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addFamilyModel.setArea_id(areaList.get(position).getId() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.flImage.setOnClickListener(v -> {
            req = 1;
            checkReadPermission();
        });

        binding.flBanner.setOnClickListener(v -> {
            req = 2;
            checkReadPermission();
        });

        binding.llMap.setOnClickListener(v -> {
            req = 3;
            Intent intent = new Intent(this, MapActivity.class);
            launcher.launch(intent);
        });

        binding.btnAdd.setOnClickListener(v -> {
            if (addFamilyModel.isDataValid(this)) {
                if (getUserModel()==null){
                    signUp();
                }else {
                    updateProfile();
                }
            }
        });

        getCityData();
        getDepartment();

    }



    private void signUp() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        MultipartBody.Part imagePart = null;
        if (!addFamilyModel.getImage().isEmpty()){
            imagePart = Common.getMultiPartImage(this,Uri.parse(addFamilyModel.getImage()), "logo");

        }
        RequestBody name_part = Common.getRequestBodyText(addFamilyModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(addFamilyModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(addFamilyModel.getPhone());
        RequestBody area_id_part = Common.getRequestBodyText(addFamilyModel.getArea_id());
        RequestBody address_part = Common.getRequestBodyText(addFamilyModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(addFamilyModel.getLat()+"");
        RequestBody lng_part = Common.getRequestBodyText(addFamilyModel.getLng()+"");
        RequestBody dept_part = Common.getRequestBodyText(addFamilyModel.getDepartment_id());
        RequestBody sub_dept_part = Common.getRequestBodyText(addFamilyModel.getSub_department_id());

        RequestBody software_part = Common.getRequestBodyText("android");


        Api.getService(Tags.base_url)
                .familySignUp(name_part,area_id_part,phone_code_part,phone_part,lat_part,lng_part,address_part,dept_part,sub_dept_part,software_part, imagePart)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("status",response.body().getStatus()+"__");
                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());
                                setResult(RESULT_OK);
                                finish();

                            }
                        } else {
                            try {
                                Log.e("error",response.code()+response.errorBody().string());
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
                                Log.e("failed", t.toString() + "__");

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

    private void updateProfile()
    {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        MultipartBody.Part imagePart = null;
        if (!addFamilyModel.getImage().isEmpty()&&!addFamilyModel.getImage().startsWith("http")){
            imagePart = Common.getMultiPartImage(this,Uri.parse(addFamilyModel.getImage()), "logo");

        }
        RequestBody user_id_part = Common.getRequestBodyText(getUserModel().getData().getId());
        RequestBody name_part = Common.getRequestBodyText(addFamilyModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(addFamilyModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(addFamilyModel.getPhone());
        RequestBody area_id_part = Common.getRequestBodyText(addFamilyModel.getArea_id());
        RequestBody address_part = Common.getRequestBodyText(addFamilyModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(addFamilyModel.getLat()+"");
        RequestBody lng_part = Common.getRequestBodyText(addFamilyModel.getLng()+"");
        RequestBody dept_part = Common.getRequestBodyText(addFamilyModel.getDepartment_id());
        RequestBody sub_dept_part = Common.getRequestBodyText(addFamilyModel.getSub_department_id());

        RequestBody software_part = Common.getRequestBodyText("android");


        Api.getService(Tags.base_url)
                .updateProfileFamily("Bearer "+getUserModel().getData().getToken(),user_id_part,name_part,area_id_part,phone_code_part,phone_part,lat_part,lng_part,address_part,dept_part,sub_dept_part,software_part, imagePart)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("status",response.body().getStatus()+"__");

                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());
                                setResult(RESULT_OK);
                                finish();

                            }
                        } else {
                            try {
                                Log.e("error", response.code()+response.errorBody().string());
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
                                Log.e("failed", t.toString() + "__");

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
    private void updateSubDepartmentData(DepartmentModel.BasicDepartmentFk basicDepartmentFk)
    {
        subDeptList.clear();
        subDeptList.addAll(basicDepartmentFk.getSub_department());
        spinnerSubDepartmentAdapter.notifyDataSetChanged();

        if (basicDepartmentFk.getSub_department().size()>0){
            addFamilyModel.setSub_department_id(basicDepartmentFk.getSub_department().get(0).getId());
            if (getUserModel()!=null){
                binding.spinnerSubDept.setSelection(getSubDeptPos());

            }
        }

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
                                addFamilyModel.setArea_id(areaList.get(0).getId() + "");

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

    private void getDepartment() {
        Api.getService(Tags.base_url).getFamilyDepartmentWithSubDepartment().enqueue(new Callback<DepartmentDataModel>() {
            @Override
            public void onResponse(Call<DepartmentDataModel> call, Response<DepartmentDataModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        deptList.clear();
                        deptList.addAll(response.body().getData());
                        runOnUiThread(() -> {
                            spinnerDepartmentAdapter.notifyDataSetChanged();
                        });
                        if (deptList.size()>0){
                            addFamilyModel.setDepartment_id(deptList.get(0).getId());
                            if (getUserModel()!=null){
                                binding.spinnerDept.setSelection(getDeptPos());
                            }
                            updateSubDepartmentData(deptList.get(0));

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
            public void onFailure(Call<DepartmentDataModel> call, Throwable t) {
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

    private int getDeptPos() {
        int pos = 0;
        for (int index = 0; index < deptList.size(); index++) {
            if (deptList.get(index).getId().equals(getUserModel().getData().getUser_basic_department().getBasic_department_fk().getId())) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private int getSubDeptPos() {
        int pos = 0;
        for (int index = 0; index < subDeptList.size(); index++) {
            Log.e("ids",subDeptList.get(index).getId()+"________"+getUserModel().getData().getUser_sub_department().getBasic_department_fk().getId());
            if (subDeptList.get(index).getId().equals(getUserModel().getData().getUser_sub_department().getBasic_department_fk().getId())) {
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
        /*Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cameraResolveInfo = getPackageManager().queryIntentActivities(cameraIntent, 0);

        for (ResolveInfo info : cameraResolveInfo) {
            Intent intent = new Intent(cameraIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            allIntents.add(intent);
        }*/


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

        } else if (req == 2) {
            outPutUri = Uri.fromFile(new File(file, "banner.png"));

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