package com.app.controllers;

import com.app.models.User;
import com.app.services.UserService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    private EmailComposerController emailComposerController;
    private UserService userService;

    public AddressBookController(EmailComposerController emailComposerController, UserService userService) {
        this.emailComposerController = emailComposerController;
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
            if (emailComposerController == null) {
                disableReceiversOperations();
            }

            handleAddUserClick();
            handleEditUserClick();
            handleDeleteUserClick();
            handleSearchButton();
            handleAddReceiverClick();
            handleRemoveReceiverClick();
            handleOkClick();
            handleCancelClick();
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

    void getUserList() {
        userTable.getItems().clear();
        Thread loadEmail = new Thread(() -> Platform.runLater(() -> {
            userService.getUsers().stream()
                    .filter(User::isInAddressBook)
                    .forEach(user -> userTable.getItems().add(user));
        }));
        loadEmail.setDaemon(true);
        loadEmail.start();
    }

    private void handleAddUserClick() {
        addUserBtn.setOnAction(e -> {
            new UserEditorController(
                    this,
                    this.userService
            ).setupStage();
        });
    }

    private void handleEditUserClick() {
        editUserBtn.setOnAction(e -> {
            new UserEditorController(
                    this,
                    this.userService,
                    getSelectedUser()
            ).setupStage();
        });
    }

    private void handleDeleteUserClick() {
        deleteUserBtn.setOnAction(e -> {
            userService.deleteUser(getSelectedUser());
            getUserList();
        });
    }

    private void handleSearchButton() {
        searchBtn.setOnAction(e -> {
            userTable.getItems().clear();
            Thread search = new Thread(() -> Platform.runLater(() -> {
                userService.findUserByText(searchInputField.getText()).stream()
                        .filter(User::isInAddressBook)
                        .forEach(email -> userTable.getItems().add(email));
            }));
            search.setDaemon(true);
            search.start();
        });
    }

    private void handleAddReceiverClick() {
        addReceiverBtn.setOnAction(e -> {
            if (receiversField.getText().equals("")) {
                receiversField.appendText(getSelectedUser().getEmailAddress());
            } else {
                receiversField.appendText(", " + getSelectedUser().getEmailAddress());
            }
        });
    }

    private void handleRemoveReceiverClick() {
        removeReceiverBtn.setOnAction(e -> {
            if (receiversField.getText().contains(getReceiverWithSeparator(getSelectedUser().getEmailAddress()))) {
                receiversField.setText(receiversField.getText().replace(getReceiverWithSeparator(getSelectedUser().getEmailAddress()), ""));
            } else if (receiversField.getText().contains(getSelectedUser().getEmailAddress())) {
                receiversField.setText(receiversField.getText().replace(getSelectedUser().getEmailAddress(), ""));
            }
        });
    }

    private void handleOkClick() {
        okBtn.setOnAction(e -> {
            if (emailComposerController != null) {
                emailComposerController.appendToReceiversField(receiversField.getText());
            }
            ((Stage) okBtn.getScene().getWindow()).close();
        });
    }

    private void handleCancelClick() {
        cancelBtn.setOnAction(e -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });
    }

    private String getReceiverWithSeparator(String receiver) {
        return (receiver + ", ");
    }

    private void disableButtonsWhenUserNotSelected() {
        Button[] operations = {editUserBtn, deleteUserBtn, addReceiverBtn, removeReceiverBtn};
        for (Button operation : operations) {
            operation.disableProperty().bind(userTable.getSelectionModel().selectedItemProperty().isNull());
        }
    }

    private void disableReceiversOperations() {
        addReceiverBtn.disableProperty().unbind();
        addReceiverBtn.setDisable(true);
        removeReceiverBtn.disableProperty().unbind();
        removeReceiverBtn.setDisable(true);
        receiversField.setDisable(true);
    }

    private User getSelectedUser() {
        return userTable.getSelectionModel().getSelectedItem();
    }
}
