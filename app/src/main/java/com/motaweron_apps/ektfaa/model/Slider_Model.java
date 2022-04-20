package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class Slider_Model implements Serializable {
    private List<Data> data;
    private int status;

    public List<Data> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public class Data implements Serializable {
        private int id;
        private String image;
        private String type;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getType() {
            return type;
        }
    }
}
