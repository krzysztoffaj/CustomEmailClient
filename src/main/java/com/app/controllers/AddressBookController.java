package com.app.controllers;

import com.app.common.User;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AddressBookController {
    @FXML
    private TableView usersTable;
    @FXML
    private TableColumn firstNameCol, lastNameCol, emailCol;

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

            getUserList();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void getUserList() {

//        Thread loadEmail = new Thread(() -> Platform.runLater(() -> {
            final List<User> users = userService.getUsers();
            usersTable.setItems(FXCollections.observableList(users));
            for (User user : users) {
//                firstNameCol
            }
//        }));
//        loadEmail.setDaemon(true);
//        loadEmail.start();
    }
}
