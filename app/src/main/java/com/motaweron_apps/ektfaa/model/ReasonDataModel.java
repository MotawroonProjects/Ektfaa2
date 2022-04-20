package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class ReasonDataModel extends StatusResponse implements Serializable {
    private List<ReasonModel> data;

    public List<ReasonModel> getData() {
        return data;
    }
}
