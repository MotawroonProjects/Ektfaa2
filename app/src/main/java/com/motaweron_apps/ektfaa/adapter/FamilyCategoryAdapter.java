
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_detials.FamilyDetialsActivity;
import com.motaweron_apps.ektfaa.databinding.CategoryRowBinding;
import com.motaweron_apps.ektfaa.model.DepartmentModel;

import java.util.List;

public class FamilyCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DepartmentModel.BasicDepartmentFk> list;
    private Context context;
    private LayoutInflater inflater;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private AppCompatActivity appCompatActivity;

    public FamilyCategoryAdapter(List<DepartmentModel.BasicDepartmentFk> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        ((MyHolder) holder).binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            currentPos = myHolder.getAdapterPosition();
            if (oldPos!=-1){
                DepartmentModel.BasicDepartmentFk oldModel = list.get(oldPos);
                oldModel.setSelected(false);
                list.set(oldPos,oldModel);
                notifyItemChanged(oldPos);
            }

            DepartmentModel.BasicDepartmentFk currentModel = list.get(currentPos);
            currentModel.setSelected(true);
            list.set(currentPos,currentModel);
            notifyItemChanged(currentPos);
            oldPos = currentPos;

            if (appCompatActivity instanceof FamilyDetialsActivity){
                FamilyDetialsActivity activity = (FamilyDetialsActivity) appCompatActivity;
                activity.setItemCategory(list.get(myHolder.getAdapterPosition()));
            }

        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryRowBinding binding;

        public MyHolder(@NonNull CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
