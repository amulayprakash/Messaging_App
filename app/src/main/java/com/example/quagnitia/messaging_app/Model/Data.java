package com.example.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 20-12-2018.
 */

public class Data {

    private String subject = "";
    private String body = "";

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    /**/
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(String messageLevel) {
        this.messageLevel = messageLevel;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String messageID = "";
    String messageLevel = "";
    String messageName = "";
    String message = "";

    public String getAqiDateTime() {
        return aqiDateTime;
    }

    public void setAqiDateTime(String aqiDateTime) {
        this.aqiDateTime = aqiDateTime;
    }

    String aqiDateTime = "";

}
