package com.app.models;

import java.util.Set;

public class Email implements EntityId {
    private int id;
    private User sender;
    private Set<User> receivers;
    private String subject;
    private String mailbox;
    private String mark;
    private String dateTime;
    private String body;

    public Email() {
    }

    public Email(int id) {
        this.id = id;
    }

    public Email(int id, User sender, Set<User> receivers, String subject, String mailbox, String mark, String dateTime, String body) {
        this.id = id;
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.mailbox = mailbox;
        this.mark = mark;
        this.dateTime = dateTime;
        this.body = body;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Set<User> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<User> receivers) {
        this.receivers = receivers;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
