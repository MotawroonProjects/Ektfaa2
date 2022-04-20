package com.motaweron_apps.ektfaa.activities_fragments.activity_home.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_about_app.AboutAppActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseFragment;
import com.motaweron_apps.ektfaa.activities_fragments.activity_contactus.ContactUsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.HomeActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_language.LanguageActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_location.LocationsActivity;
import com.motaweron_apps.ektfaa.databinding.FragmentMoreBinding;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.tags.Tags;


import java.util.List;

public class FragmentMore extends BaseFragment {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    public static FragmentMore newInstance() {
        return new FragmentMore();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                SingleAreaModel model = (SingleAreaModel) result.getData().getSerializableExtra("data");
                setArea(model);

            }
            else  if (req == 100 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                String lang = result.getData().getStringExtra("lang");
                activity.refreshActivity(lang);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        binding.setLifecycleOwner(this);

        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();

        binding.llCity.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(activity, LocationsActivity.class);
            launcher.launch(intent);

        });

        binding.llContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);

        });

        binding.llTerms.setOnClickListener(view -> {
            String url = Tags.base_url + "api/our-terms";
            navigateToAboutActivity("0", url);

        });
        binding.llPrivacy.setOnClickListener(view -> {
            String url = Tags.base_url + "api/our-policy";
            navigateToAboutActivity("2", url);

        });
        binding.llAbout.setOnClickListener(view -> {
            String url = Tags.base_url + "api/about-us";
            navigateToAboutActivity("1", url);

        });

        binding.llAds.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddAdsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        binding.llRate.setOnClickListener(view -> {
            String appId = activity.getPackageName();
            Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appId));
            boolean marketFound = false;

            final List<ResolveInfo> otherApps = activity.getPackageManager()
                    .queryIntentActivities(rateIntent, 0);
            for (ResolveInfo otherApp : otherApps) {
                if (otherApp.activityInfo.applicationInfo.packageName
                        .equals("com.android.vending")) {

                    ActivityInfo otherAppActivity = otherApp.activityInfo;
                    ComponentName componentName = new ComponentName(
                            otherAppActivity.applicationInfo.packageName,
                            otherAppActivity.name
                    );
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    rateIntent.setComponent(componentName);
                    startActivity(rateIntent);
                    marketFound = true;
                    break;

                }
            }

            if (!marketFound) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
                startActivity(webIntent);
            }

        });
        binding.lllanguage.setOnClickListener(view -> NavigateToLanguageActivity());

    }

    private void NavigateToLanguageActivity() {
        req = 100;
        Intent intent = new Intent(activity, LanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        launcher.launch(intent);
    }

    private void navigateToAboutActivity(String type, String url) {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("type", type);
        intent.putExtra("url", url);
        startActivity(intent);


    }


}
