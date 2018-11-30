package com.app.emailbrowser;


import java.util.List;

public interface EmailBrowserModel {
    public List getEmails();

    public List<String> getEmail();

    public String getSender(List<String> email);

    public String getReceivers(List<String> email);

    public String getSubject(List<String> email);

    public String getMark(List<String> email);

    public String getDate(List<String> email);

    public String getBody(List<String> email);
}
