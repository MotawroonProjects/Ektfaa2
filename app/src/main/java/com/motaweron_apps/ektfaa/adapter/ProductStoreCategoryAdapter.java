package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_products.FamilyProductsActivity;
import com.motaweron_apps.ektfaa.databinding.FamilyCategoryAddRowBinding;
import com.motaweron_apps.ektfaa.databinding.FamilyCategoryRowBinding;
import com.motaweron_apps.ektfaa.model.DepartmentModel;

import java.util.List;

public class ProductStoreCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ROW_ADD = 1;
    private final int ROW_DATA = 2;
    private List<DepartmentModel.BasicDepartmentFk> list;
    private Context context;
    private LayoutInflater inflater;
    private FamilyProductsActivity activity;
    private int old_pos = 1;
    private int currentSelectedPos = 1;

    public ProductStoreCategoryAdapter(List<DepartmentModel.BasicDepartmentFk> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (FamilyProductsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ROW_ADD){
            FamilyCategoryAddRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.family_category_add_row, parent, false);
            return new MyHolderAdd(binding);
        }else {
            FamilyCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.family_category_row, parent, false);
            return new MyHolderData(binding);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolderData){
            MyHolderData myHolderData = (MyHolderData) holder;
            myHolderData.binding.setModel(list.get(position));
            myHolderData.itemView.setOnClickListener(v -> {
                currentSelectedPos = myHolderData.getAdapterPosition();
                if (old_pos!=-1){
                    DepartmentModel.BasicDepartmentFk oldModel = list.get(old_pos);
                    oldModel.setSelected(false);
                    list.set(old_pos,oldModel);
                    notifyItemChanged(old_pos);
                }
                DepartmentModel.BasicDepartmentFk model = list.get(currentSelectedPos);
                model.setSelected(true);
                list.set(currentSelectedPos,model);
                activity.setItemCategory(model.getId());
                notifyItemChanged(currentSelectedPos);
                old_pos = currentSelectedPos;
            });
            myHolderData.binding.imageEdit.setOnClickListener(v -> {
                activity.setEditCategory(list.get(myHolderData.getAdapterPosition()));
            });
            myHolderData.binding.imageDelete.setOnClickListener(v -> {
                activity.deleteCategory(list.get(myHolderData.getAdapterPosition()).getId());
            });

        }else if (holder instanceof MyHolderAdd){
            MyHolderAdd myHolderAdd = (MyHolderAdd) holder;
            myHolderAdd.itemView.setOnClickListener(v -> {
                activity.displayDialog(null);
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolderData extends RecyclerView.ViewHolder {
        public FamilyCategoryRowBinding binding;

        public MyHolderData(@NonNull FamilyCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public static class MyHolderAdd extends RecyclerView.ViewHolder {
        public FamilyCategoryAddRowBinding binding;

        public MyHolderAdd(@NonNull FamilyCategoryAddRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        if (list.size() > 0) {
            if (position == 0) {
                return ROW_ADD;
            } else {
                return ROW_DATA;
            }
        } else {
            return ROW_ADD;

        }

    }
}
