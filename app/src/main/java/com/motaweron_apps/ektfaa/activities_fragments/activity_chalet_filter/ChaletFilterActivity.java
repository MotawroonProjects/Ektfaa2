package com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_filter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.LocationAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityChaletFilterBinding;
import com.motaweron_apps.ektfaa.databinding.ActivitySubCategoryAreaBinding;
import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.ChaletFilterModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaletFilterActivity extends BaseActivity {

    private ActivityChaletFilterBinding binding;
    private List<SingleAreaModel> cityModelList;
    private LocationAdapter cityAdapter;
    private ChaletFilterModel chaletFilterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chalet_filter);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        chaletFilterModel = (ChaletFilterModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.filter), R.color.white, R.color.black);

        cityModelList = new ArrayList<>();
        binding.recViewGovernorate.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new LocationAdapter(cityModelList, this);
        binding.recViewGovernorate.setAdapter(cityAdapter);

        if (chaletFilterModel.getOrder_type().equals("id")) {
            binding.rb1.setChecked(true);
        } else if (chaletFilterModel.getOrder_type().equals("distance")) {
            binding.rb2.setChecked(true);

        } else if (chaletFilterModel.getOrder_type().equals("price") && chaletFilterModel.getOrder_by().equals("asc")) {
            binding.rb3.setChecked(true);

        } else if (chaletFilterModel.getOrder_type().equals("price") && chaletFilterModel.getOrder_by().equals("desc")) {
            binding.rb4.setChecked(true);

        }

        binding.rb1.setOnClickListener(view -> {
            chaletFilterModel.setOrder_type("id");
            chaletFilterModel.setOrder_by("desc");
        });

        binding.rb2.setOnClickListener(view -> {
            chaletFilterModel.setOrder_type("distance");
            chaletFilterModel.setOrder_by("asc");

        });

        binding.rb3.setOnClickListener(view -> {
            chaletFilterModel.setOrder_type("price");
            chaletFilterModel.setOrder_by("asc");

        });

        binding.rb4.setOnClickListener(view -> {
            chaletFilterModel.setOrder_type("price");
            chaletFilterModel.setOrder_by("desc");

        });


        binding.btnConfirm.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.putExtra("data", chaletFilterModel);
            setResult(RESULT_OK, intent);
            finish();
        });


        binding.llArea.setOnClickListener(view -> {
            binding.arrow2.clearAnimation();
            if (binding.expandLayoutGovernorate.isExpanded()) {
                binding.expandLayoutGovernorate.collapse(true);
                binding.arrow2.animate().rotation(0).setDuration(300).start();
            } else {
                binding.expandLayoutGovernorate.setExpanded(true);
                binding.arrow2.animate().rotation(180).setDuration(300).start();


            }
        });

        getCityData();


    }

    private void getCityData() {

        Api.getService(Tags.base_url).getArea().enqueue(new Callback<AllAreaModel>() {
            @Override
            public void onResponse(Call<AllAreaModel> call, Response<AllAreaModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        cityModelList.clear();
                        cityModelList.addAll(response.body().getData());
                        int areaPos = getAreaPos();
                        if (areaPos != -1) {
                            SingleAreaModel model = cityModelList.get(areaPos);
                            model.setSelected(true);
                            cityModelList.set(areaPos, model);
                            cityAdapter.setCurrentPos(areaPos);
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

    private int getAreaPos() {
        int pos = -1;
        for (int index = 0; index < cityModelList.size(); index++) {
            if (String.valueOf(cityModelList.get(index).getId()).equals(chaletFilterModel.getArea_id())) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }


    public void setItemArea(SingleAreaModel model) {
        chaletFilterModel.setArea_id(model.getId() + "");

    }
}