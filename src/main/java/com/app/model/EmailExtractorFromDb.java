package com.app.model;

import java.util.List;

public class EmailExtractorFromDb implements EmailExtractor {

    @Override
    public List<String> getEmail() throws Exception {
        return null;
    }

    @Override
    public String getSender(List<String> email) {
        return null;
    }

    @Override
    public String getReceivers(List<String> email) {
        return null;
    }

    @Override
    public String getSubject(List<String> email) {
        return null;
    }

    @Override
    public String getMark(List<String> email) {
        return null;
    }

    @Override
    public String getDate(List<String> email) {
        return null;
    }

    @Override
    public String getBody(List<String> email) {
        return null;
    }
}
