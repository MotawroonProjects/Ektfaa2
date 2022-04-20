package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class UserModel extends StatusResponse implements Serializable{
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private String id;
        private String code;
        private String user_type;
        private String phone_code;
        private String phone;
        private String whats_up_number;
        private String name;
        private String email;
        private String address;
        private String latitude;
        private String longitude;
        private String car_type_id;
        private String logo;
        private String banner;
        private String status;
        private String approved_status;
        private String approved_by;
        private String is_blocked;
        private String is_login;
        private String logout_time;
        private String email_verified_at;
        private String confirmation_code;
        private String forget_password_code;
        private String software_type;
        private String deleted_at;
        private String created_at;
        private String updated_at;
        private String token;
        private String price;
        private static String firebase_token;
        private SingleAreaModel area;
        private CarType car_type;
        private DepartmentModel user_basic_department;
        private DepartmentModel user_sub_department;
        private List<DepartmentModel.BasicDepartmentFk> user_department_data;
        private List<ImagesData> images_data;

        private boolean selected = false;

        private Favorite user_market_favourite;

        public String getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getWhats_up_number() {
            return whats_up_number;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
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


        public String getCar_type_id() {
            return car_type_id;
        }

        public String getLogo() {
            return logo;
        }

        public String getBanner() {
            return banner;
        }

        public String getStatus() {
            return status;
        }

        public String getApproved_status() {
            return approved_status;
        }

        public String getApproved_by() {
            return approved_by;
        }

        public String getIs_blocked() {
            return is_blocked;
        }

        public String getIs_login() {
            return is_login;
        }

        public String getLogout_time() {
            return logout_time;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getConfirmation_code() {
            return confirmation_code;
        }

        public String getForget_password_code() {
            return forget_password_code;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getToken() {
            return token;
        }


        public SingleAreaModel getArea() {
            return area;
        }

        public CarType getCar_type() {
            return car_type;
        }

        public DepartmentModel getUser_basic_department() {
            return user_basic_department;
        }

        public DepartmentModel getUser_sub_department() {
            return user_sub_department;
        }

        public String getFirebase_token() {
            return firebase_token;
        }

        public List<ImagesData> getImages_data() {
            return images_data;
        }

        public String getPrice() {
            return price;
        }

        public void setFirebase_token(String firebase_token) {
            this.firebase_token = firebase_token;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public Favorite getUser_market_favourite() {
            return user_market_favourite;
        }

        public void setUser_market_favourite(Favorite user_market_favourite) {
            this.user_market_favourite = user_market_favourite;
        }

        public List<DepartmentModel.BasicDepartmentFk> getUser_department_data() {
            return user_department_data;
        }
    }

    public static class Favorite implements Serializable{

    }

    public static class ImagesData implements Serializable{
        private String id;
        private String image;

        public String getId() {
            return id;
        }

        public String getImage() {
            return image;
        }
    }
}
