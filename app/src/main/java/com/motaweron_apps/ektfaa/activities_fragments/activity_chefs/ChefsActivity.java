package com.motaweron_apps.ektfaa.activities_fragments.activity_chefs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_detials.FamilyDetialsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_sub_category_area.SubCategoryAreaActivity;
import com.motaweron_apps.ektfaa.adapter.ChefAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityChefsBinding;
import com.motaweron_apps.ektfaa.model.FilterSubDeptAreaModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.model.UsersDataModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChefsActivity extends BaseActivity {
    private ActivityChefsBinding binding;
    private String category_id = "";
    private List<UserModel.Data> list;
    private ChefAdapter adapter;
    private List<String> subDepartmentList;
    private Call<UsersDataModel> call;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private String area_id;
    private FilterSubDeptAreaModel filterSubDeptAreaModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chefs);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        category_id = intent.getStringExtra("data");

    }

    private void initView() {
        area_id= getArea()!=null?getArea().getId()+"":"0";
        setUpToolbar(binding.toolbar,getString(R.string.productive_families), R.color.white, R.color.black);

        subDepartmentList = new ArrayList<>();
        list = new ArrayList<>();
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChefAdapter(list, this);
        binding.recView.setAdapter(adapter);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = null;
                if (!editable.toString().isEmpty()){
                    query = editable.toString();

                }
                search(query);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req==1&&result.getResultCode()==RESULT_OK&&result.getData()!=null){
                filterSubDeptAreaModel = (FilterSubDeptAreaModel) result.getData().getSerializableExtra("data");
                subDepartmentList = filterSubDeptAreaModel.getSubDepartmentIds();
                Log.e("sss",subDepartmentList.size()+"__");
                if (!filterSubDeptAreaModel.getArea_id().isEmpty()){
                    area_id = filterSubDeptAreaModel.getArea_id();
                }
                String query = null;
                if (!binding.edtSearch.getText().toString().isEmpty()){
                    query = binding.edtSearch.getText().toString();
                }
                search(query);

            }
        });

        binding.cardFilter.setOnClickListener(view -> {
           req =1;
           Intent intent = new Intent(this,SubCategoryAreaActivity.class);
           intent.putExtra("data",category_id);
           if (filterSubDeptAreaModel==null){
               filterSubDeptAreaModel = new FilterSubDeptAreaModel();
               String area_id= getArea()!=null?getArea().getId()+"":"0";

               filterSubDeptAreaModel.setArea_id(area_id);
           }
           intent.putExtra("data2",filterSubDeptAreaModel);
           launcher.launch(intent);

        });
        search(null);
    }

    private void search(String query) {
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();

        if (call != null) {
            call.cancel();
        }
        call = Api.getService(Tags.base_url).searchFamily(area_id, category_id, getUserSettings().getUser_lat() + "", getUserSettings().getUser_lng() + "", query, subDepartmentList);
        call.enqueue(new Callback<UsersDataModel>() {
            @Override
            public void onResponse(Call<UsersDataModel> call, Response<UsersDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                        if (list.size()>0){
                            binding.tvNoData.setVisibility(View.GONE);
                        }else {
                            binding.tvNoData.setVisibility(View.VISIBLE);

                        }

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


    public void favorite(UserModel.Data data, int adapterPosition) {

    }

    public void setItem(UserModel.Data data) {
        Intent intent = new Intent(this,FamilyDetialsActivity.class);
        intent.putExtra("data",data.getId());
        startActivity(intent);

    }
}