package com.motaweron_apps.ektfaa.activities_fragments.activity_delegets;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_filter.DriverFilterActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_send_order.SendOrderActivity;
import com.motaweron_apps.ektfaa.adapter.DelegetsAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityDelegetsBinding;
import com.motaweron_apps.ektfaa.model.AllDriverModel;
import com.motaweron_apps.ektfaa.model.DriverFilterModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelegetsActivity extends BaseActivity {
    private ActivityDelegetsBinding binding;
    private DelegetsAdapter delegetsAdapter;
    private List<UserModel.Data> singleDriverModelList;
    private String query = null;
    private String from = "";
    private DriverFilterModel driverFilterModel;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delegets);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from");
        }
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.delegate), R.color.white, R.color.black);
        if (driverFilterModel == null) {
            driverFilterModel = new DriverFilterModel();

        }
        driverFilterModel.setArea_id(getArea().getId() + "");
        driverFilterModel.setOrder_by_type("id");

        singleDriverModelList = new ArrayList<>();
        delegetsAdapter = new DelegetsAdapter(singleDriverModelList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(delegetsAdapter);
        binding.progBar.setVisibility(View.GONE);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                driverFilterModel = (DriverFilterModel) result.getData().getSerializableExtra("data");
                getDriverData();
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = binding.edtSearch.getText().toString();

                getDriverData();
            }
        });

        binding.cardFilter.setOnClickListener(v -> {
            req = 1;
            Intent intent = new Intent(this, DriverFilterActivity.class);
            intent.putExtra("data", driverFilterModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });
        getDriverData();
    }

    private void getDriverData() {
        singleDriverModelList.clear();
        delegetsAdapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        String user_id = null;
        if (getUserModel() != null) {
            user_id = getUserModel().getData().getId();
        }
        Api.getService(Tags.base_url).get_drivers(user_id, Integer.parseInt(driverFilterModel.getArea_id()), query, getUserSettings().getUser_lat(), getUserSettings().getUser_lng(), driverFilterModel.getOrder_by_type(), driverFilterModel.getCar_type_id()).enqueue(new Callback<AllDriverModel>() {
            @Override
            public void onResponse(Call<AllDriverModel> call, Response<AllDriverModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        singleDriverModelList.clear();
                        singleDriverModelList.addAll(response.body().getData());
                        delegetsAdapter.notifyDataSetChanged();

                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.code() == 500) {
                        Toast.makeText(DelegetsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(DelegetsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<AllDriverModel> call, Throwable t) {
                binding.progBar.setVisibility(View.GONE);
                try {
                    binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(DelegetsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DelegetsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }


    public void sendOrder(UserModel.Data model) {
        if (getUserModel() != null) {
            if (from.equals("NewOrderActivity")) {
                Intent intent = getIntent();
                intent.putExtra("data", model);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Intent intent = new Intent(this, SendOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("data", model.getId());
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, R.string.si_su, Toast.LENGTH_SHORT).show();
        }

    }
}