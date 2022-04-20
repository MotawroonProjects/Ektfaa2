
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
import com.motaweron_apps.ektfaa.model.SingleAreaModel;

import java.util.List;

import io.paperdb.Paper;

public class SpinnerAreaAdapter extends BaseAdapter {
    private List<SingleAreaModel> data;
    private Context context;
    private String lang;

    public SpinnerAreaAdapter(List<SingleAreaModel> data, Context context) {
        this.data = data;
        this.context = context;
        lang = Paper.book().read("lang","ar");

    }

    @Override
    public int getCount() {
        if (data.size() == 0)
            return 0;
        else
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerAreaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_area,viewGroup,false);

       binding.setData(data.get(i).getTitle());

        return binding.getRoot();
    }
}
