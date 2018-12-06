package com.app.emailbrowser;


import com.app.common.Email;

import java.util.List;

public interface EmailBrowserModel {

    public Email getEmail(String mailbox, String emailIdentifier);

    public List<Email> getEmails(String mailbox);

    public String prepareEmailIdentifier(String mailbox, String emailIdentifier);
}
