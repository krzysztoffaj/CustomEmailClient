package com.app.emailbrowser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EmailBrowserModelTxt implements EmailBrowserModel {

    private String path;

    private final String workingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();

    public EmailBrowserModelTxt(String path) {
        this.path = path;
    }

    @Override
    public List getEmails() {
        return null;
    }

    @Override
    public List<String> getEmail() {
        try {
            return Files.readAllLines(Paths.get(this.path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public String getSender(List<String> email) {
        return email.get(0);
    }

    @Override
    public String getReceivers(List<String> email) {
        return email.get(1);
    }

    @Override
    public String getSubject(List<String> email) {
        return email.get(2);
    }

    @Override
    public String getMark(List<String> email) {
        return email.get(3);
    }

    @Override
    public String getDate(List<String> email) {
        return email.get(4);
    }

    @Override
    public String getBody(List<String> email) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < email.size(); i++) {
            stringBuilder.append(email.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }

    public static String prepareFilename(String filename) {
        filename = filename.substring(filename.lastIndexOf("\n") + 1);
        filename = filename.replace("   ", " ");
        filename = filename.replace(":", "");

        return filename;
    }
}