package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.databinding.AttrDetailsRowBinding;
import com.motaweron_apps.ektfaa.model.ProductModel;

import java.util.List;

public class AttrDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel.FoodDetails> list;
    private Context context;
    private LayoutInflater inflater;
    public AttrDetailsAdapter(Context context, List<ProductModel.FoodDetails> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AttrDetailsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.attr_details_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        if (position%2==0){
            myHolder.binding.llData.setBackgroundResource(0);
        }else {
            myHolder.binding.llData.setBackgroundResource(R.drawable.rounded_color15);

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private AttrDetailsRowBinding binding;

        public MyHolder(AttrDetailsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
