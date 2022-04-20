package com.motaweron_apps.ektfaa.activities_fragments.activity_product_details;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.AttrDetailsAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityFamilyDetialsBinding;
import com.motaweron_apps.ektfaa.databinding.ActivityProductDetailsBinding;
import com.motaweron_apps.ektfaa.model.ProductModel;
import com.motaweron_apps.ektfaa.model.SingleProductDataModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends BaseActivity {
    private ActivityProductDetailsBinding binding;
    private String product_id;
    private ProductModel productModel;
    private AttrDetailsAdapter adapter;
    private ImagePopup imagePopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        product_id = intent.getStringExtra("data");
    }

    private void initView() {
        imagePopup = new ImagePopup(this);
        imagePopup.setFullScreen(true);
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);
        binding.setLang(getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.cardBack.setOnClickListener(view -> finish());
        getProductData();
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimage(productModel.getDefault_image().getImage());
            }
        });
    }


    private void getProductData() {
        binding.llData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        String user_id = null;
        if (getUserModel() != null) {
            user_id = getUserModel().getData().getId();
        }

        Api.getService(Tags.base_url).getProductById(product_id, user_id).enqueue(new Callback<SingleProductDataModel>() {
            @Override
            public void onResponse(Call<SingleProductDataModel> call, Response<SingleProductDataModel> response) {
                binding.progBar.setVisibility(View.GONE);


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
            public void onFailure(Call<SingleProductDataModel> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());

                    }

                } catch (Exception e) {
                }

            }
        });


    }

    private void updateUi(ProductModel body) {

        productModel = body;
        binding.setModel(productModel);
        binding.llData.setVisibility(View.VISIBLE);
        adapter = new AttrDetailsAdapter(this, body.getFood_details());
        binding.recView.setAdapter(adapter);


    }

    public void showimage(String image) {
        imagePopup.initiatePopupWithPicasso(image);
        imagePopup.viewPopup();

    }

}