package com.app.controllers;

import com.app.common.Email;
import com.app.common.EmailMarks;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;


public class EmailComposerController {
    @FXML
    Button send;
    @FXML
    HBox additionalOperations;
    @FXML
    Button addressBook, attachFile, saveDraft, delete;

    @FXML
    TextField receivers;
    @FXML
    TextField subject;

    @FXML
    TextArea emailBody;

    private EmailService emailService;
    private UserService userService;

    public EmailComposerController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @FXML
    public void setupStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EmailComposerController.class.getResource("/com/app/views/EmailComposer.fxml"));
            fxmlLoader.setController(this);
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Email composer");
            secondaryStage.setScene(new Scene(fxmlLoader.load(), 1000, 750));
            secondaryStage.setMinWidth(600);
            secondaryStage.setMinHeight(400);
            secondaryStage.show();

            setButtonsWidthToFillHbox();

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
        Button[] operations = {addressBook, attachFile, saveDraft, delete};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(additionalOperations.widthProperty().divide(operations.length));
        }
    }

    private void handleSendClick() {
        send.setOnAction(e -> {
            Email email = setEmailProperties();

            if (emailProperlyFormatted(email)) {
                Thread sendEmail = new Thread(() -> {
                    emailService.sendEmail(email);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.NONE, "E-mail sent!", ButtonType.OK);
                        alert.showAndWait();
                        Stage stage = (Stage) send.getScene().getWindow();
                        stage.close();
                    });
                });
                sendEmail.setDaemon(true);
                sendEmail.start();
            }
        });
    }

    private void handleAddressBook() {
        addressBook.setOnAction(e -> {
            new AddressBookController(
                    this.emailService,
                    this.userService
            ).setupStage();
        });
    }

    private void handleAttachFileClick() {
        attachFile.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "It's gonna be implemented someday, I swear...", ButtonType.OK);
            alert.showAndWait();
        });
    }

    private void handleSaveDraftClick() {
        saveDraft.setOnAction(e -> {
            Email email = setEmailProperties();

            Thread saveEmailAsDraft = new Thread(() -> {
            emailService.saveDraft(email);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.NONE, "E-mail saved as draft!", ButtonType.OK);
                    alert.showAndWait();
                    Stage stage = (Stage) saveDraft.getScene().getWindow();
                    stage.close();
                });
            });
            saveEmailAsDraft.setDaemon(true);
            saveEmailAsDraft.start();
        });
    }

    private void handleDeleteClick() {
        delete.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete this draft?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Stage stage = (Stage) delete.getScene().getWindow();
                stage.close();
            }
        });
    }

    private Email setEmailProperties() {
        Email email = new Email();

        email.setSender("Simple User <simple.user@yahoo.com>");
        email.setSubject(subject.getText());
        email.setReceivers(Arrays.asList(receivers.getText().split("\\s*,\\s*")));
        email.setMailbox("Sent");
        email.setMark(String.valueOf(EmailMarks.UNMARKED));
        email.setDateTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
        email.setBody(emailBody.getText());

        return email;
    }

    private boolean emailProperlyFormatted(Email email) {
        if (receivers.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please specify receivers.", ButtonType.OK);
            alert.showAndWait();
            return false;
        } else if (!email.getReceivers().stream().allMatch(s -> s.contains("@")) ||
                !email.getReceivers().stream().allMatch(s -> s.contains("."))) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid receivers entry.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}