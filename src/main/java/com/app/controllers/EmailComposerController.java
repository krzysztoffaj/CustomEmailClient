package com.app.controllers;

import com.app.common.Email;
import com.app.common.EmailMarks;
import com.app.common.EmailResponseOptions;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

//@Controller("emailComposerController")
public class EmailComposerController {
    @FXML
    Button sendBtn;
    @FXML
    HBox additionalOperationsBox;
    @FXML
    Button addressBookBtn, attachFileBtn, saveDraftBtn, deleteBtn;

    @FXML
    TextField receiversField;
    @FXML
    TextField subjectField;

    @FXML
    TextArea emailBodyArea;

    private EmailService emailService;
    private UserService userService;
    private Email originalEmail;
    private Email composedEmail;

    private final static int DEFAULT_SENDER_ID = 1;

    public EmailComposerController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
        composedEmail = new Email();
    }

    public EmailComposerController(EmailService emailService, UserService userService, Email originalEmail, EmailResponseOptions responseOption) {
        this.emailService = emailService;
        this.userService = userService;
        this.originalEmail = originalEmail;

        switch (responseOption) {
            case REPLY:
                composedEmail = emailService.prepareReplyEmail(originalEmail);
                break;
            case REPLY_TO_ALL:
                composedEmail = emailService.prepareReplyToAllEmail(originalEmail);
                break;
            case FORWARD:
                composedEmail = emailService.prepareForwardEmail(originalEmail);
                break;
        }
    }

    @FXML
    public void setupStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EmailComposerController.class.getResource("/com/app/views/EmailComposer.fxml"));
            fxmlLoader.setController(this);
            Stage stage = new Stage();
            stage.setTitle("Email composer");
            stage.setScene(new Scene(fxmlLoader.load(), 1000, 750));
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.show();

            setButtonsWidthToFillHbox();
            setReceiversSubjectAndBody();

            handleSendClick();
            handleAddressBook();
            handleAttachFileClick();
            handleSaveDraftClick();
            handleDeleteClick();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBookBtn, attachFileBtn, saveDraftBtn, deleteBtn};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(additionalOperationsBox.widthProperty().divide(operations.length));
        }
    }

    private void handleSendClick() {
        sendBtn.setOnAction(e -> {
            if (emailProperlyFormatted(composedEmail)) {
                Thread sendEmail = new Thread(() -> {
                    Email email = setEmailProperties();
                    emailService.sendEmail(email);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.NONE, "E-mail sent!", ButtonType.OK);
                        alert.showAndWait();
                        Stage stage = (Stage) sendBtn.getScene().getWindow();
                        stage.close();
                    });
                });
                sendEmail.setDaemon(true);
                sendEmail.start();
            }
        });
    }

    private void handleAddressBook() {
        addressBookBtn.setOnAction(e -> {
            new AddressBookController(
                    this,
                    this.userService
            ).setupStage();
        });
    }

    private void handleAttachFileClick() {
        attachFileBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "It's gonna be implemented someday, I swear...", ButtonType.OK);
            alert.showAndWait();
        });
    }

    private void handleSaveDraftClick() {
        saveDraftBtn.setOnAction(e -> {
            Thread saveEmailDraft = new Thread(() -> {
                Email email = setEmailProperties();
                emailService.saveDraft(email);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.NONE, "E-mail saved as draft!", ButtonType.OK);
                    alert.showAndWait();
                    ((Stage) saveDraftBtn.getScene().getWindow()).close();
                });
            });
            saveEmailDraft.setDaemon(true);
            saveEmailDraft.start();
        });
    }

    private void handleDeleteClick() {
        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete this draft?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Stage stage = (Stage) deleteBtn.getScene().getWindow();
                stage.close();
            }
        });
    }

    private Email setEmailProperties() {
        composedEmail.setSender(userService.getUser(DEFAULT_SENDER_ID));
        composedEmail.setSubject(subjectField.getText());
        composedEmail.setReceivers(emailService.getReceiversFromTextField(receiversField.getText()));
        composedEmail.setMailbox("Sent");
        composedEmail.setMark(String.valueOf(EmailMarks.UNMARKED));
        composedEmail.setDateTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
        composedEmail.setBody(emailBodyArea.getText());

        return composedEmail;
    }

    private boolean emailProperlyFormatted(Email email) {
        if (receiversField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please specify receivers.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        String[] receiversEntered = receiversField.getText().split("\\s*,\\s*");

        if (Arrays.stream(receiversEntered).anyMatch(receiver -> receiver.contains(" ") || !receiver.contains("@") || !receiver.contains("."))) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid receivers entry.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    void appendToReceiversField(String input) {
        if (receiversField.getText().equals("")) {
            receiversField.setText(input);
        } else {
            receiversField.setText(receiversField.getText() + ", " + input);
        }
    }

    private void setReceiversSubjectAndBody() {
        if (composedEmail.getReceivers() != null) {
            receiversField.setText(userService.listReceiversEmailAddresses(composedEmail.getReceivers()));
            subjectField.setText(composedEmail.getSubject());
            emailBodyArea.setText(emailService.getOriginalEmailDetails(originalEmail));
        }
    }
}