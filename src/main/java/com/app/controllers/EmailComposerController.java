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

    public EmailComposerController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
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
            Email email = setEmailProperties();

            if (emailProperlyFormatted(email)) {
                Thread sendEmail = new Thread(() -> {
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
                    this.emailService,
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
            Email email = setEmailProperties();

            Thread saveEmailAsDraft = new Thread(() -> {
            emailService.saveDraft(email);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.NONE, "E-mail saved as draft!", ButtonType.OK);
                    alert.showAndWait();
                    ((Stage) saveDraftBtn.getScene().getWindow()).close();
                });
            });
            saveEmailAsDraft.setDaemon(true);
            saveEmailAsDraft.start();
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
        Email email = new Email();

        email.setSender("Simple User <simple.user@yahoo.com>");
        email.setSubject(subjectField.getText());
        email.setReceivers(Arrays.asList(receiversField.getText().split("\\s*,\\s*")));
        email.setMailbox("Sent");
        email.setMark(String.valueOf(EmailMarks.UNMARKED));
        email.setDateTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
        email.setBody(emailBodyArea.getText());

        return email;
    }

    private boolean emailProperlyFormatted(Email email) {
        if (receiversField.getText().equals("")) {
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

    public void appendToReceiversField(String input) {
        if (receiversField.getText().equals("")) {
            receiversField.setText(input);
        } else {
            receiversField.setText(receiversField.getText() + ", " + input);
        }
    }
}