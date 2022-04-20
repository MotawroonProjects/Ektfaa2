package com.motaweron_apps.ektfaa.activities_fragments.activity_advertise_with_us;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.databinding.ActivityAdvertiseWithUsBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdvertiseWiithUsActivity extends BaseActivity {

    private ActivityAdvertiseWithUsBinding binding;
    private List<String> images;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advertise_with_us);
        binding.setLifecycleOwner(this);

        initView();

    }
    private void initView()
    {
        setUpToolbar(binding.toolbar,getString(R.string.add_product),R.color.white,R.color.black);
        images = new ArrayList<>();


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK ) {

                    Uri uri = getImageUri(result.getData());
                    if (images.size()<5){
                        images.add(0,uri.toString());

                    }


                }
            }

        });
        binding.flAddImage.setOnClickListener(v ->{
            if (images.size()<5){
              checkReadPermission();
            }
        });


        binding.cardClose.setOnClickListener(v -> {
            binding.flDialog.setVisibility(View.GONE);
        });


    }

    private void addProduct() {

    }


    private void checkReadPermission()
    {
        if (ContextCompat.checkSelfPermission(this, BaseActivity.READ_REQ) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, BaseActivity.WRITE_REQ) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, BaseActivity.CAM_REQ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{BaseActivity.READ_REQ,BaseActivity.WRITE_REQ,BaseActivity.CAM_REQ},100);
        }
    }

    private void openGallery()
    {
        req =1;
        List<Intent> allIntents = new ArrayList<>();
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cameraResolveInfo = getPackageManager().queryIntentActivities(cameraIntent, 0);

        for (ResolveInfo info :cameraResolveInfo){
            Intent intent = new Intent(cameraIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            allIntents.add(intent);
        }


        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo info :resolveInfo){
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);

            allIntents.add(intent);
        }



        Intent mainIntent = allIntents.get(allIntents.size()-1);
        for (Intent intent:allIntents){
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")){
                mainIntent = intent;
                break;
            }
        }

        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Choose image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,allIntents.toArray(new Parcelable[allIntents.size()]));
        launcher.launch(chooserIntent);


    }

    private Uri getCameraUri(){
        Uri outPutUri = null;
        File file = new File(Environment.getExternalStorageDirectory(),"ektfaaApp");
        if (!file.exists()){
            file.mkdirs();
        }
        if (req==1){
            outPutUri = Uri.fromFile(new File(file,"profile.png"));

        }else if (req==2){
            outPutUri = Uri.fromFile(new File(file,"banner.png"));

        }

        return outPutUri;
    }

    private Uri getImageUri(Intent intent){
        boolean isCamera = true;
        if (intent!=null){
            String action = intent.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera?getCameraUri():intent.getData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>=3){
            openGallery();
        }else {
            Toast.makeText(this, "Access image denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.flDialog.getVisibility() == View.VISIBLE) {
            binding.flDialog.setVisibility(View.GONE);

        } else {
            super.onBackPressed();

        }
    }



}