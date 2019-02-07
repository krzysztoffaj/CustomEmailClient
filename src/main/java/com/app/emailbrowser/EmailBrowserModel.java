package com.app.emailbrowser;


import com.app.common.Email;

import java.util.List;

public interface EmailBrowserModel {

    Email getEmail(String mailbox, String emailIdentifier);

    List<Email> getEmails(String mailbox);

    String prepareEmailIdentifier(String mailbox, String emailIdentifier);

    void moveOrCopyEmailToOtherMailbox(String selectedEmail, String destination);
}
