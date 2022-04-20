package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private String id;
    private String title;
    private String user_id;
    private String department_id;
    private String price;
    private String offer_price;
    private String details;
    private String area_id;
    private String rate;
    private String have_offer;
    private String offer_type;
    private String offer_value;
    private String is_published;
    private String created_at;
    private String updated_at;
    private List<FoodDetails> food_details;
    private DepartmentModel user_basic_department;
    private DepartmentModel user_sub_department;
    private List<UserModel.ImagesData> images;
    private UserModel.ImagesData default_image;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getArea_id() {
        return area_id;
    }

    public String getRate() {
        return rate;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public String getOffer_value() {
        return offer_value;
    }

    public String getIs_published() {
        return is_published;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public List<FoodDetails> getFood_details() {
        return food_details;
    }

    public DepartmentModel getUser_basic_department() {
        return user_basic_department;
    }

    public DepartmentModel getUser_sub_department() {
        return user_sub_department;
    }

    public UserModel.ImagesData getDefault_image() {
        return default_image;
    }

    public List<UserModel.ImagesData> getImages() {
        return images;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public static class FoodDetails implements Serializable{
        private String id;
        private String title;
        private String value;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }
    }


}
