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
    private Button inboxBtn, sentBtn, savedBtn, draftBtn, deletedBtn;

    @FXML
    private Text backgroundOperationTxt;
    @FXML
    private ProgressBar backgroundOperationPb;

    @FXML
    private Button refreshBtn;
    @FXML
    private TextField searchInputField;
    @FXML
    private Button searchBtn;
    @FXML
    private ListView<Email> emailList;

    @FXML
    private TextArea emailDetailsArea;
    @FXML
    private TextArea emailBodyArea;

    @FXML
    private HBox operationsBox;
    @FXML
    private Button addressBookBtn, newEmailBtn, replyBtn, replyToAllBtn, forwardBtn, deleteBtn, markBtn, saveBtn;

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
        refreshBtn.setOnAction(e -> getEmailList());
    }

    private void handleSearchButton() {
        searchBtn.setOnAction(e -> {
            enableProgressBarAndDisplayOperation("Searching...");
            emailList.getItems().clear();

            Thread search = new Thread(() -> Platform.runLater(() -> {
                emailService.findByText(searchInputField.getText())
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
            emailBodyArea.setText(getSelectedEmail().getBody());
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
        newEmailBtn.setOnAction(e -> {
            new EmailComposerController(
                    this.emailService,
                    this.userService
            ).setupStage();
        });
    }

    private void handleAddressBookClick() {
        addressBookBtn.setOnAction(e -> {
            new AddressBookController(
                    this.emailService,
                    this.userService
            ).setupStage();
        });
    }

    private void handleDeleteClick() {
        deleteBtn.setOnAction(e -> {
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
        saveBtn.setOnAction(e -> {
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
        Button[] operations = {addressBookBtn, newEmailBtn, replyBtn, replyToAllBtn, forwardBtn, deleteBtn, markBtn, saveBtn};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(operationsBox.widthProperty().divide(operations.length));
        }
    }

    private void handleMailboxesButtons() {
        Button[] mailboxes = {inboxBtn, sentBtn, savedBtn, draftBtn, deletedBtn};
        for (Button mailbox : mailboxes) {
            mailbox.setOnAction(event -> {
                currentMailbox = mailbox.getText();
                getEmailList();
            });
        }
    }

    private void disableButtonsWhenEmailNotSelected() {
        Button[] operations = {replyBtn, replyToAllBtn, forwardBtn, deleteBtn, markBtn, saveBtn};
        for (Button operation : operations) {
            operation.disableProperty().bind(emailList.getSelectionModel().selectedItemProperty().isNull());
        }
    }

    private void showEmailBody(Email email) {
        emailBodyArea.setText(email.getBody());
    }

    private void showEmailDetails(Email email) {
        emailDetailsArea.setText(
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
        backgroundOperationTxt.setText(operation);
        backgroundOperationPb.setVisible(true);
    }

    private void disableProgressBar() {
        backgroundOperationTxt.setText("");
        backgroundOperationPb.setVisible(false);
    }
}