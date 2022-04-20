package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SingleDepartmentModel extends StatusResponse implements Serializable {
    private DepartmentModel.BasicDepartmentFk data;

    public DepartmentModel.BasicDepartmentFk getData() {
        return data;
    }
}
