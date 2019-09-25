package com.ysxsoft.imtalk.bean;

import io.rong.imlib.model.Message;

public class PalMessageBus {

    private Message newMessage;

    public PalMessageBus() {
    }

    public PalMessageBus(Message newMessage) {
        this.newMessage = newMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Message newMessage) {
        this.newMessage = newMessage;
    }
}
