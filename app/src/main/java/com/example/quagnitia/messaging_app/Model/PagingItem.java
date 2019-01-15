package com.example.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 22-10-2018.
 */

public class PagingItem {
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID = "";
    String page = "";



    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}
