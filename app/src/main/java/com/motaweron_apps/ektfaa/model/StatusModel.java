package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class StatusModel implements Serializable {
    private String id;
    private String status;

    public StatusModel(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
