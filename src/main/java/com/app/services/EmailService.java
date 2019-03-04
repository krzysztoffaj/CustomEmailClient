package com.app.services;

import com.app.common.Email;

import java.util.List;

public interface EmailService {
    List<Email> getEmails();

    List<Email> findByText(String text);

    void sendEmail(Email email);

    void saveEmail(Email email);

    void deleteEmail(Email email);

    void saveDraft(Email email);
}
