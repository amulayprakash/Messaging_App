package com.esf.quagnitia.messaging_app.Model;

/**
 * Created by Niki on 20-12-2018.
 */

public class Message {
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    String subject = "",
            body = "",
            background = "";

}
