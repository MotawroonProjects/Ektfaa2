package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.fragments.CurrentOrderFragment;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.fragments.PreviousOrderFragment;
import com.motaweron_apps.ektfaa.databinding.OrderRowBinding;
import com.motaweron_apps.ektfaa.model.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public OrderAdapter(List<OrderModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.binding.tvDetails.setPaintFlags(myHolder.binding.tvDetails.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        myHolder.binding.tvDetails.setOnClickListener(v -> {
            if (fragment instanceof CurrentOrderFragment){
                CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) fragment;
                currentOrderFragment.setItemData(list.get(myHolder.getAdapterPosition()));
            }else if (fragment instanceof PreviousOrderFragment){
                PreviousOrderFragment previousOrderFragment = (PreviousOrderFragment) fragment;
                previousOrderFragment.setItemData(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.call.setOnClickListener(v -> {
            if (fragment instanceof CurrentOrderFragment){
                CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) fragment;
                currentOrderFragment.call(list.get(myHolder.getAdapterPosition()));
            }else if (fragment instanceof PreviousOrderFragment){
                PreviousOrderFragment previousOrderFragment = (PreviousOrderFragment) fragment;
                previousOrderFragment.call(list.get(myHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public MyHolder(@NonNull OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
