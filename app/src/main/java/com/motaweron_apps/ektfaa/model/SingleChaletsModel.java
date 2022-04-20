package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SingleChaletsModel extends StatusResponse implements Serializable {
   private PlaceModel data;

    public PlaceModel getData() {
        return data;
    }
}
