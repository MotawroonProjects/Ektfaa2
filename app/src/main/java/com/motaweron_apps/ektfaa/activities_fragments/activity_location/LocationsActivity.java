package com.motaweron_apps.ektfaa.activities_fragments.activity_location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.LocationAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityLocationsBinding;
import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsActivity extends BaseActivity {
    private ActivityLocationsBinding binding;
    private List<SingleAreaModel> singleAreaModelList;
    private LocationAdapter locationAdapter;
    private SingleAreaModel selectedArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_locations);
        binding.setLifecycleOwner(this);

        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar,getString(R.string.ch_area), R.color.white, R.color.black);
        singleAreaModelList = new ArrayList<>();
        locationAdapter = new LocationAdapter(singleAreaModelList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(locationAdapter);
        binding.progBar.setVisibility(View.GONE);

        binding.btnConfirm.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.putExtra("data",selectedArea);
            setResult(RESULT_OK,intent);
            finish();
        });
        getCityData();
    }


    private void getCityData() {
        binding.tvNoData.setVisibility(View.GONE);

        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getArea().enqueue(new Callback<AllAreaModel>() {
            @Override
            public void onResponse(Call<AllAreaModel> call, Response<AllAreaModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200) {
                    if (response.body().getData().size() > 0) {
                        singleAreaModelList.clear();
                        singleAreaModelList.addAll(response.body().getData());
                        if (getArea()!=null){
                            int pos = getCityPos();
                            if (pos!=-1){
                                SingleAreaModel model = singleAreaModelList.get(pos);
                                model.setSelected(true);
                                singleAreaModelList.set(pos,model);
                                locationAdapter.setCurrentPos(pos);
                                binding.btnConfirm.setVisibility(View.VISIBLE);

                            }
                        }
                        locationAdapter.notifyDataSetChanged();

                    }
                    else {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(LocationsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(LocationsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllAreaModel> call, Throwable t) {
                binding.progBar.setVisibility(View.GONE);
                try {
                    binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(LocationsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LocationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private int getCityPos(){
        int pos = -1;
        for (int index=0;index<singleAreaModelList.size();index++){
            int area_id = getArea()!=null?getArea().getId():0;
            if (singleAreaModelList.get(index).getId()==area_id){
                pos = index;
                return pos;
            }
        }
        return pos;
    }
    @Override
    public void onBackPressed() {
        finish();
    }


    public void setItemArea(SingleAreaModel currentModel) {
        selectedArea = currentModel;
        binding.btnConfirm.setVisibility(View.VISIBLE);
    }
}