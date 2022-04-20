package com.motaweron_apps.ektfaa.activities_fragments.activity_home.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseFragment;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalets.ChaletsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_delegets.DelegetsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.GeneralHomeMvvm;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.HomeActivity;


import com.motaweron_apps.ektfaa.activities_fragments.activity_location.LocationsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_map.MapActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_productive_families.ProductiveFamiliesActivity;
import com.motaweron_apps.ektfaa.adapter.SliderAdapter;
import com.motaweron_apps.ektfaa.databinding.FragmentHomeBinding;
import com.motaweron_apps.ektfaa.model.SelectedLocation;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.model.Slider_Model;
import com.motaweron_apps.ektfaa.model.UserSettingsModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends BaseFragment {

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private SliderAdapter sliderAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private GeneralHomeMvvm generalHomeMvvm;


    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK && result.getData() != null) {
              SingleAreaModel model = (SingleAreaModel) result.getData().getSerializableExtra("data");
              binding.setArea(model.getTitle());
              setArea(model);

            } else if (req == 2 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                SelectedLocation selectedLocation = (SelectedLocation) result.getData().getSerializableExtra("location");
                UserSettingsModel userSettingsModel = getUserSettings();
                if (userSettingsModel==null){
                    userSettingsModel = new UserSettingsModel();
                }
                userSettingsModel.setUser_address(selectedLocation.getAddress());
                userSettingsModel.setUser_lat(selectedLocation.getLat());
                userSettingsModel.setUser_lng(selectedLocation.getLng());
                setUserSettings(userSettingsModel);
                binding.flLocation.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);

            }
        });
    }

    private void initView() {
        generalHomeMvvm = ViewModelProviders.of(activity).get(GeneralHomeMvvm.class);
        binding.setArea(getArea().getTitle());

        generalHomeMvvm.getOnAreaSuccess().observe(activity,areaList->{
            if (areaList.size()==0){
                setArea(null);
                binding.setArea(getString(R.string.choose_area));

            }else {
                binding.setArea(getArea().getTitle());

            }
        });



        activity = (HomeActivity) getActivity();


        binding.tvedit.setPaintFlags(binding.tvedit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        binding.tvedit.setOnClickListener(v -> {
            req=1;
            Intent intent = new Intent(activity, LocationsActivity.class);
            launcher.launch(intent);
        });


        binding.card1.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ProductiveFamiliesActivity.class);
            startActivity(intent);
        });
        binding.card2.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ChaletsActivity.class);
            startActivity(intent);
        });
        binding.card3.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DelegetsActivity.class);
            startActivity(intent);
        });
        binding.pager.setClipToPadding(false);
        binding.pager.setPageMargin(15);
        binding.pager.setPadding(70, 2, 70, 0);
        getSlider();

        if (getUserSettings()==null){

            binding.flLocation.setVisibility(View.VISIBLE);
            binding.consData.setVisibility(View.GONE);
        }else {

            if (getUserSettings().getUser_address().isEmpty()&&getUserSettings().getUser_lat()==0.0&&getUserSettings().getUser_lng()==0.0){
                binding.flLocation.setVisibility(View.VISIBLE);
                binding.scrollView.setVisibility(View.GONE);
            }else {
                binding.flLocation.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);
            }

        }

        binding.btnLocation.setOnClickListener(v -> {
            req=2;
            Intent intent = new Intent(activity, MapActivity.class);
            launcher.launch(intent);
        });


    }


    public void getSlider() {
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).get_slider(getArea().getId()).enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                binding.progBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200 && response.body().getData() != null) {
                            sliderAdapter = new SliderAdapter(response.body().getData(), activity);
                            binding.pager.setAdapter(sliderAdapter); timer = new Timer();
                            if(response.body().getData().size()>1){
                                timerTask = new MyTask();
                                timer.scheduleAtFixedRate(timerTask, 6000, 6000);
                            }

                        } else {
                            Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();

                        }

                    }

                } else {
                    try {
                        Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });

    }


    public class MyTask extends TimerTask {
        @Override
        public void run() {
          activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }
    }

}
