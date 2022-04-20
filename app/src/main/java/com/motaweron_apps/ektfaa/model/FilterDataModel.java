package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class FilterDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private List<DepartmentModel.BasicDepartmentFk> sub_departments;
        private List<SingleAreaModel> areas;

        public List<DepartmentModel.BasicDepartmentFk> getSub_departments() {
            return sub_departments;
        }

        public List<SingleAreaModel> getAreas() {
            return areas;
        }
    }
}
