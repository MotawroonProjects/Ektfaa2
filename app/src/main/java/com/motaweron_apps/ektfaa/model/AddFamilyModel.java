package com.motaweron_apps.ektfaa.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motaweron_apps.ektfaa.BR;
import com.motaweron_apps.ektfaa.R;

import java.io.Serializable;

public class AddFamilyModel extends BaseObservable implements Serializable {
    private String image;
    private String imageBanner;
    private String name;
    private String department_id;
    private String sub_department_id;
    private String area_id;
    private String phone_code;
    private String phone;
    private String address;
    private double lat;
    private double lng;


    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.isEmpty() &&
                !phone.isEmpty() &&
                !department_id.isEmpty() &&
                !sub_department_id.isEmpty() &&
                !area_id.isEmpty() &&
                !address.isEmpty()
        ) {
            error_name.set(null);
            error_address.set(null);
            error_phone.set(null);

            return true;

        } else {
           /* if (image.isEmpty()) {
                Toast.makeText(context, R.string.ch_image, Toast.LENGTH_SHORT).show();
            }
            if (imageBanner.isEmpty()) {
                Toast.makeText(context, R.string.ch_img_banner, Toast.LENGTH_SHORT).show();
            }*/

            if (area_id.isEmpty()) {
                Toast.makeText(context, R.string.ch_area, Toast.LENGTH_SHORT).show();
            }
            if (department_id.isEmpty()) {
                Toast.makeText(context, R.string.ch_dept, Toast.LENGTH_SHORT).show();
            }
            if (sub_department_id.isEmpty()) {
                Toast.makeText(context, R.string.ch_sub_dept, Toast.LENGTH_SHORT).show();
            }

            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);

            }

            if (address.isEmpty()) {
                error_address.set(context.getString(R.string.field_required));
            } else {
                error_address.set(null);

            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else {
                error_phone.set(null);

            }



        }

        return false;

    }


    public AddFamilyModel() {
        image="";
        imageBanner="";
        name="";
        department_id="";
        sub_department_id="";
        area_id="";
        phone_code="";
        phone ="";
        address="";
        lat=0.0;
        lng=0.0;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getSub_department_id() {
        return sub_department_id;
    }

    public void setSub_department_id(String sub_department_id) {
        this.sub_department_id = sub_department_id;
    }


    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
}
