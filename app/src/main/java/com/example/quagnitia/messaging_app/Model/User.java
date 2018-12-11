package com.example.quagnitia.messaging_app.Model;


import java.io.Serializable;

public class User implements Serializable {
    private String email = "";
    private String password = "";

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    private String login_type;

    {
        login_type = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
