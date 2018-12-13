package com.app.emailbrowser;

import com.app.common.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.*;


public class EmailBrowserController {
    @FXML
    Text backgroundOperation;
    @FXML
    ProgressBar backgroundOperationProgress;
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

    private String selectedMailbox = "Inbox";

    private final EmailBrowserModel emailBrowserModel = new EmailBrowserModelTxt();

    @FXML
    public void initialize() {
        setButtonsWidthToFillHbox();
        getEmailList();
    }

    @FXML
    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, newEmail, reply, replyToAll, forward, delete, mark, save};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(operationsPane.widthProperty().divide(operations.length));
        }
    }

    @FXML
    private void handleRefreshClick() {
        getEmailList();
    }

    @FXML
    private void handleMailboxesClick(ActionEvent event) {
        selectedMailbox = ((Button) event.getSource()).getText();
        getEmailList();
    }

    @FXML
    private void handleSelectedEmail() {
        backgroundOperation.setText("Loading email...");
        backgroundOperationProgress.setVisible(true);

        String selectedEmail = emailList.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            String emailIdentifier = emailBrowserModel.prepareEmailIdentifier(selectedMailbox, selectedEmail);

            Thread loadEmails = new Thread(() -> {
                Email email = emailBrowserModel.getEmail(selectedMailbox, emailIdentifier);
                Platform.runLater(() -> {
                    EmailBrowserController.this.showEmailDetails(email);
                    EmailBrowserController.this.showEmailBody(email);
                    backgroundOperation.setText("");
                    backgroundOperationProgress.setVisible(false);
                });
            });
            loadEmails.setDaemon(true);
            loadEmails.start();
        }
    }

    @FXML
    private void getEmailList() {
        backgroundOperation.setText("Loading emails...");
        backgroundOperationProgress.setVisible(true);

        emailList.getItems().clear();
        Thread loadEmail = new Thread(() -> {
            List<Email> emails = emailBrowserModel.getEmails(selectedMailbox);
            Platform.runLater(() -> {
                for (Email email : emails) {
                    addEmailToList(email);
                }
                backgroundOperation.setText("");
                backgroundOperationProgress.setVisible(false);
            });
        });
        loadEmail.setDaemon(true);
        loadEmail.start();
    }

    private void showEmailBody(Email email) {
        emailBody.setText(email.getBody());
    }

    private void showEmailDetails(Email email) {
        emailDetails.getItems().clear();
        emailDetails.getItems().add("From:\t" + email.getSender());
        emailDetails.getItems().add("To:\t\t" + email.getReceivers().toString().replace("[", "").replace("]", ""));
        emailDetails.getItems().add("Subject:\t" + email.getSubject());
        emailDetails.getItems().add("Date:\t" + email.getDate());
    }

    private void addEmailToList(Email email) {
        String emailDisplayedInList =
                        email.getSender() + "\n" +
                        email.getSubject() + "\n" +
                        email.getDate();
        emailList.getItems().add(emailDisplayedInList);
    }
}