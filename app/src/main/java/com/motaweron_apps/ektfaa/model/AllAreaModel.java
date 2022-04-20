package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class AllAreaModel implements Serializable {
    private List<SingleAreaModel> data;
    private int status;

    public List<SingleAreaModel> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}