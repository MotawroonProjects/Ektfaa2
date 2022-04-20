
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_current_order.DriverCurrentOrderActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_previous_order.DriverPreviousOrderActivity;
import com.motaweron_apps.ektfaa.databinding.ComingOrderRowBinding;
import com.motaweron_apps.ektfaa.databinding.CurrentOrderRowBinding;
import com.motaweron_apps.ektfaa.databinding.OrderRowBinding;
import com.motaweron_apps.ektfaa.model.OrderModel;

import java.util.List;

public class DriverCurrentOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;


    public DriverCurrentOrderAdapter(List<OrderModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CurrentOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.current_order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.binding.tvDetails.setPaintFlags(myHolder.binding.tvDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        myHolder.binding.tvDetails.setOnClickListener(v -> {
            if (appCompatActivity instanceof DriverCurrentOrderActivity) {
                DriverCurrentOrderActivity activity = (DriverCurrentOrderActivity) appCompatActivity;
                activity.navigateToOrderDetails(list.get(myHolder.getAdapterPosition()));
            } else if (appCompatActivity instanceof DriverPreviousOrderActivity) {
                DriverPreviousOrderActivity activity = (DriverPreviousOrderActivity) appCompatActivity;
                activity.navigateToOrderDetails(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.call.setOnClickListener(v -> {
            if (appCompatActivity instanceof DriverCurrentOrderActivity) {
                DriverCurrentOrderActivity activity = (DriverCurrentOrderActivity) appCompatActivity;
                activity.call(list.get(myHolder.getAdapterPosition()));
            } else if (appCompatActivity instanceof DriverPreviousOrderActivity) {
                DriverPreviousOrderActivity activity = (DriverPreviousOrderActivity) appCompatActivity;
                activity.call(list.get(myHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CurrentOrderRowBinding binding;

        public MyHolder(@NonNull CurrentOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
