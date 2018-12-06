package com.app.emailbrowser;


import com.app.common.Email;

import java.util.List;

public interface EmailBrowserModel {

    public Email getEmail(String emailIdentifier);

    public List<Email> getEmails();
}
