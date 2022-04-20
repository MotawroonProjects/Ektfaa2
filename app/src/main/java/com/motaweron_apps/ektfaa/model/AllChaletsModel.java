package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class AllChaletsModel extends StatusResponse implements Serializable {
    private List<UserModel.Data> data;

    public List<UserModel.Data> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}