package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_sub_category_area.SubCategoryAreaActivity;
import com.motaweron_apps.ektfaa.databinding.SubdeptCheckRowBinding;
import com.motaweron_apps.ektfaa.model.DepartmentModel;


import java.util.List;

public class SubDeptCheckedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DepartmentModel.BasicDepartmentFk> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public SubDeptCheckedAdapter(List<DepartmentModel.BasicDepartmentFk> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SubdeptCheckRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.subdept_check_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        DepartmentModel.BasicDepartmentFk model = list.get(position);
        myHolder.binding.setModel(model);

        myHolder.itemView.setOnClickListener(v -> {
            DepartmentModel.BasicDepartmentFk model2 = list.get(myHolder.getAdapterPosition());
            if (model2.isSelected()){
                model2.setSelected(false);
            }else {
                model2.setSelected(true);
            }
            list.set(myHolder.getAdapterPosition(),model2);

            if (activity instanceof SubCategoryAreaActivity){
                SubCategoryAreaActivity subCategoryAreaActivity = (SubCategoryAreaActivity) activity;
                subCategoryAreaActivity.addRemoveItemDepartment(model2);
            }
            notifyItemChanged(myHolder.getAdapterPosition());



        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public SubdeptCheckRowBinding binding;

        public MyHolder(@NonNull SubdeptCheckRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
