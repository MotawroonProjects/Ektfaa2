package com.motaweron_apps.ektfaa.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.model.UserSettingsModel;
import com.google.gson.Gson;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ektfaaUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("ektfaa_user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }


    public void createUpdateUserData(Context context, UserModel userModel) {
        SharedPreferences preferences = context.getSharedPreferences("ektfaaUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ektfaa_user_data", user_data);
        editor.apply();

    }

    public void clearUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ektfaaUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void create_update_user_settings(Context context, UserSettingsModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ektfaa_settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("ektfaa_settings", data);
        editor.apply();


    }

    public UserSettingsModel getUserSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ektfaa_settings_pref", Context.MODE_PRIVATE);
        UserSettingsModel model = new Gson().fromJson(preferences.getString("ektfaa_settings", ""), UserSettingsModel.class);
        return model;

    }

    public void create_update_user_area(Context context, SingleAreaModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ektfaaSettingArea", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (model != null) {
            String data = new Gson().toJson(model);
            editor.putString("ektfaa_area", data);
        } else {
            editor.clear();
        }

        editor.apply();


    }

    public SingleAreaModel getUserArea(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ektfaaSettingArea", Context.MODE_PRIVATE);
        SingleAreaModel model = new Gson().fromJson(preferences.getString("ektfaa_area", ""), SingleAreaModel.class);
        return model;

    }

    public void create_room_id(Context context, String room_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ektfaa_room", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ektfaa_room_id", room_id);
        editor.apply();


    }

    public String getRoom_Id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ektfaa_room", Context.MODE_PRIVATE);
        String chat_user_id = preferences.getString("ektfaa_room_id", "");
        return chat_user_id;
    }


}
