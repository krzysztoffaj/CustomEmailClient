package com.app.infrastructure;

import com.app.models.Email;
import com.app.models.User;

import java.time.LocalDateTime;
import java.util.Set;

public class EmailBuilder {

    private final Email email;

    public EmailBuilder() {
        email = new Email();
    }

    public Email build() {
        return email;
    }

    public EmailBuilder withId(int id) {
        email.setId(id);
        return this;
    }

    public EmailBuilder withSender(User sender) {
        email.setSender(sender);
        return this;
    }

    public EmailBuilder withReceivers(Set<User> receivers) {
        email.setReceivers(receivers);
        return this;
    }

    public EmailBuilder withSubject(String subject) {
        email.setSubject(subject);
        return this;
    }

    public EmailBuilder withMailbox(String mailbox) {
        email.setMailbox(mailbox);
        return this;
    }

    public EmailBuilder withMark(String mark) {
        email.setMark(mark);
        return this;
    }

    public EmailBuilder withDateTime(LocalDateTime dateTime) {
        email.setDateTime(dateTime);
        return this;
    }

    public EmailBuilder withBody(String body) {
        email.setBody(body);
        return this;
    }
}
