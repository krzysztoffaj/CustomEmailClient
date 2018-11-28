package com.app.emailbrowser;

import com.app.emailextractor.EmailManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.*;

public class EmailBrowserController {
    @FXML
    Button refresh;
    @FXML
    ListView<String> emailList;

    @FXML
    ListView<String> emailDetails;
    @FXML
    TextArea emailBody;

    @FXML
    HBox operationsPane;
    @FXML
    Button addressBook, newEmail, reply, replyToAll, forward, delete, mark, save;

    @FXML
    Button inbox, sent, saved, draft, deleted;

    private final EmailManager emailManager = new EmailManager();

    private String selectedMailbox = "Inbox";

//    private final EmailBrowserModel emailBrowserModel;
//
//
//    public EmailBrowserController(EmailBrowserModel emailBrowserModel) {
//        this.emailBrowserModel = emailBrowserModel;
//    }

    @FXML
    public void initialize() throws Exception {
//        EmailBrowserController(new EmailBrowserModelTxt());
        getEmails(inbox.getText());
        setButtonsWidthToFillHbox();
        handleMailboxesClickEvents();
        handleSelectedEmail();
    }

    @FXML
    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, newEmail, reply, replyToAll, forward, delete, mark, save};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(operationsPane.widthProperty().divide(operations.length));
        }
    }

    @FXML
    private void handleRefreshClickEvents() {
//        refresh.setOnMouseClicked((event) -> {
            try {
                getEmails(selectedMailbox);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        });
    }

    @FXML
    private void handleMailboxesClickEvents() {
        Button[] operations = {inbox, sent, draft, saved, deleted};
        for (Button operation : operations) {
            operation.setOnMouseClicked((event) -> {
                try {
                    getEmails(operation.getText());
                    selectedMailbox = operation.getText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleSelectedEmail() {
        String workingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        emailList.setOnMouseClicked(event -> {
            String extractedFilename = emailList.getSelectionModel().getSelectedItem();
            String preparedFilename = EmailBrowserModelTxt.prepareFilename(extractedFilename);
            try {
                emailManager.showEmail(emailDetails, emailBody,
                        Paths.get(workingDirectory, "emails", selectedMailbox, preparedFilename + ".txt").normalize().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void getEmails(String mailbox) throws IOException {
        emailList.getItems().clear();
        String workingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        File[] files = new File(workingDirectory + "/emails/" + mailbox).listFiles();
        Arrays.sort(requireNonNull(files), Collections.reverseOrder());
        for (File file : files) {
            emailManager.addEmailToList(emailList, emailDetails, emailBody, file.getPath());
        }
    }
}