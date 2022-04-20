package com.motaweron_apps.ektfaa.activities_fragments.activity_packages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.PackageAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityPackagesBinding;
import com.motaweron_apps.ektfaa.model.PackageDataModel;
import com.motaweron_apps.ektfaa.model.PackageModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagesActivity extends BaseActivity {
    private ActivityPackagesBinding binding;
    private List<PackageModel> list;
    private PackageAdapter adapter;
    private PackageModel selectedPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packages);
        binding.setLifecycleOwner(this);


        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.packages_and_subscriptions), R.color.white, R.color.black);
        list = new ArrayList<>();
        adapter = new PackageAdapter(list, this);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recView.setAdapter(adapter);

        getPackages();

    }

    private String getDays(String expDate) {
        String days = "0";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            if (expDate!=null){
                Date expiredDate = format.parse(expDate);
                Date nowDate = calendar.getTime();
                Log.e("date", expiredDate.toString() + "__" + expiredDate.getTime() + "___" + nowDate.toString() + "" + nowDate.getTime());
                if (expiredDate.getTime() > nowDate.getTime()) {
                    long daysMill = 1000 * 60 * 60 * 24;
                    long diff = expiredDate.getTime() - nowDate.getTime();
                    long d = diff / daysMill;
                    days = String.valueOf(d);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }


    private void getPackages() {
        binding.progBar.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();
        Call<PackageDataModel> call = null;
        String endPoint = "";
        Map<String, String> maps = new HashMap<>();

        if (getUserModel().getData().getUser_type().equals(BaseActivity.FAMILY)) {
            endPoint = "family-packages";
            maps.put("family_id", getUserModel().getData().getId());
        } else if (getUserModel().getData().getUser_type().equals(BaseActivity.DRIVER)) {
            endPoint = "driver-packages";
            maps.put("driver_id", getUserModel().getData().getId());

        } else if (getUserModel().getData().getUser_type().equals(BaseActivity.CHALETS)) {
            endPoint = "chalet-packages";
            maps.put("chalet_id", getUserModel().getData().getId());

        }
        call = Api.getService(Tags.base_url)
                .getPackages(endPoint, maps);

        call.enqueue(new Callback<PackageDataModel>() {
            @Override
            public void onResponse(Call<PackageDataModel> call, Response<PackageDataModel> response) {

                binding.progBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Log.e("code", response.body().getStatus() + "__");

                    if (response.body() != null && response.body().getStatus() == 200 && response.body().getData() != null) {
                        String days = getDays(response.body().getData().getPackage_expired_date());
                        if (days.equals("0")) {
                            binding.cardFree.setVisibility(View.GONE);
                        } else {
                            binding.cardFree.setVisibility(View.VISIBLE);

                        }
                        binding.setDays(days);
                        list.addAll(response.body().getData().getPackages());
                        adapter.notifyDataSetChanged();
                    }


                } else {
                    try {
                        Log.e("error", response.code() + "__" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<PackageDataModel> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");


                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage() + "__");
                }
            }
        });
    }


    public void setItemPackage(PackageModel model) {
        selectedPackage = model;
        binding.btnSubscribe.setVisibility(View.VISIBLE);
    }
}