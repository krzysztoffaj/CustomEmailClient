package com.app.controllers;

import com.app.common.User;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserEditorController {
    @FXML
    private TextField firstNameField, lastNameField, emailField;

    @FXML
    private Button okBtn, cancelBtn;

    private AddressBookController addressBookController;
    private UserService userService;
    private User user;

    public UserEditorController(AddressBookController addressBookController, UserService userService) {
        this.addressBookController = addressBookController;
        this.userService = userService;
        user = new User();
    }

    public UserEditorController(AddressBookController addressBookController, UserService userService, User user) {
        this.addressBookController = addressBookController;
        this.userService = userService;
        this.user = user;
    }

    @FXML
    public void setupStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UserEditorController.class.getResource("/com/app/views/UserEditor.fxml"));
            fxmlLoader.setController(this);
            Stage stage = new Stage();
            stage.setTitle("User editor");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            handleOkClick();
            handleCancelClick();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmailAddress());
    }

    private void handleOkClick() {
        okBtn.setOnAction(e -> {
            if (userProperlyFormatted()) {
                Thread sendEmail = new Thread(() -> {
                    setUserProperties();

                    if (user.getId() == 0) {
                        userService.addUser(user);
                    } else {
                        userService.editUser(user);
                    }

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.NONE, "Success!", ButtonType.OK);
                        alert.showAndWait();
                        addressBookController.getUserList();
                        ((Stage) okBtn.getScene().getWindow()).close();
                    });
                });
                sendEmail.setDaemon(true);
                sendEmail.start();
            }
        });
    }

    private void handleCancelClick() {
        cancelBtn.setOnAction(e -> ((Stage) okBtn.getScene().getWindow()).close());
    }

    private void setUserProperties() {
        user.setFirstName(firstNameField.getText() == null ? "" : firstNameField.getText());
        user.setLastName(lastNameField.getText() == null ? "" : lastNameField.getText());
        user.setEmailAddress(emailField.getText() == null ? "" : emailField.getText());
        user.setInAddressBook(true);
    }

    private boolean userProperlyFormatted() {
        if (emailField.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please enter e-mail address.", ButtonType.OK);
            alert.showAndWait();
            return false;
        } else if (!emailField.getText().contains("@") ||
                   !emailField.getText().contains(".") ||
                   emailField.getText().contains(" ")) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid e-mail address.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
