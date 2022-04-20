package com.motaweron_apps.ektfaa.activities_fragments.activity_add_product;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.ProductGalleryAdapter;
import com.motaweron_apps.ektfaa.adapter.SpinnerAreaAdapter;
import com.motaweron_apps.ektfaa.adapter.SpinnerDepartmentAdapter;
import com.motaweron_apps.ektfaa.adapter.SpinnerOfferTypeAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityAddProductBinding;
import com.motaweron_apps.ektfaa.databinding.PropertyFieldBinding;
import com.motaweron_apps.ektfaa.model.AddImageProductModel;
import com.motaweron_apps.ektfaa.model.AddProductModel;
import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.DepartmentDataModel;
import com.motaweron_apps.ektfaa.model.DepartmentModel;
import com.motaweron_apps.ektfaa.model.OfferTypeModel;
import com.motaweron_apps.ektfaa.model.ProductModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.model.SingleProductModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {
    private ActivityAddProductBinding binding;
    private AddProductModel model;
    private List<AddImageProductModel> images;
    private ActivityResultLauncher<Intent> launcher;
    private List<PropertyFieldBinding> propertyList;
    private ProductGalleryAdapter adapter;
    private int req;
    private SpinnerOfferTypeAdapter spinnerOfferTypeAdapter;
    private List<OfferTypeModel> offerTypeModelList;
    private List<SingleAreaModel> areaList;
    private SpinnerAreaAdapter spinnerAreaAdapter;
    private SpinnerDepartmentAdapter spinnerDepartmentAdapter;
    private List<DepartmentModel.BasicDepartmentFk> categoryList;
    private ProductModel productModel;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            productModel = (ProductModel) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        title = getString(R.string.add_product);
        setUpToolbar(binding.toolbar, title, R.color.white, R.color.black);

        areaList = new ArrayList<>();
        categoryList = new ArrayList<>();
        offerTypeModelList = new ArrayList<>();
        images = new ArrayList<>();
        propertyList = new ArrayList<>();
        model = new AddProductModel(this);
        binding.setModel(model);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProductGalleryAdapter(images, this);
        binding.recView.setAdapter(adapter);

        offerTypeModelList.add(new OfferTypeModel(getString(R.string.percent), "per"));
        offerTypeModelList.add(new OfferTypeModel(getString(R.string.value), "val"));

        spinnerOfferTypeAdapter = new SpinnerOfferTypeAdapter(offerTypeModelList, this);
        binding.spinnerOfferType.setAdapter(spinnerOfferTypeAdapter);


        spinnerDepartmentAdapter = new SpinnerDepartmentAdapter(categoryList, this);
        binding.spinnerDept.setAdapter(spinnerDepartmentAdapter);
        binding.spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setDepartment_id(categoryList.get(position).getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAreaAdapter = new SpinnerAreaAdapter(areaList, this);
        binding.spinnerArea.setAdapter(spinnerAreaAdapter);
        binding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setArea_id(areaList.get(position).getId() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {

                    Uri uri = getImageUri(result.getData());
                    if (images.size() < 5) {
                        AddImageProductModel addImageProductModel = new AddImageProductModel(null, uri.toString(), "local");
                        images.add(0, addImageProductModel);
                        model.setImages(images);
                        adapter.notifyItemInserted(0);
                        binding.recView.post(() -> {
                            binding.recView.smoothScrollToPosition(0);
                        });
                    }


                }
            }

        });
        binding.flAddImage.setOnClickListener(v -> {
            if (images.size() < 5) {
                checkReadPermission();
            }
        });
        binding.btnAddProperty.setOnClickListener(v -> {
            String property = binding.edtProperty.getText().toString();
            if (!property.isEmpty()) {
                binding.edtProperty.setError(null);
                Common.CloseKeyBoard(this, binding.edtProperty);
                binding.flDialog.setVisibility(View.GONE);
                addProperty(property);
            } else {
                binding.edtProperty.setError(getString(R.string.field_required));
            }
        });
        binding.flDisplayDialog.setOnClickListener(v -> {
            binding.edtProperty.setText(null);
            binding.flDialog.setVisibility(View.VISIBLE);
        });
        binding.cardClose.setOnClickListener(v -> {
            binding.flDialog.setVisibility(View.GONE);
        });
        binding.flAdd.setOnClickListener(v -> {
            if (model.isDataValid(this)) {
                Common.CloseKeyBoard(this, binding.edtName);
                Common.CloseKeyBoard(this, binding.edtName);
                if (productModel == null) {
                    addProduct();

                } else {
                    updateProduct(getLocalImage());
                }

            }
        });

        binding.spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setOffer_type(offerTypeModelList.get(position).getValue());
                binding.setModel(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.checkbox.setOnClickListener(v -> {
            if (binding.checkbox.isChecked()) {
                model.setHave_offer(true);
                binding.expandLayout.expand(true);
            } else {
                model.setHave_offer(false);
                binding.expandLayout.collapse(true);

            }
            binding.setModel(model);

        });

        if (productModel == null) {
            getCategory();
            getAreas();
        } else {
            getProductById();
        }


    }

    private void getProductById() {
        binding.scrollView.setVisibility(View.GONE);
        binding.flAdd.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getProductDataById(productModel.getId())
                .enqueue(new Callback<SingleProductModel>() {
                    @Override
                    public void onResponse(Call<SingleProductModel> call, Response<SingleProductModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (productModel != null) {
                                    binding.flDisplayDialog.setVisibility(View.GONE);

                                    productModel = response.body().getData();
                                    title = productModel.getTitle();
                                    setUpToolbar(binding.toolbar, title, R.color.white, R.color.black);
                                    model.setName(productModel.getTitle());
                                    model.setArea_id(productModel.getArea_id());
                                    model.setPrice(productModel.getPrice());
                                    model.setPrice_after_discount(productModel.getOffer_price());
                                    if(productModel.getDetails()!=null){
                                    model.setDescriptions(productModel.getDetails());}
                                    if (productModel.getHave_offer().equals("yes")) {
                                        model.setHave_offer(true);
                                        binding.expandLayout.expand(true);
                                        binding.checkbox.setChecked(true);
                                    } else {
                                        model.setHave_offer(false);
                                        binding.expandLayout.collapse(false);
                                        binding.checkbox.setChecked(false);

                                    }

                                    if (productModel.getOffer_type().equals("per")) {
                                        model.setOffer_type("per");
                                        binding.spinnerOfferType.setSelection(0);
                                    } else {
                                        model.setOffer_type("value");
                                        binding.spinnerOfferType.setSelection(1);

                                    }

                                    model.setOffer_value(productModel.getOffer_value());

                                    binding.tvBtn.setText(R.string.update);
                                    for (UserModel.ImagesData productImage : productModel.getImages()) {
                                        AddImageProductModel addImageProductModel = new AddImageProductModel(productImage.getId(), productImage.getImage(), "online");
                                        images.add(addImageProductModel);
                                    }

                                    model.setImages(images);
                                } else {
                                    setUpToolbar(binding.toolbar, title, R.color.white, R.color.black);
                                    productModel = response.body().getData();

                                }
                                binding.scrollView.setVisibility(View.VISIBLE);
                                binding.flAdd.setVisibility(View.VISIBLE);
                                getAreas();
                                getCategory();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<SingleProductModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });


    }

    private void addProduct() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String user_id = getUserModel().getData().getId();
        String user_token = getUserModel().getData().getToken();

        RequestBody user_id_part = Common.getRequestBodyText(user_id);
        RequestBody name_part = Common.getRequestBodyText(model.getName());
        RequestBody sub_dept_part = Common.getRequestBodyText(model.getDepartment_id());
        RequestBody details_part = Common.getRequestBodyText(model.getDescriptions());
        RequestBody area_id_part = Common.getRequestBodyText(model.getArea_id());
        RequestBody have_offer_part = Common.getRequestBodyText(model.isHave_offer() ? "yes" : "no");
        RequestBody offer_value_part = Common.getRequestBodyText(model.getOffer_value());
        RequestBody offer_type_part = Common.getRequestBodyText(model.getOffer_type());
        RequestBody price_part = Common.getRequestBodyText(model.getPrice());


        Api.getService(Tags.base_url)
                .addProduct("Bearer " + user_token, user_id_part, name_part, sub_dept_part, details_part, area_id_part, price_part, have_offer_part, offer_type_part, offer_value_part, getPropertyParts(), getImages())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(AddProductActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();

                            }


                        } else {
                            try {
                                Log.e("failed", response.code() + "___" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateProduct(List<String> localImage) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String user_id = getUserModel().getData().getId();
        String user_token = getUserModel().getData().getToken();


        RequestBody user_id_part = Common.getRequestBodyText(user_id);
        RequestBody product_id_part = Common.getRequestBodyText(productModel.getId());

        RequestBody name_part = Common.getRequestBodyText(model.getName());
        RequestBody dept_part = Common.getRequestBodyText(model.getDepartment_id());
        RequestBody details_part = Common.getRequestBodyText(model.getDescriptions());
        RequestBody area_id_part = Common.getRequestBodyText(model.getArea_id());
        RequestBody price_part = Common.getRequestBodyText(model.getPrice());


        RequestBody have_offer_part = Common.getRequestBodyText(model.isHave_offer() ? "yes" : "no");
        RequestBody offer_value_part = Common.getRequestBodyText(model.getOffer_value());
        RequestBody offer_type_part = Common.getRequestBodyText(model.getOffer_type());


        Api.getService(Tags.base_url)
                .updateProduct("Bearer " + user_token, product_id_part, user_id_part, name_part, dept_part, details_part, area_id_part, price_part, have_offer_part, offer_type_part, offer_value_part, getImagesLocal(localImage))
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(AddProductActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();

                            }


                        } else {
                            try {
                                Log.e("failed", response.code() + "___" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private int getSpinnerAreaPos() {
        int pos = -1;
        for (int index = 0; index < areaList.size(); index++) {
            if (String.valueOf(areaList.get(index).getId()).equals(productModel.getArea_id())) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private int getSpinnerDepartmentPos() {
        int pos = -1;
        for (int index = 0; index < categoryList.size(); index++) {
            if (categoryList.get(index).getId().equals(productModel.getDepartment_id())) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private List<String> getLocalImage() {
        List<String> imgs = new ArrayList<>();
        for (AddImageProductModel model : images) {
            if (model.getType().equals("local")) {
                imgs.add(model.getImage());
            }
        }
        return imgs;
    }

    private List<MultipartBody.Part> getImages() {
        List<MultipartBody.Part> images = new ArrayList<>();
        for (AddImageProductModel addImageProductModel : model.getImages()) {
            if (addImageProductModel.getType().equals("local")) {
                MultipartBody.Part img = Common.getMultiPart(this, Uri.parse(addImageProductModel.getImage()), "images[]");
                images.add(img);
            }

        }

        return images;
    }

    private List<MultipartBody.Part> getImagesLocal(List<String> imgs) {
        List<MultipartBody.Part> images = new ArrayList<>();
        for (String img : imgs) {
            MultipartBody.Part img1 = Common.getMultiPart(this, Uri.parse(img), "images[]");
            images.add(img1);

        }

        return images;
    }

    private List<MultipartBody.Part> getPropertyParts() {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int index = 0; index < model.getProperty().size(); index++) {
            PropertyFieldBinding binding = model.getProperty().get(index);
            String title = binding.getTitle();
            String value = binding.edtPropertyTitle.getText().toString();

            String partName1 = "food_details[" + index + "][title]";
            String partName2 = "food_details[" + index + "][value]";

            MultipartBody.Part part1 = Common.getMultiPartText(title, partName1);
            MultipartBody.Part part2 = Common.getMultiPartText(value, partName2);

            parts.add(part1);
            parts.add(part2);

        }
        return parts;
    }


    private void getAreas() {
        Api.getService(Tags.base_url)
                .getArea()
                .enqueue(new Callback<AllAreaModel>() {
                    @Override
                    public void onResponse(Call<AllAreaModel> call, Response<AllAreaModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                areaList.addAll(response.body().getData());
                                if (areaList.size() > 0) {
                                    model.setArea_id(areaList.get(0).getId() + "");
                                }
                                runOnUiThread(() -> {
                                    spinnerAreaAdapter.notifyDataSetChanged();

                                    if (productModel != null) {
                                        int pos = getSpinnerAreaPos();
                                        if (pos != -1) {
                                            new Handler().postDelayed(() -> binding.spinnerArea.setSelection(pos), 1000);

                                        }
                                    }


                                });


                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<AllAreaModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getCategory() {

        if (getUserModel() == null) {
            return;
        }
        String user_id = null;

        user_id = getUserModel().getData().getId();

        Api.getService(Tags.base_url)
                .getFamilyCategory("Bearer " + getUserModel().getData().getToken(), user_id)
                .enqueue(new Callback<DepartmentDataModel>() {
                    @Override
                    public void onResponse(Call<DepartmentDataModel> call, Response<DepartmentDataModel> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                categoryList.clear();
                                categoryList.addAll(response.body().getData());
                                if (categoryList.size() > 0) {
                                    model.setDepartment_id(categoryList.get(0).getId());
                                }
                                runOnUiThread(() -> {
                                    spinnerDepartmentAdapter.notifyDataSetChanged();
                                    if (productModel != null) {
                                        int pos = getSpinnerDepartmentPos();
                                        if (pos != -1) {
                                            new Handler().postDelayed(() -> binding.spinnerDept.setSelection(pos), 1000);

                                        }
                                    }


                                });


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

    private void addProperty(String property) {


        PropertyFieldBinding propertyFieldBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.property_field, null, false);
        propertyFieldBinding.setTitle(property);
        propertyFieldBinding.cardDelete.setOnClickListener(v -> {
            binding.llProperties.removeView(propertyFieldBinding.getRoot());
            model.setProperty(propertyList);
            binding.setModel(model);
        });
        propertyList.add(propertyFieldBinding);
        binding.llProperties.addView(propertyFieldBinding.getRoot());
        model.setProperty(propertyList);
        binding.setModel(model);


    }

    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this, BaseActivity.READ_REQ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, BaseActivity.WRITE_REQ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, BaseActivity.CAM_REQ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{BaseActivity.READ_REQ, BaseActivity.WRITE_REQ, BaseActivity.CAM_REQ}, 100);
        }
    }

    private void openGallery() {
        req = 1;
        List<Intent> allIntents = new ArrayList<>();
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cameraResolveInfo = getPackageManager().queryIntentActivities(cameraIntent, 0);

        for (ResolveInfo info : cameraResolveInfo) {
            Intent intent = new Intent(cameraIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);
            allIntents.add(intent);
        }


        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo info : resolveInfo) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            intent.setPackage(info.activityInfo.packageName);

            allIntents.add(intent);
        }


        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }

        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Choose image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        launcher.launch(chooserIntent);


    }

    private Uri getCameraUri() {
        Uri outPutUri = null;
        File file = new File(Environment.getExternalStorageDirectory(), "JustAskApp");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (req == 1) {
            outPutUri = Uri.fromFile(new File(file, "profile.png"));

        } else if (req == 2) {
            outPutUri = Uri.fromFile(new File(file, "banner.png"));

        }

        return outPutUri;
    }

    private Uri getImageUri(Intent intent) {
        boolean isCamera = true;
        if (intent != null) {
            String action = intent.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera ? getCameraUri() : intent.getData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length >= 3) {
            openGallery();
        } else {
            Toast.makeText(this, "Access image denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteImage(int adapterPosition) {
        if (images.get(adapterPosition).getId() != null) {
            if (images.size()>1){
                deleteOnlineImage(images.get(adapterPosition).getId(), adapterPosition);

            }else {
                Toast.makeText(this, R.string.at_least_photo, Toast.LENGTH_SHORT).show();
            }
        } else {
            images.remove(adapterPosition);
            adapter.notifyItemRemoved(adapterPosition);
            model.setImages(images);
        }


    }

    private void deleteOnlineImage(String id, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String token = getUserModel().getData().getToken();
        Api.getService(Tags.base_url)
                .deleteProductImage("Bearer " + token, id)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                images.remove(adapterPosition);
                                adapter.notifyItemRemoved(adapterPosition);
                                model.setImages(images);

                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

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