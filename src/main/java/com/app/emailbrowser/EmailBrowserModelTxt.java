package com.app.emailbrowser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmailBrowserModelTxt implements EmailBrowserModel {
    private final String workingDirectory = String.valueOf(Paths.get(".").toAbsolutePath());
    private final String emailDirectory = String.valueOf(Paths.get(workingDirectory, "emails", "inbox"));

    @Override
    public List<List<String>> getEmails() {
        File[] files = new File(emailDirectory).listFiles();

        return null;
    }

    @Override
    public List<String> getEmail() {
        try {
            return Files.readAllLines(Paths.get(emailDirectory, "2018-06-06 2106.txt"));
        } catch (IOException e) {
            return new ArrayList<>();
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