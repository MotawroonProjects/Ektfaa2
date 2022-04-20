package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_add_product.AddProductActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_add_ads.ChaletAddAdsActivity;
import com.motaweron_apps.ektfaa.databinding.ProductGalleryImageRowBinding;
import com.motaweron_apps.ektfaa.model.AddImageProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AddImageProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public ProductGalleryAdapter(List<AddImageProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ProductGalleryImageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_gallery_image_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        Picasso.get().load(Uri.parse(list.get(position).getImage())).into(myHolder.binding.image);

        myHolder.binding.cardDelete.setOnClickListener(v -> {
            if (activity instanceof AddProductActivity){
                AddProductActivity addProductActivity = (AddProductActivity) activity;
                addProductActivity.deleteImage(myHolder.getAdapterPosition());

            }else if (activity instanceof ChaletAddAdsActivity){
                ChaletAddAdsActivity chaletAddAdsActivity = (ChaletAddAdsActivity) activity;
                chaletAddAdsActivity.deleteImage(myHolder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ProductGalleryImageRowBinding binding;

        public MyHolder(@NonNull ProductGalleryImageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
