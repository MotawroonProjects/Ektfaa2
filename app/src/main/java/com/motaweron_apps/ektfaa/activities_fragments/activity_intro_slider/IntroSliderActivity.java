package com.motaweron_apps.ektfaa.activities_fragments.activity_intro_slider;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_choose_location.ChooseLocationActivity;
import com.motaweron_apps.ektfaa.databinding.ActivityIntroSliderBinding;
import com.motaweron_apps.ektfaa.model.IntroModel;
import com.motaweron_apps.ektfaa.model.UserSettingsModel;

import java.util.ArrayList;
import java.util.List;


public class IntroSliderActivity extends BaseActivity {
    private ActivityIntroSliderBinding binding;
    private IntroAdapter adapter;
    private List<Fragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_slider);
        binding.setLifecycleOwner(this);

        initView();
    }


    private void initView() {
        fragmentList = new ArrayList<>();
        fragmentList.add(FragmentIntroSlider.newInstance(new IntroModel(R.drawable.slider1,getString(R.string.welcome_in_ektfaa), getString(R.string.we_deliver_order))));
        fragmentList.add(FragmentIntroSlider.newInstance(new IntroModel(R.drawable.slider2, getString(R.string.get_discounts), getString(R.string.many_discount))));
        fragmentList.add(FragmentIntroSlider.newInstance(new IntroModel(R.drawable.slider3, getString(R.string.easy_comm), getString(R.string.delegate_contact))));

        adapter = new IntroAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        binding.pager.setAdapter(adapter);
        binding.indicator.setViewPager(binding.pager);
        binding.btnNext.setOnClickListener(v -> {
            binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
        });
        binding.btnSkip.setOnClickListener(v -> {
            navigateToChooseLocationActivity();
        });
        binding.btnStart.setOnClickListener(v -> {
            navigateToChooseLocationActivity();

        });

        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < (fragmentList.size() - 1)) {
                    binding.btnSkip.setVisibility(View.VISIBLE);
                    binding.btnNext.setVisibility(View.VISIBLE);
                    binding.btnStart.setVisibility(View.GONE);
                } else {
                    binding.btnSkip.setVisibility(View.GONE);
                    binding.btnNext.setVisibility(View.GONE);
                    binding.btnStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void navigateToChooseLocationActivity() {

        UserSettingsModel model = getUserSettings();
        if (model == null) {
            model = new UserSettingsModel();

        }

        model.setFirstTime(false);
        setUserSettings(model);
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        startActivity(intent);
        finish();
    }
}