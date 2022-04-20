package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SingleOrderModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private OrderModel order;
        private OrderChatModel order_chat;

        public OrderChatModel getOrder_chat() {
            return order_chat;
        }

        public OrderModel getOrder() {
            return order;
        }

    }
}
