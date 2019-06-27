package com.krzysztoffaj.customemailclient.services;

import com.krzysztoffaj.customemailclient.entities.Email;
import com.krzysztoffaj.customemailclient.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EmailService {
    List<Email> getEmailsInMailbox(String mailbox);

    List<Email> findEmailByText(String mailbox, String text);

    void sendEmail(Email email);

    void saveEmail(Email email);

    void deleteEmail(Email email);

    void saveDraft(Email email);

    Email createEmailCopy(Email email);

    Email prepareReplyEmail(Email email);

    Email prepareReplyToAllEmail(Email email);

    Email prepareForwardEmail(Email email);

    String getFormattedDateTime(LocalDateTime dateTime);

    String getOriginalEmailDetails(Email email);

    String getEmailDetails(Email email);

    String getEmailInfoOnList(Email email, String mailbox);

    Set<User> getReceiversFromTextField(String input);
}
