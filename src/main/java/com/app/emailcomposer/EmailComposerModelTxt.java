package com.app.emailcomposer;

import com.app.common.Email;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class EmailComposerModelTxt implements EmailComposerModel {
    private final String workingDirectory = String.valueOf(Paths.get("").toAbsolutePath());
    private final String sentEmailDirectory = String.valueOf(Paths.get(workingDirectory, "emails", "Sent"));
    private final String draftEmailDirectory = String.valueOf(Paths.get(workingDirectory, "emails", "Draft"));

    @Override
    public void sendEmail(Email email) {
        handleEmailOperation(email, sentEmailDirectory);
    }

    @Override
    public void saveDraft(Email email) {
        handleEmailOperation(email, draftEmailDirectory);
    }

    private void handleEmailOperation(Email email, String emailDestination) {
        String newEmailPath = String.valueOf(Paths.get(emailDestination,
                email.getDate().replace(":", ".") + ".txt"));
        try (PrintWriter newEmailFile = new PrintWriter(newEmailPath)) {
            newEmailFile.println(email.getSender());
            newEmailFile.println(email.getReceiversFormatted());
            newEmailFile.println(email.getSubject());
            newEmailFile.println(email.getMark());
            newEmailFile.println(email.getDate());
            newEmailFile.println();
            newEmailFile.println();
            newEmailFile.println(email.getBody());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
