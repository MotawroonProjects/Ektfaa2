package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class OrderChatModel implements Serializable {

    private String id;
    private String order_id;
    private String user_id;
    private String driver_id;
    private String created_at;
    private String updated_at;
    private UserModel.Data user;
    private UserModel.Data driver;
    private List<OrderChatMessage> order_chat_messages;


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

    public UserModel.Data getUser() {
        return user;
    }

    public UserModel.Data getDriver() {
        return driver;
    }

    public List<OrderChatMessage> getOrder_chat_messages() {
        return order_chat_messages;
    }

    public static class OrderChatMessage implements Serializable {
        private String id;
        private String order_chat_id;
        private String from_user_id;
        private String to_user_id;
        private String type;
        private String message;
        private String image;
        private String voice;
        private String latitude;
        private String longitude;
        private String is_read;
        private String message_time;
        private UserModel.Data from_user;
        private UserModel.Data to_user;

        public String getId() {
            return id;
        }

        public String getOrder_chat_id() {
            return order_chat_id;
        }

        public String getFrom_user_id() {
            return from_user_id;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

        public String getImage() {
            return image;
        }

        public String getVoice() {
            return voice;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getIs_read() {
            return is_read;
        }

        public UserModel.Data getFrom_user() {
            return from_user;
        }

        public UserModel.Data getTo_user() {
            return to_user;
        }

        public String getMessage_time() {
            return message_time;
        }
    }
}
