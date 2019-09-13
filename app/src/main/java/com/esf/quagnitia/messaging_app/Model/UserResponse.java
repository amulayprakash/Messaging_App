package com.esf.quagnitia.messaging_app.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class UserResponse implements Serializable {

    public ArrayList<School>  getSchool() {
        return school;
    }

    public void setSchool(ArrayList<School>  school) {
        this.school = school;
    }

    private ArrayList<School> school = new ArrayList<School> ();

    private String error = "";

    public String getIsSessionValid() {
        return isSessionValid;
    }

    public void setIsSessionValid(String isSessionValid) {
        this.isSessionValid = isSessionValid;
    }

    private String isSessionValid="";

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userType = "";
    private String userId = "";

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private String sessionId = "";


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

    public int getOtherNotificationCount() {
        return otherNotificationCount;
    }

    public void setOtherNotificationCount(int otherNotificationCount) {
        this.otherNotificationCount = otherNotificationCount;
    }

    int otherNotificationCount=0;


    public MessageList getMessageList() {
        return messageList;
    }

    public void setMessageList(MessageList messageList) {
        this.messageList = messageList;
    }

    private MessageList messageList = new MessageList();

}
