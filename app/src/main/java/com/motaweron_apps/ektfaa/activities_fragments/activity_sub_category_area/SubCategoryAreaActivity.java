package com.motaweron_apps.ektfaa.activities_fragments.activity_sub_category_area;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.adapter.LocationAdapter;
import com.motaweron_apps.ektfaa.adapter.SubDeptCheckedAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivitySubCategoryAreaBinding;
import com.motaweron_apps.ektfaa.model.DepartmentModel;
import com.motaweron_apps.ektfaa.model.FilterDataModel;
import com.motaweron_apps.ektfaa.model.FilterSubDeptAreaModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryAreaActivity extends BaseActivity {
    private ActivitySubCategoryAreaBinding binding;
    private List<DepartmentModel.BasicDepartmentFk> subDepartmentList;
    private SubDeptCheckedAdapter subDeptCheckedAdapter;
    private List<SingleAreaModel> cityModelList;
    private LocationAdapter cityAdapter;
    private FilterSubDeptAreaModel filterSubDeptAreaModel;
    private List<String> subDeptIds;
    private String department_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_category_area);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        department_id = intent.getStringExtra("data");
        filterSubDeptAreaModel = (FilterSubDeptAreaModel) intent.getSerializableExtra("data2");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.filter), R.color.white, R.color.black);
        subDeptIds = new ArrayList<>();
        if (filterSubDeptAreaModel==null){
            filterSubDeptAreaModel = new FilterSubDeptAreaModel();

        }else {
            if (filterSubDeptAreaModel.getSubDepartmentIds()!=null){
                subDeptIds.addAll(filterSubDeptAreaModel.getSubDepartmentIds());

            }
        }
        subDepartmentList = new ArrayList<>();
        cityModelList = new ArrayList<>();
        binding.recViewDepartment.setLayoutManager(new LinearLayoutManager(this));
        subDeptCheckedAdapter = new SubDeptCheckedAdapter(subDepartmentList, this);
        binding.recViewDepartment.setAdapter(subDeptCheckedAdapter);

        binding.recViewGovernorate.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new LocationAdapter(cityModelList, this);
        binding.recViewGovernorate.setAdapter(cityAdapter);



        binding.btnConfirm.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.putExtra("data", filterSubDeptAreaModel);
            setResult(RESULT_OK, intent);
            finish();
        });

        binding.llDept.setOnClickListener(view -> {
            binding.arrow1.clearAnimation();
            if (binding.expandLayoutDepartment.isExpanded()) {
                binding.expandLayoutDepartment.collapse(true);
                binding.arrow1.animate().rotation(0).setDuration(300).start();
            } else {
                binding.expandLayoutDepartment.setExpanded(true);
                binding.arrow1.animate().rotation(180).setDuration(300).start();


            }
        });

        binding.llArea.setOnClickListener(view -> {
            binding.arrow2.clearAnimation();
            if (binding.expandLayoutGovernorate.isExpanded()) {
                binding.expandLayoutGovernorate.collapse(true);
                binding.arrow2.animate().rotation(0).setDuration(300).start();
            } else {
                binding.expandLayoutGovernorate.setExpanded(true);
                binding.arrow2.animate().rotation(180).setDuration(300).start();


            }
        });


        getSubDepartments();

    }

    private void getSubDepartments() {
        subDepartmentList.clear();
        subDeptCheckedAdapter.notifyDataSetChanged();
        cityModelList.clear();
        cityAdapter.notifyDataSetChanged();

        Api.getService(Tags.base_url)
                .getSubDepartment(department_id)
                .enqueue(new Callback<FilterDataModel>() {
                    @Override
                    public void onResponse(Call<FilterDataModel> call, Response<FilterDataModel> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                subDepartmentList.addAll(response.body().getData().getSub_departments());

                                cityModelList.addAll(response.body().getData().getAreas());


                                updateUi();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<FilterDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateUi() {
        int areaPos = getAreaPos();
        if (areaPos!=-1){
            SingleAreaModel model = cityModelList.get(areaPos);
            model.setSelected(true);
            cityModelList.set(areaPos,model);
            cityAdapter.setCurrentPos(areaPos);
        }
        for (String subDeptId:filterSubDeptAreaModel.getSubDepartmentIds()){
            int subDeptPos = getSubCategoryPos(subDeptId);
            if (subDeptPos!=-1){
                DepartmentModel.BasicDepartmentFk model = subDepartmentList.get(subDeptPos);
                model.setSelected(true);
                subDepartmentList.set(subDeptPos,model);
            }
        }

        subDeptCheckedAdapter.notifyDataSetChanged();
        cityAdapter.notifyDataSetChanged();
    }

    private int getAreaPos(){
        int pos =-1;
        for (int index=0;index<cityModelList.size();index++){
            if (String.valueOf(cityModelList.get(index).getId()).equals(filterSubDeptAreaModel.getArea_id())){
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private int getSubCategoryPos(String subDeptId){
        int pos =-1;
        for (int index=0;index<subDepartmentList.size();index++){
            if (subDepartmentList.get(index).getId().equals(subDeptId)){
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    public void addRemoveItemDepartment(DepartmentModel.BasicDepartmentFk model) {
        if (subDeptIds.contains(model.getId())) {
            subDeptIds.remove(model.getId());
        } else {
            subDeptIds.add(model.getId());
        }
        filterSubDeptAreaModel.setSubDepartmentIds(subDeptIds);

    }

    public void setItemArea(SingleAreaModel model) {
        filterSubDeptAreaModel.setArea_id(model.getId() + "");
        //binding.btnClear.setVisibility(View.VISIBLE);

    }
}