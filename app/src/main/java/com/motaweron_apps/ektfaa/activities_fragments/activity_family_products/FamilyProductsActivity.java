package com.motaweron_apps.ektfaa.activities_fragments.activity_family_products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_add_product.AddProductActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.ProductStoreCategoryAdapter;
import com.motaweron_apps.ektfaa.adapter.ProductsFamilyAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityFamilyProductsBinding;
import com.motaweron_apps.ektfaa.model.DepartmentDataModel;
import com.motaweron_apps.ektfaa.model.DepartmentModel;
import com.motaweron_apps.ektfaa.model.ProductDataModel;
import com.motaweron_apps.ektfaa.model.ProductModel;
import com.motaweron_apps.ektfaa.model.SingleDepartmentModel;
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

public class FamilyProductsActivity extends BaseActivity {
    private ActivityFamilyProductsBinding binding;
    private ProductStoreCategoryAdapter categoryAdapter;
    private List<DepartmentModel.BasicDepartmentFk> categoryList;
    private List<ProductModel> productsList;
    private ProductsFamilyAdapter productsFamilyAdapter;
    private DepartmentModel.BasicDepartmentFk selectedCategory;
    private String deptType;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family_products);
        binding.setLifecycleOwner(this);

        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.products), R.color.white, R.color.black);
        categoryList = new ArrayList<>();
        productsList = new ArrayList<>();

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new ProductStoreCategoryAdapter(categoryList, this);
        binding.recViewCategory.setAdapter(categoryAdapter);

        binding.recViewProducts.setLayoutManager(new GridLayoutManager(this, 3));
        productsFamilyAdapter = new ProductsFamilyAdapter(productsList, this);
        binding.recViewProducts.setAdapter(productsFamilyAdapter);


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req == 1 && result.getResultCode() == RESULT_OK) {
                    getCategory();
                }
            }
        });
        binding.btnAdd.setOnClickListener(v -> {
            String deptName = binding.edtDept.getText().toString();
            if (!deptName.isEmpty()) {
                binding.edtDept.setError(null);
                Common.CloseKeyBoard(this, binding.edtDept);
                binding.flDialog.setVisibility(View.GONE);
                if (deptType.equals("add")) {
                    addDepartment(deptName);

                } else {
                    editCategory(deptName);
                }

            } else {
                binding.edtDept.setError(getString(R.string.field_required));
            }
        });


        binding.cardClose.setOnClickListener(v -> {
            binding.flDialog.setVisibility(View.GONE);
        });

        binding.cardClose.setOnClickListener(v -> {
            binding.flDialog.setVisibility(View.GONE);
        });

        getCategory();
    }


    private void getCategory() {

        if (getUserModel() == null) {
            return;
        }
        binding.tvNoData.setVisibility(View.GONE);
        binding.llData.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        String user_id = null;


        user_id = getUserModel().getData().getId();

        Api.getService(Tags.base_url)
                .getFamilyCategory("Bearer " + getUserModel().getData().getToken(), user_id)
                .enqueue(new Callback<DepartmentDataModel>() {
                    @Override
                    public void onResponse(Call<DepartmentDataModel> call, Response<DepartmentDataModel> response) {
                        binding.llData.setVisibility(View.VISIBLE);
                        binding.shimmer.setVisibility(View.GONE);
                        binding.shimmer.stopShimmer();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                categoryList.clear();
                                categoryList.add(null);
                                if (response.body().getData().size() > 0) {
                                    categoryList.addAll(response.body().getData());
                                    binding.tvNoData.setVisibility(View.GONE);
                                    DepartmentModel.BasicDepartmentFk model = categoryList.get(1);
                                    model.setSelected(true);
                                    categoryList.set(1, model);
                                    getProductByCategoryId(model.getId());
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);
                                    productsList.clear();
                                    productsFamilyAdapter.notifyDataSetChanged();
                                }
                                categoryAdapter.notifyDataSetChanged();

                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<DepartmentDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getProductByCategoryId(String category_id) {
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        productsList.clear();
        productsFamilyAdapter.notifyDataSetChanged();

        if (getUserModel() == null) {
            return;
        }

        Api.getService(Tags.base_url)
                .getFamilyProductByCategoryId("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), category_id)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                productsList.add(null);

                                if (response.body().getData().size() > 0) {
                                    productsList.addAll(response.body().getData());
                                    binding.tvNoData.setVisibility(View.GONE);

                                }
                                productsFamilyAdapter.notifyDataSetChanged();


                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void addDepartment(String deptName) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addCategory("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), deptName)
                .enqueue(new Callback<SingleDepartmentModel>() {
                    @Override
                    public void onResponse(Call<SingleDepartmentModel> call, Response<SingleDepartmentModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                getCategory();
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
                    public void onFailure(Call<SingleDepartmentModel> call, Throwable t) {
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

    private void editCategory(String deptName) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .editCategory("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), selectedCategory.getId(), deptName)
                .enqueue(new Callback<SingleDepartmentModel>() {
                    @Override
                    public void onResponse(Call<SingleDepartmentModel> call, Response<SingleDepartmentModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                getCategory();
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
                    public void onFailure(Call<SingleDepartmentModel> call, Throwable t) {
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

    public void displayDialog(String name) {
        binding.edtDept.setText(name);
        binding.flDialog.setVisibility(View.VISIBLE);
        if (name == null) {
            deptType = "add";
            binding.btnAdd.setText(getString(R.string.add2));
            binding.dialogTitle.setText(getString(R.string.add_department));
        } else {
            deptType = "update";
            binding.btnAdd.setText(getString(R.string.update));
            binding.dialogTitle.setText(getString(R.string.update_department));


        }
    }

    public void navigateToStoreAddProduct(ProductModel productModel) {
        req = 1;
        Intent intent = new Intent(this, AddProductActivity.class);
        if (productModel != null) {
            intent.putExtra("data", productModel);
        }
        launcher.launch(intent);
    }


    public void setEditCategory(DepartmentModel.BasicDepartmentFk category) {
        this.selectedCategory = category;
        displayDialog(category.getTitle());
    }

    public void setItemCategory(String id) {
        getProductByCategoryId(id);
    }

    public void deleteCategory(String category_id) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteCategory("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), category_id)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                getCategory();
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


    public void deleteProduct(ProductModel productModel) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteFamilyProduct("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), productModel.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                getCategory();
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

    @Override
    public void onBackPressed() {
        if (binding.flDialog.getVisibility() == View.VISIBLE) {
            binding.flDialog.setVisibility(View.GONE);

        } else {
            super.onBackPressed();

        }
    }
}