package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class ChaletFilterModel implements Serializable {
    private String order_type;
    private String order_by;
    private String area_id;

    public ChaletFilterModel(String order_type, String order_by, String area_id) {
        this.order_type = order_type;
        this.order_by = order_by;
        this.area_id = area_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
}
