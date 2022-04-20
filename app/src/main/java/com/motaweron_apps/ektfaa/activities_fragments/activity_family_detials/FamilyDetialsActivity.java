package com.motaweron_apps.ektfaa.activities_fragments.activity_family_detials;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_product_details.ProductDetailsActivity;
import com.motaweron_apps.ektfaa.adapter.FamilyCategoryAdapter;
import com.motaweron_apps.ektfaa.adapter.FamilyProductAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityFamilyDetialsBinding;
import com.motaweron_apps.ektfaa.model.DepartmentModel;
import com.motaweron_apps.ektfaa.model.ProductModel;
import com.motaweron_apps.ektfaa.model.SingleFamilyModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyDetialsActivity extends BaseActivity {
    private ActivityFamilyDetialsBinding binding;
    private List<DepartmentModel.BasicDepartmentFk> categoryFamilyModelList;
    private FamilyCategoryAdapter familyCategoryAdapter;
    private List<ProductModel> productModelList;
    private FamilyProductAdapter familyProductAdapter;
    private String family_id;
    private UserModel.Data familyModel;
    private ImagePopup imagePopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_family_detials);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        family_id = intent.getStringExtra("data");
    }

    private void initView() {
        imagePopup = new ImagePopup(this);
        imagePopup.setFullScreen(true);
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);
        categoryFamilyModelList = new ArrayList<>();
        productModelList = new ArrayList<>();
        binding.setLang(getLang());
        familyCategoryAdapter = new FamilyCategoryAdapter(categoryFamilyModelList, this);
        familyProductAdapter = new FamilyProductAdapter(productModelList, this);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewCategory.setAdapter(familyCategoryAdapter);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(familyProductAdapter);
        getFamilyData();

        binding.llBack.setOnClickListener(view -> finish());
        binding.cardCall.setOnClickListener(view -> {
            String phone = "+" + familyModel.getPhone_code() + familyModel.getPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        });
        binding.cardWhatsApp.setOnClickListener(view -> {
            String phone = "+" + familyModel.getPhone_code() + familyModel.getPhone();

            String url = "https://api.whatsapp.com/send?phone=" + phone;
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        binding.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimage(familyModel.getLogo());
            }
        });
    }


    private void getFamilyData() {
        binding.llData.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();


        Api.getService(Tags.base_url).get_oneFamily(family_id).enqueue(new Callback<SingleFamilyModel>() {
            @Override
            public void onResponse(Call<SingleFamilyModel> call, Response<SingleFamilyModel> response) {
                binding.shimmer.setVisibility(View.GONE);
                binding.shimmer.stopShimmer();


                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        updateUi(response.body().getData());

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
            public void onFailure(Call<SingleFamilyModel> call, Throwable t) {
                try {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.shimmer.stopShimmer();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());

                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private void updateUi(UserModel.Data body) {

        familyModel = body;
        binding.setModel(familyModel);
        binding.llData.setVisibility(View.VISIBLE);

        categoryFamilyModelList.clear();
        if (body.getUser_department_data().size() > 0) {

            categoryFamilyModelList.addAll(body.getUser_department_data());
            DepartmentModel.BasicDepartmentFk basicDepartmentFk = categoryFamilyModelList.get(0);
            basicDepartmentFk.setSelected(true);
            categoryFamilyModelList.set(0, basicDepartmentFk);
            familyCategoryAdapter.notifyDataSetChanged();

            if (basicDepartmentFk.getDepartment_foods_data().size() > 0) {
                productModelList.clear();
                productModelList.addAll(basicDepartmentFk.getDepartment_foods_data());
                familyProductAdapter.notifyDataSetChanged();
                binding.tvNoData.setVisibility(View.GONE);

            } else {
                binding.tvNoData.setVisibility(View.VISIBLE);

            }

        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }


    }


    public void setItemCategory(DepartmentModel.BasicDepartmentFk basicDepartmentFk) {
        productModelList.clear();
        if (basicDepartmentFk.getDepartment_foods_data().size() > 0) {
            productModelList.addAll(basicDepartmentFk.getDepartment_foods_data());
            familyProductAdapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);

        } else {

            familyProductAdapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.VISIBLE);

        }

    }

    public void setItemProduct(ProductModel model) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("data", model.getId());
        startActivity(intent);
    }

    public void showimage(String image) {
        imagePopup.initiatePopupWithPicasso(image);
        imagePopup.viewPopup();

    }
}