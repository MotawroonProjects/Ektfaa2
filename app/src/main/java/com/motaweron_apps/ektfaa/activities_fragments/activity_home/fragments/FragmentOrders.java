package com.motaweron_apps.ektfaa.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseFragment;
import com.motaweron_apps.ektfaa.adapter.MyPagerAdapter;
import com.motaweron_apps.ektfaa.databinding.FragmentOrdersBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrders extends BaseFragment {

    private FragmentOrdersBinding binding;
    private List<String> titles;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        binding.setLifecycleOwner(this);

        initView();

        return binding.getRoot();
    }

    private void initView() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();

        titles.add(getString(R.string.current));
        titles.add(getString(R.string.prev));
        fragments.add(CurrentOrderFragment.newInstance());
        fragments.add(PreviousOrderFragment.newInstance());

        adapter = new MyPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments,titles);
        binding.pager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(fragments.size());






    }

}
