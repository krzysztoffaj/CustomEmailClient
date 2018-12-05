package com.app.emailbrowser;


import com.app.common.Email;

import java.util.List;

public interface EmailBrowserModel {

    public Email getEmail(String emailIdentifier);

    public List<Email> getEmails();

//    public String getSender(List<String> email);
//
//    public String getReceivers(List<String> email);
//
//    public String getSubject(List<String> email);
//
//    public String getMark(List<String> email);
//
//    public String getDate(List<String> email);
//
//    public String getBody(List<String> email);
}
