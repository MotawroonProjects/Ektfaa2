
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chat.ChatActivity;
import com.motaweron_apps.ektfaa.databinding.CancelReasonRowBinding;
import com.motaweron_apps.ektfaa.databinding.ChefRowBinding;
import com.motaweron_apps.ektfaa.model.ReasonModel;

import java.util.List;

public class ReasonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReasonModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    private String lang;

    public ReasonsAdapter(List<ReasonModel> list, Context context,String lang) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
        this.lang = lang;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        CancelReasonRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cancel_reason_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        if (position==list.size()-1){
            myHolder.binding.view.setVisibility(View.GONE);
        }else {
            myHolder.binding.view.setVisibility(View.VISIBLE);

        }
        myHolder.itemView.setOnClickListener(v -> {
            if (appCompatActivity instanceof ChatActivity){
                ChatActivity activity = (ChatActivity) appCompatActivity;
                activity.setItemReason(list.get(myHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CancelReasonRowBinding binding;

        public MyHolder(CancelReasonRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
