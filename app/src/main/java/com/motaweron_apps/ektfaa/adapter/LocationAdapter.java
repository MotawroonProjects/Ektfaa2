
package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_filter.ChaletFilterActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_filter.DriverFilterActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_location.LocationsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_sub_category_area.SubCategoryAreaActivity;
import com.motaweron_apps.ektfaa.databinding.LocationRowBinding;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleAreaModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int oldPos = -1;
    private int currentPos =-1;
    private AppCompatActivity appCompatActivity;
    public LocationAdapter(List<SingleAreaModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LocationRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.location_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setAreamodel(list.get(position));
        myHolder.binding.rb.setOnClickListener(view -> {
            currentPos = myHolder.getAdapterPosition();
            if (oldPos!=-1){
                SingleAreaModel oldModel = list.get(oldPos);
                oldModel.setSelected(false);
                list.set(oldPos,oldModel);
                notifyItemChanged(oldPos);
            }

            SingleAreaModel currentModel = list.get(currentPos);
            currentModel.setSelected(true);
            list.set(currentPos,currentModel);
            notifyItemChanged(currentPos);
            oldPos = currentPos;

            if (appCompatActivity instanceof LocationsActivity){
                LocationsActivity activity = (LocationsActivity) appCompatActivity;
                activity.setItemArea(currentModel);
            }else if (appCompatActivity instanceof SubCategoryAreaActivity){
                SubCategoryAreaActivity activity = (SubCategoryAreaActivity) appCompatActivity;
                activity.setItemArea(currentModel);
            }else if (appCompatActivity instanceof ChaletFilterActivity){
                ChaletFilterActivity activity = (ChaletFilterActivity) appCompatActivity;
                activity.setItemArea(currentModel);
            }else if (appCompatActivity instanceof DriverFilterActivity){
                DriverFilterActivity activity = (DriverFilterActivity) appCompatActivity;
                activity.setItemArea(currentModel);
            }




        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public LocationRowBinding binding;

        public MyHolder(@androidx.annotation.NonNull LocationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setCurrentPos(int currentPos){
        this.currentPos = currentPos;
        oldPos = currentPos;
    }

}
