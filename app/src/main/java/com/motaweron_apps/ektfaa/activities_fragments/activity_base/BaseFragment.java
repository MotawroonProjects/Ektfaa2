package com.motaweron_apps.ektfaa.activities_fragments.activity_base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.model.UserSettingsModel;
import com.motaweron_apps.ektfaa.preferences.Preferences;

import io.paperdb.Paper;

public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public String getLang() {
        Paper.init(context);
        return Paper.book().read("lang", "ar");
    }

    public UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(context);
    }

    public void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(context, userModel);
    }

    public SingleAreaModel getArea(){
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserArea(context);

    }
    public void setArea(SingleAreaModel singleAreaModel){
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_area(context,singleAreaModel);

    }
    public void setUserSettings(UserSettingsModel userSettingsModel){
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_settings(context,userSettingsModel);
    }

    public UserSettingsModel getUserSettings(){
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettings(context);
    }

    public void clearUserData(){
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(context);
    }
}
