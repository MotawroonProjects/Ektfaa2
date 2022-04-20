package com.motaweron_apps.ektfaa.activities_fragments.activity_client_offer_details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.databinding.ActivityClientOfferDetailsBinding;
import com.motaweron_apps.ektfaa.databinding.ActivityDelegateAddOfferBinding;
import com.motaweron_apps.ektfaa.model.OrderModel;
import com.motaweron_apps.ektfaa.model.SingleOrderModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientOfferDetialsActivity extends BaseActivity implements OnMapReadyCallback {
    private ActivityClientOfferDetailsBinding binding;
    private GoogleMap mMap;
    private OrderModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_offer_details);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (OrderModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        binding.setLang(getLang());
        binding.setModel(model);
        binding.close.setOnClickListener(v -> {
            super.onBackPressed();
        });

        binding.btnAccept.setOnClickListener(v -> {
            acceptOffer();
        });

        binding.btnRefuse.setOnClickListener(v -> {
            refuseOffer();
        });
        getOrderById();
    }


    private void acceptOffer() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptOffer("Bearer " + getUserModel().getData().getToken(), model.getUser_id(), model.getDriver_id(), model.getId(), "client_accept_offer", null)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Intent intent = getIntent();
                                intent.putExtra("order_id", model.getId());
                                setResult(RESULT_OK, intent);
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

    private void refuseOffer() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptOffer("Bearer " + getUserModel().getData().getToken(), model.getUser_id(), model.getDriver_id(), model.getId(), "refuse_and_request_other_offer", null)
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

    private void getOrderById() {
        binding.llData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).getOrderById("Bearer " + getUserModel().getData().getToken(), model.getId())
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null) {
                            model = response.body().getData().getOrder();
                            binding.setModel(model);

                            updateUI();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            addMarker(Double.parseDouble(model.getFrom_latitude()), Double.parseDouble(model.getFrom_longitude()), 1);
            addMarker(Double.parseDouble(model.getTo_latitude()), Double.parseDouble(model.getTo_longitude()), 2);

        }
    }

    private void addMarker(double lat, double lng, int type) {
        View view = LayoutInflater.from(this).inflate(R.layout.map_add_offer_location_row, null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        if (type == 1) {
            tvTitle.setText(getString(R.string.pick_up_location));
            tvTitle.setBackgroundResource(R.drawable.rounded_primary);
        } else {
            tvTitle.setText(getString(R.string.drop_off_location));
            tvTitle.setBackgroundResource(R.drawable.rounded_second);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(Double.parseDouble(model.getFrom_latitude()), Double.parseDouble(model.getFrom_longitude())));
            builder.include(new LatLng(Double.parseDouble(model.getTo_latitude()), Double.parseDouble(model.getTo_longitude())));

            try {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 180));

            } catch (Exception e) {

            }

        }
        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setContentPadding(2, 2, 2, 2);
        iconGenerator.setBackground(null);
        iconGenerator.setContentView(view);


        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).position(new LatLng(lat, lng)));


    }


}