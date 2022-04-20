
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_filter.DriverFilterActivity;
import com.motaweron_apps.ektfaa.databinding.CarTypeRowBinding;
import com.motaweron_apps.ektfaa.databinding.LocationRowBinding;
import com.motaweron_apps.ektfaa.model.CarType;

import java.util.List;

public class FilterCarTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CarType> list;
    private Context context;
    private LayoutInflater inflater;
    private int oldPos = -1;
    private int currentPos =-1;
    private AppCompatActivity appCompatActivity;
    public FilterCarTypeAdapter(List<CarType> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CarTypeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.car_type_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.rb.setOnClickListener(view -> {
            currentPos = myHolder.getAdapterPosition();
            if (oldPos!=-1){
                CarType oldModel = list.get(oldPos);
                oldModel.setSelected(false);
                list.set(oldPos,oldModel);
                notifyItemChanged(oldPos);
            }

            CarType currentModel = list.get(currentPos);
            currentModel.setSelected(true);
            list.set(currentPos,currentModel);
            notifyItemChanged(currentPos);
            oldPos = currentPos;

            if (appCompatActivity instanceof DriverFilterActivity){
                DriverFilterActivity activity = (DriverFilterActivity) appCompatActivity;
                activity.setCarTypeItem(currentModel);
            }


        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CarTypeRowBinding binding;

        public MyHolder(@NonNull CarTypeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setCurrentPos(int currentPos){
        this.currentPos = currentPos;
        oldPos = currentPos;
    }

}
