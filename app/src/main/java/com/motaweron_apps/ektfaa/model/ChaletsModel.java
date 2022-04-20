package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class ChaletsModel implements Serializable {
    private SingleChaletsModel data;
    private int status;

    public SingleChaletsModel getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}