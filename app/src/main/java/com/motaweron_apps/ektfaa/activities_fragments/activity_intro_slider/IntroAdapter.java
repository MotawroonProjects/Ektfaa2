package com.motaweron_apps.ektfaa.activities_fragments.activity_intro_slider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class IntroAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public IntroAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);

    }


    @Override
    public int getCount() {
        return fragments.size();
    }

}
