package com.example.quagnitia.messaging_app.Model;

import java.io.Serializable;
import java.util.List;


public class UserResponse implements Serializable {

    private String error = "";


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String message = "";
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

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    private Text text = new Text();

}
