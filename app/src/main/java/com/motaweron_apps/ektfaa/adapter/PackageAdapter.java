package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_packages.PackagesActivity;
import com.motaweron_apps.ektfaa.databinding.PackageRowBinding;
import com.motaweron_apps.ektfaa.model.PackageModel;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PackageModel> list;
    private Context context;
    private LayoutInflater inflater;
    private PackagesActivity activity;
    private int old_pos = -1;
    private int currentSelectedPos = -1;

    public PackageAdapter(List<PackageModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (PackagesActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        PackageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.package_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(v -> {
            currentSelectedPos = myHolder.getAdapterPosition();
            if (old_pos!=-1){
                PackageModel oldModel = list.get(old_pos);
                oldModel.setSelected(false);
                list.set(old_pos,oldModel);
                notifyItemChanged(old_pos);
            }
            PackageModel model = list.get(currentSelectedPos);
            model.setSelected(true);
            list.set(currentSelectedPos,model);
            notifyItemChanged(currentSelectedPos);
            old_pos = currentSelectedPos;
            activity.setItemPackage(model);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public PackageRowBinding binding;

        public MyHolder(@NonNull PackageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
