package com.esf.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 20-12-2018.
 */

public class School {
    String aqiSchoolID = "",
            schoolName = "",
            lat = "",
            lng = "";

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    Message message = new Message();


}
