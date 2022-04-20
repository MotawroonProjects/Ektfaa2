package com.motaweron_apps.ektfaa.activities_fragments.activity_add_ads;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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
import android.widget.Toast;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.databinding.ActivityAddAdsBinding;
import com.motaweron_apps.ektfaa.databinding.ActivityOpenYourWorkBinding;
import com.motaweron_apps.ektfaa.model.SettingModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdsActivity extends BaseActivity {

    private ActivityAddAdsBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private Uri uri;
    private int amount = 1;
    private SettingModel.Setting setting;
    private double adsCostPerDay =0.0;
    private double totalCost = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ads);
        binding.setLifecycleOwner(this);

        initView();

    }


    private void initView() {

        setUpToolbar(binding.toolbar, getString(R.string.add_ads), R.color.white, R.color.black);
        binding.setAmount(String.valueOf(amount));
        binding.setCost(adsCostPerDay+"");
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {

                    uri = getImageUri(result.getData());
                    binding.image.setImageURI(uri);
                    binding.icon.setVisibility(View.GONE);

                }

            }

        });

        binding.flImage.setOnClickListener(v -> {
            req = 1;
            checkReadPermission();
        });


        binding.imageIncrease.setOnClickListener(v -> {
            amount +=1;
            binding.setAmount(String.valueOf(amount));
            totalCost = amount*adsCostPerDay;
            binding.setCost(totalCost+"");
        });

        binding.imageDecrease.setOnClickListener(v -> {
            if (amount>1){
                amount -=1;
                totalCost = amount*adsCostPerDay;
                binding.setCost(totalCost+"");
                binding.setAmount(String.valueOf(amount));
            }

        });


        binding.tvPayment.setOnClickListener(v -> {
            String url = binding.edtLink.getText().toString();
            if (uri != null && !url.isEmpty()) {
                binding.edtLink.setError(null);
                Common.CloseKeyBoard(this, binding.edtLink);
                addAds(url);
            } else {

                if (uri == null) {
                    Toast.makeText(this, R.string.ch_ad_photo, Toast.LENGTH_SHORT).show();
                }

                if (url.isEmpty()) {
                    binding.edtLink.setError(getString(R.string.field_required));

                } else {
                    binding.edtLink.setError(null);

                }
            }
        });

        getSetting();

    }

    private void getSetting() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getSetting()
                .enqueue(new Callback<SettingModel>() {
                    @Override
                    public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            setting = response.body().getData();
                            try {
                                adsCostPerDay = Double.parseDouble(setting.getAdvertisement_price());

                            }catch (Exception e){

                            }
                            binding.setCost(setting.getAdvertisement_price());
                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<SettingModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");


                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void addAds(String url) {
        if (getUserModel() == null) {
            Toast.makeText(this, getString(R.string.si_su), Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody user_id_part = Common.getRequestBodyText(getUserModel().getData().getId());
        RequestBody day_num_part = Common.getRequestBodyText(amount + "");
        RequestBody link_part = Common.getRequestBodyText(url);
        RequestBody total_part = Common.getRequestBodyText(totalCost+"");



        MultipartBody.Part image = Common.getMultiPartImage(this, uri, "image");


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addAds("Bearer " + getUserModel().getData().getToken(), user_id_part, link_part, day_num_part,total_part, image)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(AddAdsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("err", t.toString() + "__");
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


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
       /* Intent cameraIntent = new Intent();
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
            outPutUri = Uri.fromFile(new File(file, "ads.png"));

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
        if (requestCode ==100&&grantResults.length >= 3) {
            openGallery();
        } else {
            Toast.makeText(this, "Access image denied", Toast.LENGTH_SHORT).show();
        }
    }

}