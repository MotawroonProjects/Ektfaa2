package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class OfferTypeModel implements Serializable {
    private String title;
    private String value;

    public OfferTypeModel(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
