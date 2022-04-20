
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalets.ChaletsActivity;
import com.motaweron_apps.ektfaa.databinding.ChaletsRowBinding;
import com.motaweron_apps.ektfaa.model.PlaceModel;

import java.util.List;

public class ChaletsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlaceModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ChaletsAdapter(List<PlaceModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        ChaletsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chalets_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        ((MyHolder) holder).binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(v -> {
            ChaletsActivity chaletsActivity = (ChaletsActivity) context;
            chaletsActivity.setItemChalets(list.get(holder.getLayoutPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ChaletsRowBinding binding;

        public MyHolder(@androidx.annotation.NonNull ChaletsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
