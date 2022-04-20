package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class PackageModel implements Serializable {
    private String id;
    private String number_of_days;
    private String title;
    private String cost;
    private String type;
    private String is_shown;
    private String service_id;
    private String department_id;
    private String created_at;
    private String updated_at;
    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public String getNumber_of_days() {
        return number_of_days;
    }

    public String getTitle() {
        return title;
    }

    public String getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public String getService_id() {
        return service_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
