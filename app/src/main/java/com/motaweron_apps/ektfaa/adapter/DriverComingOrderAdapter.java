
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_coming_order.DriverComingOrderActivity;
import com.motaweron_apps.ektfaa.databinding.ChefRowBinding;
import com.motaweron_apps.ektfaa.databinding.ComingOrderRowBinding;
import com.motaweron_apps.ektfaa.model.OrderModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class DriverComingOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;


    public DriverComingOrderAdapter(List<OrderModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        ComingOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.coming_order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        OrderModel model = list.get(position);
        myHolder.binding.setModel(list.get(position));


        myHolder.binding.btnNext.setOnClickListener(v -> {
            if (appCompatActivity instanceof DriverComingOrderActivity) {
                DriverComingOrderActivity activity = (DriverComingOrderActivity) appCompatActivity;
                activity.setItemOrder(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.btnRefuse.setOnClickListener(v -> {
            if (appCompatActivity instanceof DriverComingOrderActivity) {
                DriverComingOrderActivity activity = (DriverComingOrderActivity) appCompatActivity;
                activity.refuseOrder(list.get(myHolder.getAdapterPosition()));
            }
        });

    }

    private String getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        Double dist = SphericalUtil.computeDistanceBetween(new LatLng(lat1, lng1), new LatLng(lat2, lng2));
        return (dist / 1000) + "";

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ComingOrderRowBinding binding;

        public MyHolder(ComingOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
