package com.app.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class EmailExtractor {

    private String path;

    public EmailExtractor(String path) {
        this.path = path;
    }

    public List<String> getEmail() throws IOException {
        return Files.readAllLines(Paths.get(this.path));
    }

    public String getSender(List<String> email) {
        return email.get(0);
    }

    public String getReceivers(List<String> email) {
        return email.get(1);
    }

    public String getSubject(List<String> email) {
        return email.get(2);
    }

    public String getMark(List<String> email) {
        return email.get(3);
    }

    public String getDate(List<String> email) {
        return email.get(4);
    }

    public String getBody(List<String> email) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < email.size(); i++) {
            stringBuilder.append(email.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }
}