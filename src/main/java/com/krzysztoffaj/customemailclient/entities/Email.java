package com.krzysztoffaj.customemailclient.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "email")
public class Email implements EntityId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "email_user",
            joinColumns = {@JoinColumn(name = "email_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> receivers;

    @Column(name = "subject")
    private String subject;

    @Column(name = "mailbox")
    private String mailbox;

    @Column(name = "mark")
    private String mark;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "body", columnDefinition = "LONGTEXT")
    private String body;

    public Email() {
    }

    public Email(int id) {
        this.id = id;
    }

    public Email(int id, User sender, Set<User> receivers, String subject, String mailbox, String mark, LocalDateTime dateTime, String body) {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
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
