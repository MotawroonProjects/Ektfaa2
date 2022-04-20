package com.motaweron_apps.ektfaa.activities_fragments.activity_intro_slider;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseFragment;
import com.motaweron_apps.ektfaa.databinding.FragmentIntroBinding;
import com.motaweron_apps.ektfaa.model.IntroModel;

public class FragmentIntroSlider extends BaseFragment {
    private FragmentIntroBinding binding;
    private IntroModel introModel;


    public static FragmentIntroSlider newInstance(IntroModel introModel) {
        FragmentIntroSlider fragment = new FragmentIntroSlider();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",introModel);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro, container, false);
        binding.setLifecycleOwner(this);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        if (getArguments()!=null){
            introModel = (IntroModel) getArguments().getSerializable("data");
        }

        binding.image.setImageResource(introModel.getImage());
        binding.tvTitle.setText(Html.fromHtml(introModel.getTitle()));
        binding.tvContent.setText(introModel.getContent());

    }
}
