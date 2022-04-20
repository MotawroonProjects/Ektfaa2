package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class AllCategoryModel extends StatusResponse implements Serializable {
    private List<DepartmentModel.BasicDepartmentFk> data;

    public List<DepartmentModel.BasicDepartmentFk> getData() {
        return data;
    }
}