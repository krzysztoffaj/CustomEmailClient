package com.app.controllers;

import com.app.common.Email;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class EmailBrowserController {
    @FXML
    private Button inbox, sent, saved, draft, deleted;

    @FXML
    private Text backgroundOperation;
    @FXML
    private ProgressBar backgroundOperationProgress;

    @FXML
    private Button refresh;
    @FXML
    private TextField searchInput;
    @FXML
    private Button search;
    @FXML
    private ListView<Email> emailList;

    @FXML
    private TextArea emailDetails;
    @FXML
    private TextArea emailBody;

    @FXML
    private HBox operationsPane;
    @FXML
    private Button addressBook, newEmail, reply, replyToAll, forward, delete, mark, save;

    private String currentMailbox = "Inbox";

    private EmailService emailService;
    private UserService userService;

    @FXML
    public void setupStage(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/app/views/EmailBrowser.fxml"));
        fxmlLoader.setController(this);
        stage.setTitle("Custom Email Client");
        stage.setScene(new Scene(fxmlLoader.load(), 1200, 800));
        stage.setMinWidth(900);
        stage.setMinHeight(450);
        stage.show();

        setEmailListCells();
        setButtonsWidthToFillHbox();
        disableButtonsWhenEmailNotSelected();

        handleMailboxesButtons();
        handleRefreshButton();
        handleSearchButton();
        handleEmailListClicks();
        handleAddressBookClick();
        handleNewEmailClick();
        handleDeleteClick();
        handleSaveClick();

        getEmailList();
    }

    public EmailBrowserController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    private void handleRefreshButton() {
        refresh.setOnAction(e -> getEmailList());
    }

    private void handleSearchButton() {
        search.setOnAction(e -> {
            enableProgressBarAndDisplayOperation("Searching...");
            emailList.getItems().clear();

            Thread search = new Thread(() -> Platform.runLater(() -> {
                emailService.findByText(searchInput.getText())
                        .forEach(email -> emailList.getItems().add(email));
                disableProgressBar();
            }));
            search.setDaemon(true);
            search.start();
        });
    }

    private void setEmailListCells() {
        emailList.setCellFactory(param -> new ListCell<Email>() {
            @Override
            protected void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);

                if (empty || email == null) {
                    setText(null);
                } else {
                    setText(getDataForEmailList(email));
                }
            }
        });
    }

    private void handleEmailListClicks() {
        emailList.setOnMouseClicked(e -> {
            showEmailDetails(getSelectedEmail());
            emailBody.setText(getSelectedEmail().getBody());
        });
    }

    private void getEmailList() {
        enableProgressBarAndDisplayOperation("Loading emails...");
        emailList.getItems().clear();

        Thread loadEmail = new Thread(() -> Platform.runLater(() -> {
            emailService.getEmails().stream()
                    .filter(x -> x.getMailbox().equals(currentMailbox))
                    .forEach(e -> emailList.getItems().add(e));
            disableProgressBar();
        }));
        loadEmail.setDaemon(true);
        loadEmail.start();
    }

    private void handleNewEmailClick() {
        newEmail.setOnAction(e -> {
            new EmailComposerController(
                    this.emailService,
                    this.userService
            ).setupStage();
        });
    }

    private void handleAddressBookClick() {
        addressBook.setOnAction(e -> {
            new AddressBookController(
                    this.emailService,
                    this.userService
            ).setupStage();
        });
    }

    private void handleDeleteClick() {
        delete.setOnAction(e -> {
            enableProgressBarAndDisplayOperation("Deleting email...");
            Thread deleteEmail = new Thread(() -> {
                emailService.deleteEmail(getSelectedEmail());
                Platform.runLater(() -> {
                    getEmailList();
                    disableProgressBar();
                });
            });
            deleteEmail.setDaemon(true);
            deleteEmail.start();
        });
    }

    private void handleSaveClick() {
        save.setOnAction(e -> {
            enableProgressBarAndDisplayOperation("Saving email...");
            Thread saveEmail = new Thread(() -> {
                emailService.saveEmail(getSelectedEmail());
                Platform.runLater(() -> {
                    getEmailList();
                    disableProgressBar();
                });
            });
            saveEmail.setDaemon(true);
            saveEmail.start();
        });
    }

    private Email getSelectedEmail() {
        return emailList.getSelectionModel().getSelectedItem();
    }

    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, newEmail, reply, replyToAll, forward, delete, mark, save};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(operationsPane.widthProperty().divide(operations.length));
        }
    }

    private void handleMailboxesButtons() {
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
        emailDetails.setText(
                "From:\t" + email.getSender() + "\n" +
                "To:\t\t" + email.getReceiversFormatted() + "\n" +
                "Subject:\t" + email.getSubject() + "\n" +
                "Date:\t" + email.getDateTime());
    }

    private String getDataForEmailList(Email email) {
        if (currentMailbox.equals("Sent") || currentMailbox.equals("Draft")) {
            return email.getReceiversFormatted() + "\n" +
                   email.getSubject() + "\n" +
                   email.getDateTime();
        } else {
            return email.getSender() + "\n" +
                   email.getSubject() + "\n" +
                   email.getDateTime();
        }
    }

    private void enableProgressBarAndDisplayOperation(String operation) {
        backgroundOperation.setText(operation);
        backgroundOperationProgress.setVisible(true);
    }

    private void disableProgressBar() {
        backgroundOperation.setText("");
        backgroundOperationProgress.setVisible(false);
    }
}