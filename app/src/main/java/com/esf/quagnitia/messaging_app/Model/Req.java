package com.esf.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 08-01-2019.
 */

public class Req {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId = "";


    public String getFcmTokenId() {
        return fcmTokenId;
    }

    public void setFcmTokenId(String fcmTokenId) {
        this.fcmTokenId = fcmTokenId;
    }

    private String fcmTokenId = "";
}
