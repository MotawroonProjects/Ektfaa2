package com.motaweron_apps.ektfaa.activities_fragments.activity_chalets;

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
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_filter.ChaletFilterActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalets_detials.ChaletsDetialsActivity;
import com.motaweron_apps.ektfaa.adapter.ChaletsAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityChaletsBinding;
import com.motaweron_apps.ektfaa.model.ChaletFilterModel;
import com.motaweron_apps.ektfaa.model.PlaceModel;
import com.motaweron_apps.ektfaa.model.PlacesDataModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaletsActivity extends BaseActivity {
    private ActivityChaletsBinding binding;
    private ChaletsAdapter adapter;
    private List<PlaceModel> list;
    private String query = null;
    private ChaletFilterModel chaletFilterModel;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private Call<PlacesDataModel> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chalets);
        binding.setLifecycleOwner(this);

        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.chalets), R.color.white, R.color.black);
        list = new ArrayList<>();
        int area_id = getArea()!=null?getArea().getId():0;
        chaletFilterModel = new ChaletFilterModel("id", "desc", area_id + "");
        adapter = new ChaletsAdapter(list, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.progBar.setVisibility(View.GONE);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = null;
                if (!s.toString().isEmpty()) {
                    query = s.toString();
                }

                getChaletsData();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                chaletFilterModel = (ChaletFilterModel) result.getData().getSerializableExtra("data");

                getChaletsData();

            }
        });

        binding.cardFilter.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(this, ChaletFilterActivity.class);
            intent.putExtra("data", chaletFilterModel);
            launcher.launch(intent);

        });
        getChaletsData();
    }

    private void getChaletsData() {
        binding.tvNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        if (call != null) {
            call.cancel();
        }

        Log.e("data",chaletFilterModel.getArea_id()+"__"+query+"__"+getUserSettings().getUser_lat()+"__"+getUserSettings().getUser_lng()+"___"+chaletFilterModel.getOrder_type()+"__"+chaletFilterModel.getOrder_by());
        call = Api.getService(Tags.base_url).get_chalets(chaletFilterModel.getArea_id(), query, getUserSettings().getUser_lat(), getUserSettings().getUser_lng(), chaletFilterModel.getOrder_type(), chaletFilterModel.getOrder_by());

        call.enqueue(new Callback<PlacesDataModel>() {
            @Override
            public void onResponse(Call<PlacesDataModel> call, Response<PlacesDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        list.clear();
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();

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
                        Toast.makeText(ChaletsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(ChaletsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }


            }

            @Override
            public void onFailure(Call<PlacesDataModel> call, Throwable t) {
                binding.progBar.setVisibility(View.GONE);
                try {
                    binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ChaletsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChaletsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
        });


    }


    public void setItemChalets(PlaceModel model) {
        Intent intent = new Intent(ChaletsActivity.this, ChaletsDetialsActivity.class);
        intent.putExtra("data", model.getId());
        startActivity(intent);
    }
}