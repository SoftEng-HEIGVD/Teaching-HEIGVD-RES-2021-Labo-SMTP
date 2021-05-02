package model.mail;

import java.util.ArrayList;

public class Message {
    public final static String contentType = "Content-Type: text/plain; charset=utf-8";
    public final static String CRLF = "\r\n";

    private String subject;
    private String body;

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
}
