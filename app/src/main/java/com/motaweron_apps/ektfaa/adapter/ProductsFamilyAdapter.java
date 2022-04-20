package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_products.FamilyProductsActivity;
import com.motaweron_apps.ektfaa.databinding.FamilyAddProductRowBinding;
import com.motaweron_apps.ektfaa.databinding.StoreProductRowBinding;
import com.motaweron_apps.ektfaa.model.ProductModel;

import java.util.List;

public class ProductsFamilyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ROW_ADD = 1;
    private final int ROW_DATA = 2;
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private FamilyProductsActivity activity;

    public ProductsFamilyAdapter(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (FamilyProductsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ROW_ADD){
            FamilyAddProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.family_add_product_row, parent, false);
            return new MyHolderAdd(binding);
        }else {
            StoreProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.store_product_row, parent, false);
            return new MyHolderData(binding);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolderData){
            MyHolderData myHolderData = (MyHolderData) holder;
            myHolderData.binding.setModel(list.get(position));


            myHolderData.binding.cardEdit.setOnClickListener(v -> {
                activity.navigateToStoreAddProduct(list.get(myHolderData.getAdapterPosition()));

            });

            myHolderData.binding.cardDelete.setOnClickListener(v -> {
                activity.deleteProduct(list.get(myHolderData.getAdapterPosition()));

            });
        }else if (holder instanceof MyHolderAdd){
            MyHolderAdd myHolderAdd = (MyHolderAdd) holder;
            myHolderAdd.itemView.setOnClickListener(v -> {
                activity.navigateToStoreAddProduct(null);
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolderData extends RecyclerView.ViewHolder {
        public StoreProductRowBinding binding;

        public MyHolderData(@NonNull StoreProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public static class MyHolderAdd extends RecyclerView.ViewHolder {
        public FamilyAddProductRowBinding binding;

        public MyHolderAdd(@NonNull FamilyAddProductRowBinding binding) {
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
