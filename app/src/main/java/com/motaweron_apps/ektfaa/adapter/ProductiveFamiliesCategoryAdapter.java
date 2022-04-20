
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_productive_families.ProductiveFamiliesActivity;
import com.motaweron_apps.ektfaa.databinding.CategoryImageRowBinding;
import com.motaweron_apps.ektfaa.model.DepartmentModel;

import java.util.List;

import io.paperdb.Paper;

public class ProductiveFamiliesCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DepartmentModel.BasicDepartmentFk> singleCategoryFamilyModels;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    private ProductiveFamiliesActivity activity;

    public ProductiveFamiliesCategoryAdapter(List<DepartmentModel.BasicDepartmentFk> singleCategoryFamilyModels, Context context) {
        this.singleCategoryFamilyModels = singleCategoryFamilyModels;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ProductiveFamiliesActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        CategoryImageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_image_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setModel(singleCategoryFamilyModels.get(position));
        eventHolder.itemView.setOnClickListener(view -> {
            activity.setItemData(singleCategoryFamilyModels.get(eventHolder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return singleCategoryFamilyModels.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CategoryImageRowBinding binding;

        public EventHolder(@androidx.annotation.NonNull CategoryImageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
