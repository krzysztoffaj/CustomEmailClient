package com.app.emailbrowser;

import com.app.common.Email;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.*;

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
        getEmailList(selectedMailbox);
        setButtonsWidthToFillHbox();
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
        getEmailList(selectedMailbox);
    }

    @FXML
    private void handleMailboxesClick(ActionEvent event) {
        selectedMailbox = ((Button) event.getSource()).getText();
        getEmailList(selectedMailbox);
    }

    @FXML
    private void handleSelectedEmail() {
//        String workingDirectory = String.valueOf(Paths.get(".").toAbsolutePath());
        String emailIdentifier = emailList.getSelectionModel().getSelectedItem();
        System.out.println(emailIdentifier);
//        if (emailIdentifier != null) {
//            String preparedFilename = EmailBrowserModelTxt.prepareFilename(emailIdentifier);

//            emailManager.showEmail(emailDetails, emailBody,
//                    String.valueOf(Paths.get(workingDirectory, "emails", selectedMailbox, preparedFilename + ".txt")));
        showEmailDetails(emailIdentifier);
        showEmailBody(emailIdentifier);
    }

    @FXML
    private void getEmailList(String mailbox) {
        emailList.getItems().clear();
        List<Email> emails = emailBrowserModel.getEmails();
        for (Email email : emails) {
            addEmailToList(email);
        }
    }

    private void showEmailBody(String emailIdentifier) {
        emailBody.setText(emailBrowserModel.getEmail(emailIdentifier).getBody());
        emailBody.setEditable(false);
    }

    private void showEmailDetails(String emailIdentifier) {
        Email email = emailBrowserModel.getEmail(emailIdentifier);
        emailDetails.getItems().clear();
        emailDetails.getItems().add("From:\t" + email.getSender());
        emailDetails.getItems().add("To:\t\t" + email.getReceivers());
        emailDetails.getItems().add("Subject:\t" + email.getSubject());
        emailDetails.getItems().add("Date:\t" + email.getDate());
//        emailDetails.setMouseTransparent(true);

//        emailDetails.getItems().clear();
//        emailDetails.getItems().addAll(observableList);
    }

    private void addEmailToList(Email email) {
        List<String> emailDisplayedInList = new ArrayList<>();
        emailDisplayedInList.add(email.getSender() + "\n" +
                                 email.getSubject() + "\n" +
                                 email.getDate());
        ObservableList<String> emailDescription = FXCollections.observableArrayList(emailDisplayedInList);
        emailList.setCellFactory(param -> new ListCell<String>() {
            {
                prefWidthProperty().bind(emailList.widthProperty().subtract(2));
                setMaxWidth(Control.USE_PREF_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });

        emailList.getItems().addAll(emailDescription);
    }
}