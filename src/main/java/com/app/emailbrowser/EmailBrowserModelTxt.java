package com.app.emailbrowser;

import com.app.common.Email;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EmailBrowserModelTxt implements EmailBrowserModel {
    private final String workingDirectory = String.valueOf(Paths.get("").toAbsolutePath());
    private final String emailDirectory = String.valueOf(Paths.get(workingDirectory, "emails", "inbox"));

    @Override
    public Email getEmail(String emailPath) {
        try {
            List<String> emailFile = Files.readAllLines(Paths.get(emailPath));
            Email email = new Email();

            email.setSender(emailFile.get(0));
            email.setReceivers(Arrays.asList(emailFile.get(1).split("\\s*,\\s*")));
            email.setSubject(emailFile.get(2));
            email.setMark(emailFile.get(3));
            email.setDate(LocalDateTime.parse((emailFile.get(4))));
            email.setBody(prepareEmailBody(emailFile));

            return email;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public List<Email> getEmails() {
        File[] files = new File(emailDirectory).listFiles();
        List<Email> emails = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                emails.add(getEmail(file.getPath()));
            }
        }
        return emails;
    }

    private String prepareFilename(String rawFilename) {
        String filename = rawFilename.substring(rawFilename.lastIndexOf("\n") + 1);
//        filename = filename.replace("   ", " ");
        filename = filename.replace(":", "");
        filename = filename + ".txt";

        return filename;
    }

    private String prepareEmailBody(List<String> emailFile) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < emailFile.size(); i++) {
            stringBuilder.append(emailFile.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }
}