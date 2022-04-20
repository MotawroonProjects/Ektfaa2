
package com.motaweron_apps.ektfaa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.databinding.SpinnerAreaBinding;
import com.motaweron_apps.ektfaa.databinding.SpinnerStatusBinding;
import com.motaweron_apps.ektfaa.model.StatusModel;

import java.util.List;

public class SpinnerStatusAdapter extends BaseAdapter {
    private List<StatusModel> list;
    private Context context;

    public SpinnerStatusAdapter(List<StatusModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerStatusBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_status, viewGroup, false);
        binding.setTitle(list.get(i).getStatus());
        return binding.getRoot();
    }
}
