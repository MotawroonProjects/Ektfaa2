
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_detials.FamilyDetialsActivity;
import com.motaweron_apps.ektfaa.databinding.FamilyProductRowBinding;
import com.motaweron_apps.ektfaa.model.ProductModel;

import java.util.List;

public class FamilyProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;

    public FamilyProductAdapter(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;

    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        FamilyProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.family_product_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        ((MyHolder) holder).binding.setModel(list.get(position));
        if (list.get(position).getHave_offer().equals("yes")){
            myHolder.binding.tvOldPrice.setPaintFlags(myHolder.binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            myHolder.binding.tvCurrency.setPaintFlags(myHolder.binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        }else {
            myHolder.binding.tvOldPrice.setPaintFlags(0);
            myHolder.binding.tvCurrency.setPaintFlags(0);


        }
        myHolder.itemView.setOnClickListener(v -> {
            if (appCompatActivity instanceof FamilyDetialsActivity){
                FamilyDetialsActivity activity = (FamilyDetialsActivity) appCompatActivity;
                activity.setItemProduct(list.get(myHolder.getAdapterPosition()));
            }
        });
        myHolder.binding.image.setOnClickListener(v -> {
            if (appCompatActivity instanceof FamilyDetialsActivity){
                FamilyDetialsActivity activity = (FamilyDetialsActivity) appCompatActivity;
                activity.showimage(list.get(myHolder.getAdapterPosition()).getDefault_image().getImage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public FamilyProductRowBinding binding;

        public MyHolder(@androidx.annotation.NonNull FamilyProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
