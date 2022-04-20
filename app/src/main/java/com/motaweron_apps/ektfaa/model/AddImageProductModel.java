package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class AddImageProductModel implements Serializable {
    private String id;
    private String image;
    private String type;

    public AddImageProductModel(String id, String image, String type) {
        this.id = id;
        this.image = image;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
