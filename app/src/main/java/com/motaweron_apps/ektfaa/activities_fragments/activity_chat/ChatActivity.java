package com.motaweron_apps.ektfaa.activities_fragments.activity_chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.ChatAdapter;
import com.motaweron_apps.ektfaa.adapter.ReasonsAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityAboutAppBinding;
import com.motaweron_apps.ektfaa.databinding.ActivityChatBinding;
import com.motaweron_apps.ektfaa.model.FirebaseNotModel;
import com.motaweron_apps.ektfaa.model.OrderChatModel;
import com.motaweron_apps.ektfaa.model.PlaceGeocodeData;
import com.motaweron_apps.ektfaa.model.ReasonDataModel;
import com.motaweron_apps.ektfaa.model.ReasonModel;
import com.motaweron_apps.ektfaa.model.SingleMessageModel;
import com.motaweron_apps.ektfaa.model.SingleOrderModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivityChatBinding binding;
    private String order_id = "";
    private SingleOrderModel.Data orderModel;
    private Animation animation;
    private List<ReasonModel> reasonList;
    private ReasonsAdapter reasonsAdapter;
    private List<OrderChatModel.OrderChatMessage> messageList;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int CAMERA_REQ = 1;
    private Uri uri = null;
    private ChatAdapter chatAdapter;
    private ReasonModel selectedReason;
    private boolean statusChanged = false;
    private int dy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        order_id = intent.getStringExtra("data");
        setRoomId(order_id);

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.back), R.color.white, R.color.black);
        reasonList = new ArrayList<>();
        messageList = new ArrayList<>();
        binding.setLang(getLang());

        LinearLayoutManager manager =new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        chatAdapter = new ChatAdapter(messageList, this, getUserModel().getData().getId());

        binding.recView.setAdapter(chatAdapter);

        binding.recView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            binding.recView.postDelayed(() -> binding.recView.smoothScrollToPosition(messageList.size()), 50);
            return;

        });

        reasonsAdapter = new ReasonsAdapter(reasonList, this, getLang());
        binding.recViewReason.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewReason.setAdapter(reasonsAdapter);

        binding.flSheet.setOnClickListener(v -> closeSheet());

        binding.imageMenu.setOnClickListener(v -> {
            binding.flMenu.setVisibility(View.VISIBLE);
            openSheet();
        });

        binding.btnMenuCancel.setOnClickListener(v -> closeSheet());

        binding.tvMenuCancelOrder.setOnClickListener(v -> {
            binding.flAlert.setVisibility(View.GONE);
            binding.flCancelReasons.setVisibility(View.VISIBLE);
            binding.flMenu.setVisibility(View.GONE);
            binding.flLocation.setVisibility(View.GONE);

        });
        binding.btnDelivered.setOnClickListener(v -> {
            driverDeliveredOrder();
        });
        binding.tvCancel.setOnClickListener(v -> {
            closeSheet();

        });

        binding.tvMenuShareLocation.setOnClickListener(v -> {
            binding.flAlert.setVisibility(View.GONE);
            binding.flCancelReasons.setVisibility(View.GONE);
            binding.flMenu.setVisibility(View.GONE);
            binding.flLocation.setVisibility(View.VISIBLE);
            checkPermission();
        });

        binding.tvAlertDelete.setOnClickListener(v -> {
            if (getUserModel().getData().getId().equals(orderModel.getOrder().getUser().getId())) {
                clientRefuseOrder();
            } else {
                driverRefuseOrder();
            }
        });
        binding.btnAlertCancel.setOnClickListener(v -> {
            closeSheet();

        });

        binding.imageCamera.setOnClickListener(v -> checkCameraPermission());

        binding.btnSend.setOnClickListener(v -> {
            String msg = binding.edtMsg.getText().toString();
            if (!msg.isEmpty()) {
                sendMessage(msg);
            }
        });
        getOrderById();

        EventBus.getDefault().register(this);
    }


    private void openSheet() {
        selectedReason = null;
        binding.flSheet.clearAnimation();
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        binding.flSheet.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flSheet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void closeSheet() {
        binding.flSheet.clearAnimation();
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        binding.flSheet.startAnimation(animation);
        binding.flAlert.setVisibility(View.GONE);
        binding.flCancelReasons.setVisibility(View.GONE);
        binding.flMenu.setVisibility(View.GONE);
        binding.flLocation.setVisibility(View.GONE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flSheet.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getOrderById() {
        binding.progBarData.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).getOrderById("Bearer " + getUserModel().getData().getToken(), order_id)
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        binding.progBarData.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            orderModel = response.body().getData();
                            updateUi();
                        } else {
                            binding.progBarData.setVisibility(View.VISIBLE);

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                        try {
                            binding.progBarData.setVisibility(View.VISIBLE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });
    }

    private void updateUi() {
        String name = "";
        String image = "";
        if (getUserModel().getData().getId().equals(orderModel.getOrder().getUser().getId())) {
            name = orderModel.getOrder().getDriver().getName();
            image = orderModel.getOrder().getDriver().getLogo();

        } else {
            name = orderModel.getOrder().getUser().getName();
            image = orderModel.getOrder().getUser().getLogo();
        }
        binding.setName(name);
        binding.setImage(image);
        binding.setModel(orderModel);
        messageList.clear();
        messageList.addAll(orderModel.getOrder_chat().getOrder_chat_messages());
        chatAdapter.notifyDataSetChanged();
        binding.recView.postDelayed(() -> binding.recView.scrollToPosition(messageList.size() - 1), 10);
        updateOrderStatusUi(orderModel.getOrder().getStatus());
        binding.llData.setVisibility(View.VISIBLE);

    }

    private void updateOrderStatusUi(String status) {
        if (status.equals("client_accept_offer")) {
            binding.llDriverData.setVisibility(View.VISIBLE);
            binding.flOrderCanceled.setVisibility(View.GONE);
            binding.llMsg.setVisibility(View.VISIBLE);
            binding.flOrderDelivered.setVisibility(View.GONE);


        } else {
            if (status.equals("driver_refuse_order") || status.equals("client_refuse_order_after_offer")) {
                binding.flOrderCanceled.setVisibility(View.VISIBLE);
            } else if (status.equals("driver_deliveried_order_to_client") || status.equals("client_rate_driver")) {
                binding.flOrderDelivered.setVisibility(View.VISIBLE);
                binding.flOrderCanceled.setVisibility(View.GONE);

            }
            binding.llMsg.setVisibility(View.GONE);
            binding.llDriverData.setVisibility(View.GONE);


        }


        if (getUserModel().getData().getId().equals(orderModel.getOrder().getUser().getId())) {
            binding.btnDelivered.setVisibility(View.GONE);
            if (status.equals("client_accept_offer")) {
                getClientCancelReasons();

            }
        } else {
            if (status.equals("client_accept_offer")) {
                binding.btnDelivered.setVisibility(View.VISIBLE);
                getDriverCancelReasons();

            } else {
                binding.btnDelivered.setVisibility(View.GONE);

            }
        }
    }

    private void getClientCancelReasons() {
        Api.getService(Tags.base_url).getClientRefuseReasons("Bearer " + getUserModel().getData().getToken())
                .enqueue(new Callback<ReasonDataModel>() {
                    @Override
                    public void onResponse(Call<ReasonDataModel> call, Response<ReasonDataModel> response) {

                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            reasonList.clear();
                            reasonList.addAll(response.body().getData());
                            reasonsAdapter.notifyDataSetChanged();
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<ReasonDataModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });
    }

    private void getDriverCancelReasons() {
        Api.getService(Tags.base_url).getDriverRefuseReasons("Bearer " + getUserModel().getData().getToken())
                .enqueue(new Callback<ReasonDataModel>() {
                    @Override
                    public void onResponse(Call<ReasonDataModel> call, Response<ReasonDataModel> response) {

                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            reasonList.clear();
                            reasonList.addAll(response.body().getData());
                            reasonsAdapter.notifyDataSetChanged();
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<ReasonDataModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });
    }

    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        try {
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, req);
        } catch (SecurityException e) {
            Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(ChatActivity.this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        getGeoData(lat, lng);

        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }


    }

    private void getGeoData(double lat, double lng) {
        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, getLang(), getString(R.string.search_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        closeSheet();

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getResults().size() > 0) {
                                String address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                sendLocationMessage(address, String.valueOf(lat), String.valueOf(lng));


                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {

                            Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            startLocationUpdate();
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                this.uri = uri;
                uploadImage(uri);

            }


        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }

    private void sendMessage(String msg) {
        binding.edtMsg.setText("");
        String to_user_id = "";
        if (getUserModel().getData().getId().equals(orderModel.getOrder().getUser().getId())) {
            to_user_id = orderModel.getOrder().getDriver().getId();
        } else {
            to_user_id = orderModel.getOrder().getUser().getId();

        }
        Api.getService(Tags.base_url)
                .sendChatMessage("Bearer " + getUserModel().getData().getToken(), orderModel.getOrder_chat().getId(), getUserModel().getData().getId(), to_user_id, "message", msg)
                .enqueue(new Callback<SingleMessageModel>() {
                    @Override
                    public void onResponse(Call<SingleMessageModel> call, Response<SingleMessageModel> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                OrderChatModel.OrderChatMessage model = response.body().getData();
                                messageList.add(model);
                                chatAdapter.notifyItemInserted(messageList.size());
                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<SingleMessageModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("Error", t.getMessage());

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().contains("socket")) {

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void sendLocationMessage(String msg, String latitude, String longitude) {
        String to_user_id = "";
        if (getUserModel().getData().getId().equals(orderModel.getOrder().getUser().getId())) {
            to_user_id = orderModel.getOrder().getDriver().getId();
        } else {
            to_user_id = orderModel.getOrder().getUser().getId();

        }
        Api.getService(Tags.base_url)
                .sendLocationChatMessage("Bearer " + getUserModel().getData().getToken(), orderModel.getOrder_chat().getId(), getUserModel().getData().getId(), to_user_id, "location", msg, latitude, longitude)
                .enqueue(new Callback<SingleMessageModel>() {
                    @Override
                    public void onResponse(Call<SingleMessageModel> call, Response<SingleMessageModel> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                OrderChatModel.OrderChatMessage model = response.body().getData();
                                messageList.add(model);
                                chatAdapter.notifyItemInserted(messageList.size());
                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<SingleMessageModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("Error", t.getMessage());

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().contains("socket")) {

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void uploadImage(Uri uri) {
        String to_user_id = "";
        if (getUserModel().getData().getId().equals(orderModel.getOrder().getUser().getId())) {
            to_user_id = orderModel.getOrder().getDriver().getId();
        } else {
            to_user_id = orderModel.getOrder().getUser().getId();

        }
        Intent intent = new Intent(this, ServiceUploadAttachment.class);
        intent.putExtra("file_uri", uri.toString());
        intent.putExtra("user_token", getUserModel().getData().getToken());
        intent.putExtra("user_id", getUserModel().getData().getId());
        intent.putExtra("to_user_id", to_user_id);
        intent.putExtra("room_id", orderModel.getOrder_chat().getId());
        intent.putExtra("attachment_type", "image");
        startService(intent);
    }


    private void clientRefuseOrder() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptOffer("Bearer " + getUserModel().getData().getToken(), orderModel.getOrder().getUser().getId(), orderModel.getOrder().getDriver().getId(), order_id, "client_refuse_order_after_offer", selectedReason.getTitle_ar())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        Log.e("status", response.body().getStatus() + "");
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
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

    private void driverRefuseOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .refuseOrder("Bearer " + getUserModel().getData().getToken(), orderModel.getOrder().getUser().getId(), orderModel.getOrder().getDriver().getId(), order_id, "driver_refuse_order", selectedReason.getTitle_ar())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        Log.e("status", response.body().getStatus() + "");

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
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

    private void driverDeliveredOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deliveredOrder("Bearer " + getUserModel().getData().getToken(), orderModel.getOrder().getUser().getId(), orderModel.getOrder().getDriver().getId(), order_id)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttachmentSuccess(OrderChatModel.OrderChatMessage messageModel) {
        messageList.add(messageModel);
        chatAdapter.notifyItemChanged(messageList.size());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusChanged(FirebaseNotModel model) {
        statusChanged = true;
        updateOrderStatusUi(model.getOrder_status());
    }


    public void setLocationItem(OrderChatModel.OrderChatMessage model) {
        String latitude = model.getLatitude();
        String longitude = model.getLongitude();

        String uri = "geo:" + latitude + ","
                + longitude + "?q=" + latitude
                + "," + longitude;
        startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uri)));
    }

    public void setItemReason(ReasonModel reasonModel) {
        selectedReason = reasonModel;
        binding.flAlert.setVisibility(View.VISIBLE);
        binding.flCancelReasons.setVisibility(View.GONE);
        binding.flMenu.setVisibility(View.GONE);
        binding.flLocation.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (binding.flSheet.getVisibility() == View.VISIBLE) {
            closeSheet();
        } else {
            if (statusChanged) {
                setResult(RESULT_OK);
                finish();
            } else {
                super.onBackPressed();

            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRoomId("");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


}