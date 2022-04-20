package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SingleFamilyModel extends StatusResponse implements Serializable {
   private UserModel.Data data;

    public UserModel.Data getData() {
        return data;
    }
}