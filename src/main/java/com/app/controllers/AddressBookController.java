package com.app.controllers;

import com.app.common.User;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AddressBookController {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> firstNameCol, lastNameCol, emailCol;

    @FXML
    private Button addUserBtn, editUserBtn, deleteUserBtn;

    @FXML
    private TextField searchInputField;
    @FXML
    private Button searchBtn;

    @FXML
    private Button addReceiverBtn, removeReceiverBtn;

    @FXML
    private TextField receiversField;

    @FXML
    private Button okBtn, cancelBtn;

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
            Stage stage = new Stage();
            stage.setTitle("Address book");
            stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.show();

            disableButtonsWhenUserNotSelected();

            handleAddUserClick();
            handleEditUserClick();
            handleDeleteUserClick();
            handleSearchButton();
            handleAddReceiverClick();
            handleRemoveReceiverClick();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        getUserList();
        firstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailAddress()));
    }

    private void getUserList() {
        Thread loadEmail = new Thread(() -> {
            final List<User> users = userService.getUsers();
            Platform.runLater(() -> userTable.setItems(FXCollections.observableList(users)));
        });
        loadEmail.setDaemon(true);
        loadEmail.start();
    }

    private void handleAddUserClick() {
        addUserBtn.setOnAction(e -> {
            new UserEditorController(
                    this.userService
            ).setupStage();
        });
    }

    private void handleEditUserClick() {
        editUserBtn.setOnAction(e -> {
            new UserEditorController(
                    this.userService,
                    getSelectedUser()
            ).setupStage();
        });
    }

    private void handleDeleteUserClick() {
        deleteUserBtn.setOnAction(e -> {

        });
    }

    private void handleSearchButton() {
        searchBtn.setOnAction(e -> {
            userTable.getItems().clear();
            Thread search = new Thread(() -> Platform.runLater(() -> {
                userService.findByText(searchInputField.getText())
                        .forEach(email -> userTable.getItems().add(email));
            }));
            search.setDaemon(true);
            search.start();
        });
    }

    private void handleAddReceiverClick() {
        addReceiverBtn.setOnAction(e -> {
            if (receiversField.getText().equals("")) {
                receiversField.setText(getSelectedUser().getEmailAddress());
            } else {
                receiversField.setText(receiversField.getText() + ", " + getSelectedUser().getEmailAddress());
            }
        });
    }

    private void handleRemoveReceiverClick() {
        removeReceiverBtn.setOnAction(e -> {
            if (receiversField.getText().contains(getSelectedUser().getEmailAddress())) {
                receiversField.setText(receiversField.getText().replace(getSelectedUser().getEmailAddress(), ""));
            }
        });
    }

    private void disableButtonsWhenUserNotSelected() {
        Button[] operations = {editUserBtn, deleteUserBtn, addReceiverBtn, removeReceiverBtn};
        for (Button operation : operations) {
            operation.disableProperty().bind(userTable.getSelectionModel().selectedItemProperty().isNull());
        }
    }

    private User getSelectedUser() {
        return userTable.getSelectionModel().getSelectedItem();
    }
}
