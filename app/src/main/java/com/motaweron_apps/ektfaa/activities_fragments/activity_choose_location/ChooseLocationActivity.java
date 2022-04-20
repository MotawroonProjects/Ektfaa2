package com.motaweron_apps.ektfaa.activities_fragments.activity_choose_location;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.HomeActivity;
import com.motaweron_apps.ektfaa.adapter.SpinnerAreaAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityChooseLocationBinding;
import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseLocationActivity extends BaseActivity {
    private ActivityChooseLocationBinding binding;
    private List<SingleAreaModel> singleAreaModelList;
    private SpinnerAreaAdapter spinnerAreaAdapter;
    private SingleAreaModel singleareamodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_location);
        binding.setLifecycleOwner(this);

        initView();
    }

    private void initView() {

        singleAreaModelList = new ArrayList<>();
        spinnerAreaAdapter = new SpinnerAreaAdapter(singleAreaModelList, this);
        binding.spcity.setAdapter(spinnerAreaAdapter);
        binding.spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    singleareamodel = singleAreaModelList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnNext.setOnClickListener(view -> {
            if (singleareamodel != null) {
                setArea(singleareamodel);
                NavigateToHomeActivity();
            } else {
                Toast.makeText(this, getResources().getString(R.string.choose_location), Toast.LENGTH_LONG).show();
            }
        });
        getCityData();
    }

    private void getCityData() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url).getArea().enqueue(new Callback<AllAreaModel>() {
            @Override
            public void onResponse(Call<AllAreaModel> call, Response<AllAreaModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        singleAreaModelList.clear();
                        singleAreaModelList.add(new SingleAreaModel(getResources().getString(R.string.choose_location)));
                        singleAreaModelList.addAll(response.body().getData());
                        ChooseLocationActivity.this.runOnUiThread(() -> {
                            spinnerAreaAdapter.notifyDataSetChanged();
                        });
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(ChooseLocationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(ChooseLocationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllAreaModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    dialog.dismiss();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ChooseLocationActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChooseLocationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private void NavigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}