package com.motaweron_apps.ektfaa.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.motaweron_apps.ektfaa.BR;
import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.databinding.PropertyFieldBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddChaletAdsModel extends BaseObservable implements Serializable {
    private String name;
    private String price;
    private String descriptions;
    private String address;
    private String lat;
    private String lng;
    private String area_id;
    private List<PropertyFieldBinding> property;
    private List<AddImageProductModel> images;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_descriptions = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    private Context context;


    public boolean isDataValid(Context context) {
        if (!name.isEmpty() &&
                !address.isEmpty() &&
                !area_id.isEmpty() &&
                !descriptions.isEmpty() &&
                !price.isEmpty() &&
                images.size() > 0
        ) {

            error_name.set(null);
            error_price.set(null);
            error_descriptions.set(null);
            error_address.set(null);

            if (property.size() > 0) {
                if (isPropertyValid(context)) {

                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }


        } else {
            if (images.size() == 0) {
                Toast.makeText(context, R.string.ch_image, Toast.LENGTH_SHORT).show();
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

            if (price.isEmpty()) {
                error_price.set(context.getString(R.string.field_required));
            } else {
                error_price.set(null);

            }

            if (descriptions.isEmpty()) {
                error_descriptions.set(context.getString(R.string.field_required));
            } else {
                error_descriptions.set(null);

            }


            if (property.size() > 0) {
                isPropertyValid(context);
            }


            return false;

        }


    }

    private boolean isPropertyValid(Context context) {
        boolean isValid = true;

        for (PropertyFieldBinding binding : property) {
            String title = binding.edtPropertyTitle.getText().toString();
            if (title.isEmpty()) {
                isValid = false;
                binding.edtPropertyTitle.setError(context.getString(R.string.field_required));
            } else {
                binding.edtPropertyTitle.setError(null);

            }
        }

        return isValid;
    }

    public AddChaletAdsModel(Context context) {
        this.context = context;
        name = "";

        descriptions = "";
        area_id = "";
        price = "";
        address = "";
        lat = "0";
        lng = "0";
        property = new ArrayList<>();
        images = new ArrayList<>();
    }

    public List<AddImageProductModel> getImages() {
        return images;
    }

    public void setImages(List<AddImageProductModel> images) {
        this.images = images;
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
    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
        notifyPropertyChanged(BR.descriptions);

    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }


    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public List<PropertyFieldBinding> getProperty() {
        return property;
    }

    public void setProperty(List<PropertyFieldBinding> property) {
        this.property = property;
    }

}
