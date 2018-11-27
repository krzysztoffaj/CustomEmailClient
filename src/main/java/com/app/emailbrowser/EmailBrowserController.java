package com.app.emailbrowser;

import com.app.emailextractor.EmailManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
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
    ListView<String> listedEmails;
    @FXML
    ListView<String> emailDetails;
    @FXML
    TextArea emailBody;

    @FXML
    HBox operationsPane;
    @FXML
    Button refresh, addressBook, newEmail, reply, replyToAll, forward, delete, mark, save;

    @FXML
    Button inbox, sent, saved, draft, deleted;

    private final EmailManager emailManager = new EmailManager();

//    private final EmailBrowserModel emailBrowserModel;
//
//
//    public EmailBrowserController(EmailBrowserModel emailBrowserModel) {
//        this.emailBrowserModel = emailBrowserModel;
//    }

    @FXML
    public void initialize() throws IOException {
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
    private void handleMailboxesClickEvents() {
        Button[] operations = {inbox, sent, draft, saved, deleted};
        for (Button operation : operations) {
            operation.setOnAction((event) -> {
                try {
                    getEmails(operation.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void getEmails(String mailbox) throws IOException {
        listedEmails.getItems().clear();
        String workingDirection = Paths.get(".").toAbsolutePath().normalize().toString();
        File[] files = new File(workingDirection + "/emails/" + mailbox).listFiles();
        Arrays.sort(requireNonNull(files), Collections.reverseOrder());
        for (File file : files) {
            emailManager.addEmailToList(listedEmails, emailDetails, emailBody, file.getPath());
        }
    }

    @FXML
    public void handleSelectedEmail() throws IOException {


//        String workingDirection = Paths.get(".").toAbsolutePath().normalize().toString();
//        String extractedFilename = listedEmails.getSelectionModel().getSelectedItem();
//        final String tak = EmailBrowserModelTxt.prepareFilename(extractedFilename);

//        emailManager.showEmail(emailDetails, emailBody, workingDirection + "/emails/Inbox/" + tak + ".txt");
//        listedEmails.setOnMouseClicked((event ->
//
//        {
//            try {
//                emailManager.showEmail(emailDetails, emailBody, workingDirection + "/emails/Inbox/" + tak + ".txt");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//                ));

//        String workingDirection = Paths.get(".").toAbsolutePath().normalize().toString();
//        String extractedFilename = listedEmails.getSelectionModel().getSelectedItem();
//        extractedFilename = EmailBrowserModelTxt.prepareFilename(extractedFilename);
//        emailManager.showEmail(emailDetails, emailBody, workingDirection + "/emails/Inbox/" + extractedFilename + ".txt");
    }
}
