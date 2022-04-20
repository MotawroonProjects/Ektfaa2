package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.databinding.SliderRowBinding;
import com.motaweron_apps.ektfaa.model.Slider_Model;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<Slider_Model.Data> list ;
    private Context context;
    private LayoutInflater inflater;

    public SliderAdapter(List<Slider_Model.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SliderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.slider_row,container,false);
        binding.setSliderData(list.get(position).getImage());
      container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
