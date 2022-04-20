
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chefs.ChefsActivity;
import com.motaweron_apps.ektfaa.databinding.ChefRowBinding;
import com.motaweron_apps.ektfaa.model.UserModel;

import java.util.List;

public class ChefAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;


    public ChefAdapter(List<UserModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        ChefRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chef_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.imageFave.setOnClickListener(view -> {
            if (appCompatActivity instanceof ChefsActivity){
                ChefsActivity activity = (ChefsActivity) appCompatActivity;
                activity.favorite(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
            }
        });
        myHolder.itemView.setOnClickListener(view -> {
            if (appCompatActivity instanceof ChefsActivity){
                ChefsActivity activity = (ChefsActivity) appCompatActivity;
                activity.setItem(list.get(myHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ChefRowBinding binding;

        public MyHolder(ChefRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
