package com.motaweron_apps.ektfaa.activities_fragments.activity_home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chat.ChatActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_notifications.NotificationActivity;
import com.motaweron_apps.ektfaa.location.UpdateLocation;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.R;

import com.motaweron_apps.ektfaa.databinding.ActivityHomeBinding;
import com.motaweron_apps.ektfaa.language.Language;
import com.motaweron_apps.ektfaa.preferences.Preferences;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;
    private NavController navController;
    private Preferences preferences;

    private UserModel userModel;
    private String lang;
    private boolean backPressed = false;
    private int locReq = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            String order_id = intent.getStringExtra("data");
            String to = intent.getStringExtra("to");
            if (getUserModel() != null) {
                Intent intent2 = null;
                if (to.equals("chat")) {
                    intent2 = new Intent(this, ChatActivity.class);
                    intent2.putExtra("order_id", order_id);
                } else if (to.equals("notification")) {
                    intent2 = new Intent(this, NotificationActivity.class);
                    intent2.putExtra("order_id", order_id);

                }
                startActivity(intent2);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getUserModel() != null) {
            updateFirebaseToken();
        }
    }

    private void initView() {

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        binding.imgNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);

        });


    }


    public void startUpdateLocation() {
        Intent intent = new Intent(this, UpdateLocation.class);
        getApplicationContext().startService(intent);
    }

    public void stopUpdateLocation() {
        Intent intent = new Intent(this, UpdateLocation.class);
        getApplicationContext().stopService(intent);
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, BaseActivity.fineLocPerm) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, BaseActivity.bgLocPerm) == PackageManager.PERMISSION_GRANTED
        ) {
            startUpdateLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{BaseActivity.fineLocPerm, BaseActivity.bgLocPerm}, locReq);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locReq && grantResults.length > 0) {
            startUpdateLocation();
        }
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    public void updateFirebaseToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
                try {
                    Api.getService(Tags.base_url)
                            .updateFirebaseToken("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), token, "android")
                            .enqueue(new Callback<StatusResponse>() {
                                @Override
                                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        UserModel userModel = getUserModel();
                                        if (userModel != null) {
                                            userModel.getData().setFirebase_token(token);
                                            setUserModel(userModel);

                                        }

                                        Log.e("token", "updated successfully");
                                    } else {
                                        try {

                                            Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<StatusResponse> call, Throwable t) {
                                    try {

                                        if (t.getMessage() != null) {
                                            Log.e("errorToken2", t.getMessage());

                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }




}
