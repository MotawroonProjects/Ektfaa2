package com.motaweron_apps.ektfaa.activities_fragments.activity_productive_families;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chefs.ChefsActivity;
import com.motaweron_apps.ektfaa.adapter.ProductiveFamiliesCategoryAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityProductiveFamiliesBinding;
import com.motaweron_apps.ektfaa.model.AllCategoryModel;
import com.motaweron_apps.ektfaa.model.DepartmentModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductiveFamiliesActivity extends BaseActivity {
    private ActivityProductiveFamiliesBinding binding;
    private List<DepartmentModel.BasicDepartmentFk> categoryFamilyModelList;
    private ProductiveFamiliesCategoryAdapter productiveFamiliesCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_productive_families);
        binding.setLifecycleOwner(this);

        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.productive_families), R.color.white, R.color.black);

        categoryFamilyModelList = new ArrayList<>();
        productiveFamiliesCategoryAdapter = new ProductiveFamiliesCategoryAdapter(categoryFamilyModelList, this);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recView.setAdapter(productiveFamiliesCategoryAdapter);

        getCategories();
    }

    private void getCategories() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getCategories().enqueue(new Callback<AllCategoryModel>() {
            @Override
            public void onResponse(Call<AllCategoryModel> call, Response<AllCategoryModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    categoryFamilyModelList.clear();
                    categoryFamilyModelList.addAll(response.body().getData());
                    if (categoryFamilyModelList.size() > 0) {
                        productiveFamiliesCategoryAdapter.notifyDataSetChanged();
                        binding.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.progBar.setVisibility(View.GONE);

                    switch (response.code()) {
                        case 500:
                            Toast.makeText(ProductiveFamiliesActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ProductiveFamiliesActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<AllCategoryModel> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);

                    Log.e("msg", t.getMessage() + "__");
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ProductiveFamiliesActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(ProductiveFamiliesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public void setItemData(DepartmentModel.BasicDepartmentFk singleCategoryFamilyModel) {
        Intent intent = new Intent(this, ChefsActivity.class);
        intent.putExtra("data", singleCategoryFamilyModel.getId() + "");
        startActivity(intent);
    }
}