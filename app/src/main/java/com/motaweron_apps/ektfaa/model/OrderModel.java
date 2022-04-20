package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private String id;
    private String type;
    private String name;
    private String user_id;
    private String driver_id;
    private String status;
    private String from_latitude;
    private String from_longitude;
    private String from_location;
    private String to_latitude;
    private String to_longitude;
    private String to_location;
    private String order_cost;
    private String delivery_value;
    private String details;
    private String refuse_reason;
    private String distance;
    private String created_at;
    private String updated_at;
    private UserModel.Data user;
    private UserModel.Data driver;
    private OrderChat order_chat;
    private OrderDriverOffer order_driver_offer;
    private String distance_driver_to_family;
    private String distance_family_to_user;


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getStatus() {
        return status;
    }

    public String getFrom_latitude() {
        return from_latitude;
    }

    public String getFrom_longitude() {
        return from_longitude;
    }

    public String getFrom_location() {
        return from_location;
    }

    public String getTo_latitude() {
        return to_latitude;
    }

    public String getTo_longitude() {
        return to_longitude;
    }

    public String getTo_location() {
        return to_location;
    }

    public String getOrder_cost() {
        return order_cost;
    }

    public String getDelivery_value() {
        return delivery_value;
    }

    public String getDetails() {
        return details;
    }

    public String getRefuse_reason() {
        return refuse_reason;
    }

    public String getDistance() {
        return distance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.Data getUser() {
        return user;
    }

    public UserModel.Data getDriver() {
        return driver;
    }

    public OrderChat getOrder_chat() {
        return order_chat;
    }

    public OrderDriverOffer getOrder_driver_offer() {
        return order_driver_offer;
    }

    public String getDistance_driver_to_family() {
        return distance_driver_to_family;
    }

    public String getDistance_family_to_user() {
        return distance_family_to_user;
    }

    public static class OrderChat implements Serializable {
        private String id;
        private String order_id;
        private String user_id;
        private String driver_id;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

    public static class OrderDriverOffer implements Serializable {
        private String id;
        private String user_id;
        private String driver_id;
        private String order_id;
        private String delivery_value;
        private String status;
        private String max_offer;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getDelivery_value() {
            return delivery_value;
        }

        public String getStatus() {
            return status;
        }

        public String getMax_offer() {
            return max_offer;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
