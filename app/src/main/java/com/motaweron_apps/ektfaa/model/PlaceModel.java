package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class PlaceModel implements Serializable {
   private String id;
   private String user_id;
   private String title;
   private String main_image;
   private String price;
   private String area_id;
   private String address;
   private String latitude;
   private String longitude;
   private String details;
   private String whats_up_number;
   private String phone;
   private String is_published;
   private String distance;
   private List<UserModel.ImagesData> images_data;
   private List<ProductModel.FoodDetails> place_details;
   private SingleAreaModel area;
   private UserModel.Data user;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getPrice() {
        return price;
    }

    public String getArea_id() {
        return area_id;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDetails() {
        return details;
    }

    public String getWhats_up_number() {
        return whats_up_number;
    }

    public String getPhone() {
        return phone;
    }

    public String getIs_published() {
        return is_published;
    }

    public String getDistance() {
        return distance;
    }

    public List<UserModel.ImagesData> getImages_data() {
        return images_data;
    }

    public SingleAreaModel getArea() {
        return area;
    }

    public UserModel.Data getUser() {
        return user;
    }

    public List<ProductModel.FoodDetails> getPlace_details() {
        return place_details;
    }
}
