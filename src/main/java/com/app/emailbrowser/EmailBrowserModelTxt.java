package com.app.emailbrowser;

import com.app.common.Email;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EmailBrowserModelTxt implements EmailBrowserModel {
    private final String workingDirectory = String.valueOf(Paths.get("").toAbsolutePath());
    private final String emailDirectory = String.valueOf(Paths.get(workingDirectory, "emails"));

    @Override
    public Email getEmail(String mailbox, String emailPath) {
        try {
            List<String> emailFile = Files.readAllLines(Paths.get(emailPath));
            Email email = new Email(2);

            email.setSender(emailFile.get(0));
            email.setReceivers(Arrays.asList(emailFile.get(1).split("\\s*,\\s*")));
            email.setSubject(emailFile.get(2));
            email.setMark(emailFile.get(3));
            email.setDateTime((emailFile.get(4)));
            email.setBody(prepareEmailBody(emailFile));

            return email;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public List<Email> getEmails(String mailbox) {
        File[] files = new File(String.valueOf(Paths.get(emailDirectory, mailbox))).listFiles();
        List<Email> emails = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                emails.add(getEmail(mailbox, file.getPath()));
            }
        }
//        emails.sort(Comparator.comparing(Email::setDateTime).reversed());
        return emails;
    }

    @Override
    public String prepareEmailIdentifier(String mailbox, String rawEmailIdentifier) {
        String emailFile = rawEmailIdentifier.substring(rawEmailIdentifier.lastIndexOf("\n") + 1);
        emailFile = emailFile.replace(":", ".");
        emailFile = emailFile + ".txt";

        return String.valueOf(Paths.get(emailDirectory, mailbox, emailFile));
    }

    @Override
    public void moveOrCopyEmailToOtherMailbox(String selectedEmail, String destinationMailbox) {
        Path source = Paths.get(selectedEmail);
        Path destination = Paths.get(workingDirectory, "emails", destinationMailbox);
        try {
            if (destinationMailbox.equals("Saved")) {
                Files.copy(source, destination.resolve(source.getFileName()));
            } else if (destinationMailbox.equals("Deleted")) {
                Files.move(source, destination.resolve(source.getFileName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String prepareEmailBody(List<String> emailFile) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < emailFile.size(); i++) {
            stringBuilder.append(emailFile.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }
}