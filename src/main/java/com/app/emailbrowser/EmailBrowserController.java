package com.app.emailbrowser;

import com.app.addressbook.AddressBookController;
import com.app.common.Email;
import com.app.emailcomposer.EmailComposerController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/app/emailbrowser/EmailBrowser.fxml"));
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
            showEmailBody(getSelectedEmail());
        });
    }

    @FXML
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

//    @FXML
//    private void handleDeleteClick() {
//        moveOrCopySelectedEmail("Deleted");
//    }
//
//    @FXML
//    private void handleSaveClick() {
//        moveOrCopySelectedEmail("Saved");
//    }

//    private void moveOrCopySelectedEmail(String destinationMailbox) {
//        String operation = getVerbFromNoun(destinationMailbox);
//
//        backgroundOperation.setText(operation + " email...");
//        backgroundOperationProgress.setVisible(true);
//
//        String emailIdentifier = model.prepareEmailIdentifier(currentMailbox, getSelectedEmail());
//        Thread moveEmail = new Thread(() -> {
//            model.moveOrCopyEmailToOtherMailbox(emailIdentifier, destinationMailbox);
//            Platform.runLater(() -> {
//                backgroundOperation.setText("");
//                backgroundOperationProgress.setVisible(false);
//
//                getEmailList();
//            });
//        });
//        moveEmail.setDaemon(true);
//        moveEmail.start();
//    }

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
        emailBody.clear();
        emailBody.setText(email.getBody());
    }

    private void showEmailDetails(Email email) {
        emailDetails.clear();
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