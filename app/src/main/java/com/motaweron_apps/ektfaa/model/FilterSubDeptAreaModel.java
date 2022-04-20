package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterSubDeptAreaModel implements Serializable {
    private List<String> subDepartmentIds;
    private String area_id;

    public FilterSubDeptAreaModel() {
        subDepartmentIds = new ArrayList<>();
        area_id = null;
    }

    public List<String> getSubDepartmentIds() {
        return subDepartmentIds;
    }

    public void setSubDepartmentIds(List<String> subDepartmentIds) {
        this.subDepartmentIds = subDepartmentIds;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
}
