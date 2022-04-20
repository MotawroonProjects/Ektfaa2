
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_delegets.DelegetsActivity;
import com.motaweron_apps.ektfaa.databinding.DelegetRowBinding;
import com.motaweron_apps.ektfaa.model.UserModel;

import java.util.List;

public class DelegetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    public DelegetsAdapter(List<UserModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;

    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        DelegetRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.deleget_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        if (list.get(position).isSelected()){
            myHolder.binding.flOrder.setVisibility(View.VISIBLE);

        }else {
            myHolder.binding.flOrder.setVisibility(View.GONE);

        }

        myHolder.itemView.setOnClickListener(view -> {
            UserModel.Data model = list.get(myHolder.getAdapterPosition());
            if (model.isSelected()){
                model.setSelected(false);
                myHolder.binding.flOrder.setVisibility(View.GONE);


            }else {
                model.setSelected(true);
                myHolder.binding.flOrder.setVisibility(View.VISIBLE);

            }
            list.set(myHolder.getAdapterPosition(),model);
        });

        myHolder.binding.llSend.setOnClickListener(view -> {
            UserModel.Data model = list.get(myHolder.getAdapterPosition());
           if (appCompatActivity instanceof DelegetsActivity){
               DelegetsActivity activity = (DelegetsActivity) appCompatActivity;
               activity.sendOrder(model);
           }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public DelegetRowBinding binding;

        public MyHolder(@androidx.annotation.NonNull DelegetRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
