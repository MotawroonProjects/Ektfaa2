package com.motaweron_apps.ektfaa.model;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motaweron_apps.ektfaa.BR;
import com.motaweron_apps.ektfaa.R;

import java.io.Serializable;

public class SendOrderModel extends BaseObservable implements Serializable {
    private String user_id;
    private String driver_id;
    private String family_id;
    private String type;
    private String name;
    private String from_latitude;
    private String from_longitude;
    private String from_location;
    private String to_latitude;
    private String to_longitude;
    private String to_location;
    private String details;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_from_location = new ObservableField<>();
    public ObservableField<String> error_to_location = new ObservableField<>();
    public ObservableField<String> error_details = new ObservableField<>();

    public boolean isDataValid(Context context) {
        Log.e("name" ,name+"_"+type+"_"+from_location+"_"+to_location+"_"+details);
        if (!name.isEmpty() &&
                !type.isEmpty() &&
                !details.isEmpty()&&
                !from_location.isEmpty()&&
                !to_location.isEmpty()
        ) {
            error_name.set(null);
            error_from_location.set(null);
            error_to_location.set(null);
            error_details.set(null);
            return true;
        } else {

            if (name.isEmpty()){
                Log.e("dd","ff");
                error_name.set(context.getString(R.string.field_required));

            }else {
                error_name.set(null);

            }

            if (from_location.isEmpty()){
                error_from_location.set(context.getString(R.string.field_required));

            }else {
                error_from_location.set(null);

            }

            if (to_location.isEmpty()){
                error_to_location.set(context.getString(R.string.field_required));

            }else {
                error_to_location.set(null);

            }

            if (details.isEmpty()){
                error_details.set(context.getString(R.string.field_required));

            }else {
                error_details.set(null);

            }
            return false;
        }
    }

    public SendOrderModel() {
        user_id = "";
        driver_id = "";
        type = "";
        name = "";
        from_latitude = "0.0";
        from_longitude = "0.0";
        from_location = "";
        to_latitude = "0.0";
        to_longitude = "0.0";
        to_location = "";
        details = "";
        family_id = "";


    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getFrom_latitude() {
        return from_latitude;
    }

    public void setFrom_latitude(String from_latitude) {
        this.from_latitude = from_latitude;
    }

    public String getFrom_longitude() {
        return from_longitude;
    }

    public void setFrom_longitude(String from_longitude) {
        this.from_longitude = from_longitude;
    }

    @Bindable
    public String getFrom_location() {
        return from_location;
    }

    public void setFrom_location(String from_location) {
        this.from_location = from_location;
        notifyPropertyChanged(BR.from_location);
    }

    public String getTo_latitude() {
        return to_latitude;
    }

    public void setTo_latitude(String to_latitude) {
        this.to_latitude = to_latitude;
    }

    public String getTo_longitude() {
        return to_longitude;
    }

    public void setTo_longitude(String to_longitude) {
        this.to_longitude = to_longitude;
    }

    @Bindable
    public String getTo_location() {
        return to_location;
    }

    public void setTo_location(String to_location) {
        this.to_location = to_location;
        notifyPropertyChanged(BR.to_location);
    }

    @Bindable
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        notifyPropertyChanged(BR.details);

    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }
}
