package com.app.emailbrowser;

import com.app.common.Email;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.*;


public class EmailBrowserController {
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
        String selectedEmail = emailList.getSelectionModel().getSelectedItem();
        if(selectedEmail != null) {
            String emailIdentifier = emailBrowserModel.prepareEmailIdentifier(selectedMailbox, selectedEmail);
            showEmailDetails(emailIdentifier);
            showEmailBody(emailIdentifier);
        }
    }

    @FXML
    private void getEmailList() {
        emailList.getItems().clear();
        List<Email> emails = emailBrowserModel.getEmails(selectedMailbox);
        for (Email email : emails) {
            addEmailToList(email);
        }
    }

    private void showEmailBody(String emailIdentifier) {
        emailBody.setText(emailBrowserModel.getEmail(selectedMailbox, emailIdentifier).getBody());
    }

    private void showEmailDetails(String emailIdentifier) {
        Email email = emailBrowserModel.getEmail(selectedMailbox, emailIdentifier);
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