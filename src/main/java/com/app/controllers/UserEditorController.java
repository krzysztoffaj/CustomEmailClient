package com.app.controllers;

import com.app.common.User;
import com.app.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserEditorController {
    @FXML
    private TextField firstNameField, lastNameField, emailField;

    @FXML
    private Button okBtn, cancelBtn;

    private UserService userService;

    public UserEditorController(UserService userService) {
        this.userService = userService;
    }

    public UserEditorController(UserService userService, User userToEdit) {
        this.userService = userService;
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
