package com.app.services;

import com.app.common.Email;

import java.util.List;

public interface EmailService {
    List<Email> getEmails();

    List<Email> findByText(String text);

    void sendEmail(Email email);
}
