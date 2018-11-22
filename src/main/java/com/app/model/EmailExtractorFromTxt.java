package com.app.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EmailExtractorFromTxt implements EmailExtractor {

    private String path;

    public EmailExtractorFromTxt(String path) {
        this.path = path;
    }

    @Override
    public List<String> getEmail() throws IOException {
        return Files.readAllLines(Paths.get(this.path));
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
}