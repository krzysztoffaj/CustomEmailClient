package com.app.controllers;

import com.app.common.User;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class AddressBookController {
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> firstNameCol, lastNameCol, emailCol;

    @FXML
    private Button addUserBtn, editUserBtn, deleteUserBtn, addReceiverBtn, removeReceiverBtn;

    private EmailService emailService;
    private UserService userService;

    public AddressBookController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @FXML
    public void setupStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBookController.class.getResource("/com/app/views/AddressBook.fxml"));
            fxmlLoader.setController(this);
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Address book");
            secondaryStage.setScene(new Scene(fxmlLoader.load(), 800, 600));
            secondaryStage.setMinWidth(600);
            secondaryStage.setMinHeight(400);
            secondaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        usersTable.setItems(FXCollections.observableList(userService.getUsers()));
        firstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailAddress()));
    }

    private void getUserList() {

    }

    private void handleAddUserClick() {
        addUserBtn.setOnAction(e -> {

        });
    }

    private void handleEditUserClick() {
        editUserBtn.setOnAction(e -> {

        });
    }

    private void handleDeleteUserClick() {
        deleteUserBtn.setOnAction(e -> {

        });
    }

    private void handleAddReceiverClick() {
        addReceiverBtn.setOnAction(e -> {

        });
    }

    private void handleRemoveReceiverClick() {
        removeReceiverBtn.setOnAction(e -> {

        });
    }
}
