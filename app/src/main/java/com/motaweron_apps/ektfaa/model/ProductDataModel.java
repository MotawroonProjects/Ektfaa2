package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class ProductDataModel extends StatusResponse implements Serializable {
    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }
}
