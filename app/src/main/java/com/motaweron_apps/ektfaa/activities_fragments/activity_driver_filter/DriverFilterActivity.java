package com.motaweron_apps.ektfaa.activities_fragments.activity_driver_filter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.FilterCarTypeAdapter;
import com.motaweron_apps.ektfaa.adapter.LocationAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityDriverFilterBinding;
import com.motaweron_apps.ektfaa.model.CarType;
import com.motaweron_apps.ektfaa.model.DriverFilterDataModel;
import com.motaweron_apps.ektfaa.model.DriverFilterModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverFilterActivity extends BaseActivity {
    private ActivityDriverFilterBinding binding;
    private List<CarType> carTypeList;
    private List<SingleAreaModel> cityModelList;
    private LocationAdapter cityAdapter;
    private DriverFilterModel driverFilterModel;
    private FilterCarTypeAdapter carTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_filter);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        driverFilterModel = (DriverFilterModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.filter), R.color.white, R.color.black);
        if (driverFilterModel == null) {
            driverFilterModel = new DriverFilterModel();

        }
        carTypeList = new ArrayList<>();
        cityModelList = new ArrayList<>();
        binding.recViewCarType.setLayoutManager(new LinearLayoutManager(this));
        carTypeAdapter = new FilterCarTypeAdapter(carTypeList, this);
        binding.recViewCarType.setAdapter(carTypeAdapter);

        binding.recViewGovernorate.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new LocationAdapter(cityModelList, this);
        binding.recViewGovernorate.setAdapter(cityAdapter);

        if (driverFilterModel.getOrder_by_type().equals("id")) {
            binding.rbRecently.setChecked(true);
        } else {
            binding.rbNear.setChecked(true);

        }

        binding.rbRecently.setOnClickListener(v -> {
            driverFilterModel.setOrder_by_type("id");
        });

        binding.rbNear.setOnClickListener(v -> {
            driverFilterModel.setOrder_by_type("distance");
        });
        binding.btnConfirm.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.putExtra("data", driverFilterModel);
            setResult(RESULT_OK, intent);
            finish();
        });

        binding.llCarType.setOnClickListener(view -> {
            binding.arrow1.clearAnimation();
            if (binding.expandLayoutDepartment.isExpanded()) {
                binding.expandLayoutDepartment.collapse(true);
                binding.arrow1.animate().rotation(0).setDuration(300).start();
            } else {
                binding.expandLayoutDepartment.setExpanded(true);
                binding.arrow1.animate().rotation(180).setDuration(300).start();


            }
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


        getData();

    }

    private void getData() {
        carTypeList.clear();

        cityModelList.clear();
        cityAdapter.notifyDataSetChanged();

        Api.getService(Tags.base_url)
                .getDriverAreaCarType()
                .enqueue(new Callback<DriverFilterDataModel>() {
                    @Override
                    public void onResponse(Call<DriverFilterDataModel> call, Response<DriverFilterDataModel> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                carTypeList.addAll(response.body().getData().getCar_types());

                                cityModelList.addAll(response.body().getData().getAreas());


                                updateUi();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<DriverFilterDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateUi() {
        int areaPos = getAreaPos();
        if (areaPos != -1) {
            SingleAreaModel model = cityModelList.get(areaPos);
            model.setSelected(true);
            cityModelList.set(areaPos, model);
            cityAdapter.setCurrentPos(areaPos);
        }
        if (!driverFilterModel.getCar_type_id().isEmpty()){
            int carTypePos = getCarTypePos(Integer.parseInt(driverFilterModel.getCar_type_id()));

            if (carTypePos != -1) {
                CarType model = carTypeList.get(carTypePos);
                model.setSelected(true);
                carTypeList.set(carTypePos, model);
            }

            carTypeAdapter.notifyDataSetChanged();
        }

        cityAdapter.notifyDataSetChanged();
    }

    private int getAreaPos() {
        int pos = -1;
        for (int index = 0; index < cityModelList.size(); index++) {
            if (String.valueOf(cityModelList.get(index).getId()).equals(driverFilterModel.getArea_id())) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private int getCarTypePos(int carTypeId) {
        int pos = -1;
        for (int index = 0; index < carTypeList.size(); index++) {
            if (carTypeList.get(index).getId() == carTypeId) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    public void setCarTypeItem(CarType model) {

        driverFilterModel.setCar_type_id(model.getId() + "");
    }

    public void setItemArea(SingleAreaModel model) {
        driverFilterModel.setArea_id(model.getId() + "");

    }
}