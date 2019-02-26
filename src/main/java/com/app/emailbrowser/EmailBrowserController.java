package com.app.emailbrowser;

import com.app.addressbook.AddressBookController;
import com.app.common.Email;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class EmailBrowserController {
    @FXML
    private Button inbox, sent, saved, draft, deleted;

    @FXML
    private Text backgroundOperation;
    @FXML
    private ProgressBar backgroundOperationProgress;
    @FXML
    private ListView<String> emailList;

    @FXML
    private ListView<String> emailDetails;
    @FXML
    private TextArea emailBody;

    @FXML
    private HBox operationsPane;
    @FXML
    private Button addressBook, newEmail, reply, replyToAll, forward, delete, mark, save;

    private String currentMailbox = "Inbox";

    private final EmailBrowserModel model = new EmailBrowserModelTxt();
    private EmailService emailService;
    private UserService userService;

    @FXML
    public void setupStage(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/app/emailbrowser/EmailBrowserView.fxml"));
        fxmlLoader.setController(this);
        stage.setTitle("Custom Email Client");
        stage.setScene(new Scene(fxmlLoader.load(), 1200, 800));
        stage.setMinWidth(900);
        stage.setMinHeight(450);
        stage.show();

        setButtonsWidthToFillHbox();
        setMailboxButtonsHandlers();
        disableButtonsWhenEmailNotSelected();
        getEmailList();
    }

    public EmailBrowserController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
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

//        String selectedEmail = getSelectedEmail();
//        if (selectedEmail != null) {
//            backgroundOperation.setText("Loading email...");
//            backgroundOperationProgress.setVisible(true);
//            String emailIdentifier = model.prepareEmailIdentifier(currentMailbox, selectedEmail);
//
//            Thread loadEmails = new Thread(() -> {
//                Email email = model.getEmail(currentMailbox, emailIdentifier);
//                Platform.runLater(() -> {
//                    EmailBrowserController.this.showEmailDetails(email);
//                    EmailBrowserController.this.showEmailBody(email);
//                    backgroundOperation.setText("");
//                    backgroundOperationProgress.setVisible(false);
//                });
//            });
//            loadEmails.setDaemon(true);
//            loadEmails.start();
//        }
    }

    @FXML
    private void getEmailList() {
        final List<Email> emails = emailService.getEmails();

//        backgroundOperation.setText("Loading emails...");
//        backgroundOperationProgress.setVisible(true);

        emailList.getItems().clear();
        for (Email email : emails.stream()
                                .filter(x -> x.getMailbox()
                                .equals(currentMailbox)).collect(Collectors.toList()))
        {
            addEmailToList(email);
        }

//        Thread loadEmail = new Thread(() -> {
//            List<Email> emails = model.getEmails(currentMailbox);
//            Platform.runLater(() -> {
//                for (Email email : emails) {
//                    addEmailToList(email);
//                }
//                backgroundOperation.setText("");
//                backgroundOperationProgress.setVisible(false);
//            });
//        });
//        loadEmail.setDaemon(true);
//        loadEmail.start();
    }

    @FXML
    private void handleNewEmailClick() throws IOException {
//        EmailComposerController.setupStage();
    }

    @FXML
    private void handleAddressBookClick() throws IOException {
        AddressBookController.setupStage();
    }

    @FXML
    private void handleDeleteClick() {
        moveOrCopySelectedEmail("Deleted");
    }

    @FXML
    private void handleSaveClick() {
        moveOrCopySelectedEmail("Saved");
    }

    private void moveOrCopySelectedEmail(String destinationMailbox) {
        String operation = getVerbFromNoun(destinationMailbox);

        backgroundOperation.setText(operation + " email...");
        backgroundOperationProgress.setVisible(true);

        String emailIdentifier = model.prepareEmailIdentifier(currentMailbox, getSelectedEmail());
        Thread moveEmail = new Thread(() -> {
            model.moveOrCopyEmailToOtherMailbox(emailIdentifier, destinationMailbox);
            Platform.runLater(() -> {
                backgroundOperation.setText("");
                backgroundOperationProgress.setVisible(false);

                getEmailList();
            });
        });
        moveEmail.setDaemon(true);
        moveEmail.start();
    }

    private String getVerbFromNoun(String source) {
        return source.substring(0, source.length() - 2) + "ing";
    }

    private String getSelectedEmail() {
        return emailList.getSelectionModel().getSelectedItem();
    }

    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, newEmail, reply, replyToAll, forward, delete, mark, save};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(operationsPane.widthProperty().divide(operations.length));
        }
    }

    private void setMailboxButtonsHandlers() {
        Button[] mailboxes = {inbox, sent, saved, draft, deleted};
        for (Button mailbox : mailboxes) {
            mailbox.setOnAction(event -> {
                currentMailbox = mailbox.getText();
                getEmailList();
            });
        }
    }

    private void disableButtonsWhenEmailNotSelected() {
        Button[] operations = {reply, replyToAll, forward, delete, mark, save};
        for (Button operation : operations) {
            operation.disableProperty().bind(emailList.getSelectionModel().selectedItemProperty().isNull());
        }
    }

    private void showEmailBody(Email email) {
        emailBody.setText(email.getBody());
    }

    private void showEmailDetails(Email email) {
        emailDetails.getItems().clear();
        emailDetails.getItems().add("From:\t" + email.getSender());
        emailDetails.getItems().add("To:\t\t" + email.getReceiversFormatted());
        emailDetails.getItems().add("Subject:\t" + email.getSubject());
        emailDetails.getItems().add("Date:\t" + email.getDateTime());
    }

    private void addEmailToList(Email email) {
        String emailDisplayedInList;
        if (currentMailbox.equals("Sent") || currentMailbox.equals("Draft")) {
            emailDisplayedInList =
                            email.getReceiversFormatted() + "\n" +
                            email.getSubject() + "\n" +
                            email.getDateTime();
        } else {
            emailDisplayedInList =
                            email.getSender() + "\n" +
                            email.getSubject() + "\n" +
                            email.getDateTime();
        }
        emailList.getItems().add(emailDisplayedInList);
    }
}