package com.motaweron_apps.ektfaa.activities_fragments.activity_send_order;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_map.MapActivity;
import com.motaweron_apps.ektfaa.adapter.SpinnerFamilyAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityPackagesBinding;
import com.motaweron_apps.ektfaa.databinding.ActivitySendOrderBinding;
import com.motaweron_apps.ektfaa.model.SelectedLocation;
import com.motaweron_apps.ektfaa.model.SendOrderModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.model.UsersDataModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOrderActivity extends BaseActivity {
    private ActivitySendOrderBinding binding;
    private SendOrderModel model;
    private int req;
    private ActivityResultLauncher<Intent> launcher;
    private List<UserModel.Data> familyList;
    private SpinnerFamilyAdapter spinnerFamilyAdapter;
    private String driver_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_order);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        driver_id = intent.getStringExtra("data");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.send_order), R.color.white, R.color.black);
        familyList = new ArrayList<>();

        model = new SendOrderModel();
        model.setUser_id(getUserModel().getData().getId());
        model.setDriver_id(driver_id);
        binding.setModel(model);
        Log.e("data",getUserModel().getData().getId()+"__"+driver_id);
        spinnerFamilyAdapter = new SpinnerFamilyAdapter(familyList, this);
        binding.spinner.setAdapter(spinnerFamilyAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setFamily_id(familyList.get(position).getId());
                model.setName(familyList.get(position).getName());
                binding.setModel(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                SelectedLocation selectedLocation = (SelectedLocation) result.getData().getSerializableExtra("location");
                model.setFrom_location(selectedLocation.getAddress());
                model.setFrom_latitude(selectedLocation.getLat() + "");
                model.setFrom_longitude(selectedLocation.getLng() + "");

            } else if (req == 2 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                SelectedLocation selectedLocation = (SelectedLocation) result.getData().getSerializableExtra("location");
                model.setTo_location(selectedLocation.getAddress());
                model.setTo_latitude(selectedLocation.getLat() + "");
                model.setTo_longitude(selectedLocation.getLng() + "");
            }

            binding.setModel(model);
        });

        binding.llFromMap.setOnClickListener(v -> {
            req = 1;
            Intent intent = new Intent(this, MapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });

        binding.llToMap.setOnClickListener(v -> {
            req = 2;
            Intent intent = new Intent(this, MapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });

        binding.rbFamily.setOnClickListener(v -> {

            if (!model.getType().equals("family")) {
                model.setType("family");
                if (familyList.size() > 0) {
                    binding.spinner.setSelection(0);

                }

                binding.expandLayout.collapse(true);
                binding.edtName.setVisibility(View.GONE);
                binding.setModel(model);
                new Handler().postDelayed(() -> {
                    binding.flSpinner.setVisibility(View.VISIBLE);
                    binding.expandLayout.expand(true);
                }, 500);
            }

        });

        binding.rbNormal.setOnClickListener(v -> {
            if (!model.getType().equals("normal")) {
                model.setType("normal");
                model.setName("");
                binding.setModel(model);
                binding.expandLayout.collapse(true);
                binding.flSpinner.setVisibility(View.GONE);

                new Handler().postDelayed(() -> {
                    binding.edtName.setVisibility(View.VISIBLE);
                    binding.expandLayout.expand(true);
                }, 500);
            }

        });

        binding.btnSend.setOnClickListener(v -> {
            if (model.isDataValid(this)){
                sendOrder();
            }
        });
        getFamilies();
    }

    private void getFamilies() {
        int area_id = getArea()!=null?getArea().getId():0;

        Api.getService(Tags.base_url).getAvailableFamily("Bearer " + getUserModel().getData().getToken(), area_id+ "")
                .enqueue(new Callback<UsersDataModel>() {
                    @Override
                    public void onResponse(Call<UsersDataModel> call, Response<UsersDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                familyList.clear();
                                familyList.addAll(response.body().getData());
                                runOnUiThread(() -> spinnerFamilyAdapter.notifyDataSetChanged());


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
                    public void onFailure(Call<UsersDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });

    }

    private void sendOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sendOrder("Bearer " + getUserModel().getData().getToken(), model)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("code",response.body().getStatus()+"");
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(SendOrderActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
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
}