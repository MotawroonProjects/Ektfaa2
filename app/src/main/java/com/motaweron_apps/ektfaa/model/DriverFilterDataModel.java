package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class DriverFilterDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private List<CarType> car_types;
        private List<SingleAreaModel> areas;

        public List<CarType> getCar_types() {
            return car_types;
        }

        public List<SingleAreaModel> getAreas() {
            return areas;
        }
    }


}
