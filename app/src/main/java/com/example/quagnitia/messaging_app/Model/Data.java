package com.example.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 20-12-2018.
 */

public class Data {
    public String getAqiSchoolID() {
        return aqiSchoolID;
    }

    public void setAqiSchoolID(String aqiSchoolID) {
        this.aqiSchoolID = aqiSchoolID;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    String aqiSchoolID = "",
            schoolName = "";

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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    String datetime="";
    String color="";
    String alert="";

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    boolean is_open =false;


}
