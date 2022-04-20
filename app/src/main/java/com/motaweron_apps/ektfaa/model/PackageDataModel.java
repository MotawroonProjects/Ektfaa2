package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class PackageDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private String package_expired_date;
        private List<PackageModel> packages;

        public List<PackageModel> getPackages() {
            return packages;
        }

        public String getPackage_expired_date() {
            return package_expired_date;
        }
    }
}
