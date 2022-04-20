package com.motaweron_apps.ektfaa.activities_fragments.activity_chalets_detials;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.AttrDetailsAdapter;
import com.motaweron_apps.ektfaa.adapter.SliderChaletsAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityChaletsDetialsBinding;
import com.motaweron_apps.ektfaa.model.PlaceModel;
import com.motaweron_apps.ektfaa.model.SingleChaletsModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaletsDetialsActivity extends BaseActivity implements  OnMapReadyCallback {
    private ActivityChaletsDetialsBinding binding;
    private PlaceModel placeModel;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private SliderChaletsAdapter sliderChaletsAdapter;
    private String chalet_id;
    private AttrDetailsAdapter attrDetailsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chalets_detials);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        chalet_id =intent.getStringExtra("data");
    }

    private void initView() {
        getChaletData();
        binding.setLang(getLang());
        binding.cardBack.setOnClickListener(view -> finish());
        binding.cardCall.setOnClickListener(view -> {
            String phone = "+" + placeModel.getUser().getPhone_code() + placeModel.getUser().getPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        });
        binding.cardWhatsApp.setOnClickListener(view -> {
            String phone = "+" + placeModel.getUser().getPhone_code() + placeModel.getUser().getPhone();

            String url = "https://api.whatsapp.com/send?phone=" + phone;
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void updateUi(PlaceModel placeModel)
    {
        this.placeModel = placeModel;
        updateFragmentUI();
        binding.progBar.setVisibility(View.GONE);
        binding.llData.setVisibility(View.VISIBLE);
        List<UserModel.ImagesData> sliderList = new ArrayList<>(placeModel.getImages_data());
        sliderChaletsAdapter = new SliderChaletsAdapter(sliderList,this);
        binding.pager.setAdapter(sliderChaletsAdapter);
        binding.tab.setupWithViewPager(binding.pager);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        attrDetailsAdapter = new AttrDetailsAdapter(this,placeModel.getPlace_details());
        binding.recView.setAdapter(attrDetailsAdapter);
        binding.setModel(placeModel);
    }
    private void getChaletData()
    {

        binding.progBar.setVisibility(View.VISIBLE);
        binding.llData.setVisibility(View.GONE);
        Api.getService(Tags.base_url).getChaletById(chalet_id).enqueue(new Callback<SingleChaletsModel>() {
            @Override
            public void onResponse(Call<SingleChaletsModel> call, Response<SingleChaletsModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        updateUi(response.body().getData());

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
            public void onFailure(Call<SingleChaletsModel> call, Throwable t) {
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());

                    }

                } catch (Exception e) {
                }

            }
        });


    }
    public void addMarker(double lat, double lng, String address)
    {



        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            marker.setPosition(new LatLng(lat, lng));


        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
    }
    private void updateFragmentUI()
    {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (fragment!=null){
            fragment.getMapAsync(this);

        }


    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            addMarker(Double.parseDouble(placeModel.getLatitude()),Double.parseDouble(placeModel.getLongitude()),placeModel.getAddress());

        }
    }




}