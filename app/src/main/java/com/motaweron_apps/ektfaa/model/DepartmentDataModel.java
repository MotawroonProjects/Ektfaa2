package com.motaweron_apps.ektfaa.model;

import java.util.List;

public class DepartmentDataModel extends StatusResponse {

    private List<DepartmentModel.BasicDepartmentFk> data;

    public List<DepartmentModel.BasicDepartmentFk> getData() {
        return data;
    }
}
