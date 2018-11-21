package com.app.controller;

import com.app.model.EmailManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.*;

public class EmailBrowserController {
    @FXML
    ListView<String> listedEmails;
    @FXML
    ListView<String> emailDetails;
    @FXML
    TextArea emailBody;

    @FXML
    HBox operationsPane;
    @FXML
    Button refresh;
    @FXML
    Button compose;
    @FXML
    Button reply;
    @FXML
    Button replyToAll;
    @FXML
    Button forward;
    @FXML
    Button delete;
    @FXML
    Button mark;
    @FXML
    Button save;

    private final EmailManager emailManager = new EmailManager();

    @FXML
    private void getEmails() throws IOException {
        listedEmails.getItems().clear();
        String workingDirection = Paths.get(".").toAbsolutePath().normalize().toString();
        File[] files = new File(workingDirection + "/emails/Inbox").listFiles();
        Arrays.sort(requireNonNull(files), Collections.reverseOrder());
        for (File file : files) {
            emailManager.addEmailToList(listedEmails, emailDetails, emailBody, file.getPath());
        }
    }

    @FXML
    public void handleSelectedEmail(MouseEvent arg0) throws IOException {
        String workingDirection = Paths.get(".").toAbsolutePath().normalize().toString();
        String extractedFilename = listedEmails.getSelectionModel().getSelectedItem();
        extractedFilename = extractedFilename.substring(extractedFilename.lastIndexOf("\n") + 1);
        extractedFilename = extractedFilename.replace("   ", " ");
        extractedFilename = extractedFilename.replace(":", "");
        emailManager.showEmail(emailDetails, emailBody, workingDirection + "/emails/Inbox/" + extractedFilename + ".txt");
    }
}
