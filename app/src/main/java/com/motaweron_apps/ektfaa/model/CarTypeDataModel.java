package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class CarTypeDataModel extends StatusResponse implements Serializable {
    private List<CarType> data;

    public List<CarType> getData() {
        return data;
    }
}
