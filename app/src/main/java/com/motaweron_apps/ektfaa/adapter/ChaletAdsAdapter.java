
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_my_ads.ChaletMyAdActivity;
import com.motaweron_apps.ektfaa.databinding.ChaletAdRowBinding;
import com.motaweron_apps.ektfaa.model.PlaceModel;

import java.util.List;

public class ChaletAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlaceModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ChaletMyAdActivity activity;

    public ChaletAdsAdapter(List<PlaceModel> list, Context context) {
        this.list = list;
        this.context = context;
        activity = (ChaletMyAdActivity) context;
        inflater = LayoutInflater.from(context);
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        ChaletAdRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chalet_ad_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));

        myHolder.binding.cardDelete.setOnClickListener(v -> {
            activity.deleteAd(myHolder.getAdapterPosition());

        });

        myHolder.binding.cardEdit.setOnClickListener(v -> {
            activity.editAd(myHolder.getAdapterPosition());

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ChaletAdRowBinding binding;

        public MyHolder(@androidx.annotation.NonNull ChaletAdRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
