package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SingleMessageModel extends StatusResponse implements Serializable {
    private OrderChatModel.OrderChatMessage data;

    public OrderChatModel.OrderChatMessage getData() {
        return data;
    }
}
