package com.example.quagnitia.messaging_app.Model;

import java.io.Serializable;


public class MsgResponse implements Serializable {

    private String error = "";


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    private User user = new User();

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Data data = new Data();

    public Data getPrev() {
        return prev;
    }

    public void setPrev(Data prev) {
        this.prev = prev;
    }

    private Data prev = new Data();

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    private Message message = new Message();

}
