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

    Email createEmailCopy(Email email);

    Email prepareReplyEmail(Email email);

    Email prepareReplyToAllEmail(Email email);

    Email prepareForwardEmail(Email email);

    String originalEmailDetails(Email email);

    String emailDetails(Email email);

    String emailInfoOnList(Email email, String mailbox);
}
