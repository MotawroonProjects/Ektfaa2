package com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_my_ads;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_add_ads.ChaletAddAdsActivity;
import com.motaweron_apps.ektfaa.adapter.ChaletAdsAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityChaletMyAdBinding;
import com.motaweron_apps.ektfaa.model.PlaceModel;
import com.motaweron_apps.ektfaa.model.PlacesDataModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaletMyAdActivity extends BaseActivity {
    private ActivityChaletMyAdBinding binding;
    private ChaletAdsAdapter adapter;
    private List<PlaceModel> list;
    private Call<PlacesDataModel> call;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chalet_my_ad);
        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.chalets), R.color.white, R.color.black);
        list = new ArrayList<>();
        adapter = new ChaletAdsAdapter(list, this);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recView.setAdapter(adapter);
        getChaletsData();


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK) {
                getChaletsData();
            }
        });
        binding.fabAddAds.setOnClickListener(v -> {
            req =1;
            Intent intent = new Intent(this, ChaletAddAdsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });

        binding.swipeRefresh.setOnRefreshListener(this::getChaletsData);

    }

    private void getChaletsData() {
        binding.tvNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        binding.swipeRefresh.setRefreshing(true);

        if (call != null) {
            call.cancel();
        }

        call = Api.getService(Tags.base_url).get_MyChalets("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId());

        call.enqueue(new Callback<PlacesDataModel>() {
            @Override
            public void onResponse(Call<PlacesDataModel> call, Response<PlacesDataModel> response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        list.clear();
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                        binding.tvNoData.setVisibility(View.GONE);


                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    binding.swipeRefresh.setRefreshing(false);

                }


            }

            @Override
            public void onFailure(Call<PlacesDataModel> call, Throwable t) {
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        binding.swipeRefresh.setRefreshing(false);

                    }

                } catch (Exception e) {
                }

            }
        });


    }


    public void deleteAd(int adapterPosition) {
        PlaceModel model = list.get(adapterPosition);
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteMyChalet("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), model.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                list.remove(adapterPosition);
                                adapter.notifyItemRemoved(adapterPosition);
                                if (list.size() > 0) {
                                    binding.tvNoData.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
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
                                Log.e("error", t.getMessage() + "__");


                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void editAd(int adapterPosition) {
        req =1;
        PlaceModel model = list.get(adapterPosition);
        Intent intent = new Intent(this,ChaletAddAdsActivity.class);
        intent.putExtra("data",model);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        launcher.launch(intent);

    }
}