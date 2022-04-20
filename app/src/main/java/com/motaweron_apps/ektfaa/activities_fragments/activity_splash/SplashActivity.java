package com.motaweron_apps.ektfaa.activities_fragments.activity_splash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_choose_location.ChooseLocationActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.HomeActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_intro_slider.IntroSliderActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_language.LanguageActivity;
import com.motaweron_apps.ektfaa.databinding.ActivitySplashBinding;
import com.motaweron_apps.ektfaa.language.Language;
import com.motaweron_apps.ektfaa.location.UpdateLocation;
import com.motaweron_apps.ektfaa.model.UserSettingsModel;

import io.paperdb.Paper;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private int locReq =1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        initView();

    }

    private void initView() {
        if (getUserModel()!=null&&getUserModel().getData().getUser_type().equals(BaseActivity.DRIVER)){
            checkLocationPermission();
        }else {
            new Handler().postDelayed(() -> {
                if (getUserSettings()==null){
                    NavigateToLanguageActivity();

                }else {
                    if (getUserSettings().isLanguageSelected()){

                        if (getUserSettings().isFirstTime()){
                            navigateToIntroSlider();

                        }else {
                            if(getArea()!=null){
                                NavigateToHomeActivity();}
                            else {

                                NavigateToLocationActivity();
                            }
                        }



                    }else {
                        NavigateToLanguageActivity();

                    }


                }


            }, 2000);
        }


    }

    private void navigateToIntroSlider() {
        Intent intent = new Intent(this, IntroSliderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    private void NavigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void NavigateToLocationActivity() {

        Intent intent = new Intent(this, ChooseLocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void NavigateToLanguageActivity() {

        Intent intent = new Intent(this, LanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    private void refreshActivity(String lang) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Paper.init(this);
            Paper.book().write("lang", lang);
            Language.updateResources(this, lang);
            UserSettingsModel model =getUserSettings();
            if (getUserSettings()== null) {
                model = new UserSettingsModel();
            }
            model.setLanguageSelected(true);
            setUserSettings(model);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }, 1500);


    }

    private void startSplash(){

        try {
            if (getUserModel()!=null&&getUserModel().getData().getUser_type().equals(BaseActivity.DRIVER)){
                Intent intent = new Intent(this, UpdateLocation.class);

                getApplicationContext().startService(intent);
            }

        }catch (Exception e){}


        new Handler().postDelayed(() -> {
            if (getUserSettings()==null){
                NavigateToLanguageActivity();

            }else {
                if (getUserSettings().isLanguageSelected()){

                    if (getUserSettings().isFirstTime()){
                        navigateToIntroSlider();

                    }else {
                        if(getArea()!=null){
                            NavigateToHomeActivity();}
                        else {

                            NavigateToLocationActivity();
                        }
                    }


                }else {
                    NavigateToLanguageActivity();

                }


            }


        }, 2000);
    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,BaseActivity.fineLocPerm)== PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,BaseActivity.bgLocPerm)== PackageManager.PERMISSION_GRANTED
        ){
            startSplash();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{BaseActivity.fineLocPerm,BaseActivity.bgLocPerm},locReq);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==locReq&&grantResults.length>0){
            startSplash();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String lang = data.getStringExtra("lang");
            refreshActivity(lang);
        }
    }
}

