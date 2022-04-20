
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_client_new_order.ClientNewOrderActivity;
import com.motaweron_apps.ektfaa.databinding.ComingOrderRowBinding;
import com.motaweron_apps.ektfaa.databinding.NewOrderRowBinding;
import com.motaweron_apps.ektfaa.model.OrderModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class ClientNewOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;


    public ClientNewOrderAdapter(List<OrderModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        NewOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.new_order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        OrderModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.btnAnotherDriver.setOnClickListener(v -> {
            if (appCompatActivity instanceof ClientNewOrderActivity) {
                ClientNewOrderActivity activity = (ClientNewOrderActivity) appCompatActivity;
                activity.setItemOrder(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.btnRefuse.setOnClickListener(v -> {
            if (appCompatActivity instanceof ClientNewOrderActivity) {
                ClientNewOrderActivity activity = (ClientNewOrderActivity) appCompatActivity;
                String status = list.get(holder.getAdapterPosition()).getStatus();
                if (status.equals("select_other_driver")) {
                    activity.cancelOrder(list.get(myHolder.getAdapterPosition()));

                } else if (status.equals("driver_refuse_order")) {
                    activity.cancelOrder(list.get(myHolder.getAdapterPosition()));

                } else {
                    activity.refuseOrder(list.get(myHolder.getAdapterPosition()));

                }

            }
        });

        myHolder.binding.btnOffer.setOnClickListener(v -> {
            if (appCompatActivity instanceof ClientNewOrderActivity) {
                ClientNewOrderActivity activity = (ClientNewOrderActivity) appCompatActivity;
                activity.setOfferDetails(list.get(myHolder.getAdapterPosition()));
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
        public NewOrderRowBinding binding;

        public MyHolder(NewOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
