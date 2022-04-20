package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SingleAreaModel implements Serializable {
    private int id;
    private String title;
    private boolean selected= false;


    public SingleAreaModel(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
