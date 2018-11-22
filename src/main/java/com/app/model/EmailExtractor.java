package com.app.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public interface EmailExtractor {
    public List<String> getEmail() throws Exception;

    public String getSender(List<String> email);

    public String getReceivers(List<String> email);

    public String getSubject(List<String> email);

    public String getMark(List<String> email);

    public String getDate(List<String> email);

    public String getBody(List<String> email);
}
