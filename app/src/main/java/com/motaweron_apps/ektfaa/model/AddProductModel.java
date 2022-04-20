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

public class AddProductModel extends BaseObservable implements Serializable {
    private String name;
    private String department_id;
    private String price;
    private String price_after_discount;
    private boolean have_offer;
    private String offer_type;
    private String offer_value;
    private String descriptions;
    private String area_id;
    private List<PropertyFieldBinding> property;
    private List<AddImageProductModel> images;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_descriptions = new ObservableField<>();
    public ObservableField<String> error_offer_value = new ObservableField<>();
    public ObservableField<String> error_price_after_discount = new ObservableField<>();
    private Context context;


    public boolean isDataValid(Context context) {
        if (!name.isEmpty() &&
                !department_id.isEmpty() &&
                !area_id.isEmpty() &&
                !descriptions.isEmpty() &&
                !price.isEmpty() &&
                images.size() > 0
        ) {

            error_name.set(null);
            error_price.set(null);
            error_descriptions.set(null);
            if (have_offer) {
                if (!offer_value.isEmpty() && !price_after_discount.isEmpty()) {
                    error_offer_value.set(null);
                    error_price_after_discount.set(null);

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
                    error_price_after_discount.set(context.getString(R.string.inv_offer));

                    error_offer_value.set(context.getString(R.string.field_required));
                    if (property.size() > 0) {
                        isPropertyValid(context);
                    }
                    return false;
                }
            } else {
                if (property.size() > 0) {
                    if (isPropertyValid(context)) {

                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
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


            if (department_id.isEmpty()) {
                Toast.makeText(context, R.string.ch_dept, Toast.LENGTH_SHORT).show();
            }

            if (property.size() > 0) {
                isPropertyValid(context);
            }

            if (have_offer) {
                if (offer_value.isEmpty()) {
                    error_offer_value.set(context.getString(R.string.field_required));

                } else {
                    error_offer_value.set(null);

                }

                if (price_after_discount.isEmpty()) {
                    error_price_after_discount.set(context.getString(R.string.inv_offer));

                } else {
                    error_price_after_discount.set(null);

                }
            } else {
                error_offer_value.set(null);
                error_price_after_discount.set(null);


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

    public AddProductModel(Context context) {
        this.context = context;
        name = "";
        department_id = "";
        descriptions = "";
        area_id = "";
        price = "";
        price_after_discount = "";
        have_offer = false;
        offer_type = "per";
        offer_value = "";
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

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
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
        calculatePriceAfterDiscount();

    }

    public boolean isHave_offer() {
        return have_offer;
    }

    public void setHave_offer(boolean have_offer) {
        this.have_offer = have_offer;
        if (!have_offer) {
            offer_value = "";
            price_after_discount = "";


        }
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
        calculatePriceAfterDiscount();
    }

    public String getOffer_value() {
        return offer_value;
    }

    public void setOffer_value(String offer_value) {
        this.offer_value = offer_value;
        calculatePriceAfterDiscount();
    }

    @Bindable
    public String getPrice_after_discount() {
        return price_after_discount;
    }

    public void setPrice_after_discount(String price_after_discount) {
        this.price_after_discount = price_after_discount;
        notifyPropertyChanged(BR.price_after_discount);
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

    private void calculatePriceAfterDiscount() {


        if (have_offer) {

            if (!offer_value.isEmpty() && !price.isEmpty()) {
                if (offer_type.equals("per")) {

                    price_after_discount = (Double.parseDouble(this.price) - (((Double.parseDouble(offer_value)) / 100.0) * Double.parseDouble(this.price))) + "";


                } else {
                    if (Double.parseDouble(this.price) > Double.parseDouble(offer_value)) {
                        price_after_discount = (Double.parseDouble(this.price) - Double.parseDouble(offer_value)) + "";
                        error_price_after_discount.set(null);

                    } else {
                        price_after_discount = "";
                        error_price_after_discount.set(context.getString(R.string.inv_offer));

                    }

                }

            }


            setPrice_after_discount(price_after_discount);


        }
    }
}
