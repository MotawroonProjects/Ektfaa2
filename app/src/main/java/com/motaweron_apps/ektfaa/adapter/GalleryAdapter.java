package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_add_product.AddProductActivity;
import com.motaweron_apps.ektfaa.databinding.GalleryImageRowBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private AddProductActivity activity;
    public GalleryAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AddProductActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        GalleryImageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.gallery_image_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        Picasso.get().load(Uri.parse(list.get(position))).into(myHolder.binding.image);
        myHolder.binding.cardDelete.setOnClickListener(v -> {
            activity.deleteImage(myHolder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public GalleryImageRowBinding binding;

        public MyHolder(@NonNull GalleryImageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
