package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class ReasonModel implements Serializable {
    private String id;
    private String title_ar;
    private String title_en;
    private String user_type;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
