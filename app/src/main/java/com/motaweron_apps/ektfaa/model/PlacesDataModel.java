package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class PlacesDataModel extends StatusResponse implements Serializable {
    private List<PlaceModel> data;

    public List<PlaceModel> getData() {
        return data;
    }
}
