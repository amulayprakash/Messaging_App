package com.example.quagnitia.messaging_app.Model;

import java.io.Serializable;



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

}
