package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class DriverFilterModel implements Serializable {
    private String order_by_type="";
    private String car_type_id="";
    private String area_id="";



    public String getOrder_by_type() {
        return order_by_type;
    }

    public String getCar_type_id() {
        return car_type_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setOrder_by_type(String order_by_type) {
        this.order_by_type = order_by_type;
    }

    public void setCar_type_id(String car_type_id) {
        this.car_type_id = car_type_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
}
