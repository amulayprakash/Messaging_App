package com.esf.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 22-10-2018.
 */

public class PagingItem {
//    public String getUserID() {
//        return userID;
//    }
//
//    public void setUserID(String userID) {
//        this.userID = userID;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId = "";
    String page = "";



    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}
