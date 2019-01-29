package com.app.emailbrowser;

import com.app.addressbook.AddressBookController;
import com.app.common.Email;
import com.app.emailcomposer.EmailComposerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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

    private String currentMailbox = "Inbox";

    private final EmailBrowserModel model = new EmailBrowserModelTxt();

    @FXML
    public static void setupStage(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(EmailBrowserController.class.getResource("/com/app/emailbrowser/EmailBrowserView.fxml"));
        primaryStage.setTitle("Custom Email Client");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }

    @FXML
    private void initialize() {
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
        currentMailbox = ((Button) event.getSource()).getText();
        getEmailList();
    }

    @FXML
    private void handleSelectedEmail() {
        backgroundOperation.setText("Loading email...");
        backgroundOperationProgress.setVisible(true);

        String selectedEmail = emailList.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            String emailIdentifier = model.prepareEmailIdentifier(currentMailbox, selectedEmail);

            Thread loadEmails = new Thread(() -> {
                Email email = model.getEmail(currentMailbox, emailIdentifier);
                Platform.runLater(() -> {
                    EmailBrowserController.this.showEmailDetails(email);
                    EmailBrowserController.this.showEmailBody(email);
                    backgroundOperation.setText("");
                    backgroundOperationProgress.setVisible(false);
                });
            });
            loadEmails.setDaemon(true);
            loadEmails.start();

            disableEmailOperationsButtons(false);
        }
    }

    @FXML
    private void getEmailList() {
        backgroundOperation.setText("Loading emails...");
        backgroundOperationProgress.setVisible(true);

        emailList.getItems().clear();
        Thread loadEmail = new Thread(() -> {
            List<Email> emails = model.getEmails(currentMailbox);
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

    @FXML
    private void handleNewEmailClick() throws IOException {
        EmailComposerController.setupStage();
    }

    @FXML
    private void handleAddressBookClick() throws IOException {
        AddressBookController.setupStage();
    }

    @FXML
    private void handleDeleteClick() {
        backgroundOperation.setText("Deleting email...");
        backgroundOperationProgress.setVisible(true);

        String selectedEmail = emailList.getSelectionModel().getSelectedItem();
        String emailIdentifier = model.prepareEmailIdentifier(currentMailbox, selectedEmail);
        Thread deleteEmail = new Thread(() -> {
            model.moveEmailToDeleted(emailIdentifier);
            Platform.runLater(() -> {
                backgroundOperation.setText("");
                backgroundOperationProgress.setVisible(false);

                getEmailList();
            });
        });
        deleteEmail.setDaemon(true);
        deleteEmail.start();

        disableEmailOperationsButtons(true);
    }

    private void showEmailBody(Email email) {
        emailBody.setText(email.getBody());
    }

    private void showEmailDetails(Email email) {
        emailDetails.getItems().clear();
        emailDetails.getItems().add("From:\t" + email.getSender());
        emailDetails.getItems().add("To:\t\t" + email.getReceiversFormatted());
        emailDetails.getItems().add("Subject:\t" + email.getSubject());
        emailDetails.getItems().add("Date:\t" + email.getDate());
    }

    private void addEmailToList(Email email) {
        String emailDisplayedInList;
        if (currentMailbox.equals("Sent")) {
            emailDisplayedInList =
                            email.getReceiversFormatted() + "\n" +
                            email.getSubject() + "\n" +
                            email.getDate();
        } else {
            emailDisplayedInList =
                            email.getSender() + "\n" +
                            email.getSubject() + "\n" +
                            email.getDate();
        }
        emailList.getItems().add(emailDisplayedInList);
    }

    private void disableEmailOperationsButtons(boolean disabled) {
        Button[] emailOperations = {reply, replyToAll, forward, delete, mark, save};
        for (Button operation : emailOperations) {
            operation.setDisable(disabled);
        }
    }
}