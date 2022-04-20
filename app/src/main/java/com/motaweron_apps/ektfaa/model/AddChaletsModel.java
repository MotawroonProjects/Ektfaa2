package com.motaweron_apps.ektfaa.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motaweron_apps.ektfaa.BR;
import com.motaweron_apps.ektfaa.R;

import java.io.Serializable;

public class AddChaletsModel extends BaseObservable implements Serializable {
    private String image;
    private String name;
    private String area_id;
    private String phone_code;
    private String phone;


    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.isEmpty() &&
                !phone.isEmpty()&&
                !area_id.isEmpty()
        ) {
            error_name.set(null);
            error_phone.set(null);
            return true;

        } else {
            /*if (image.isEmpty()) {
                Toast.makeText(context, R.string.ch_image, Toast.LENGTH_SHORT).show();
            }*/

            if (area_id.isEmpty()) {
                Toast.makeText(context, R.string.ch_area, Toast.LENGTH_SHORT).show();
            }

            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);

            }


            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else {
                error_phone.set(null);

            }


        }

        return false;

    }


    public AddChaletsModel() {
        image = "";
        name = "";
        phone = "";
        phone_code = "";
        area_id = "";

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }


    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
}
