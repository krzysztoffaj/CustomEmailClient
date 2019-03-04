package com.app.addressbook;

import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddressBookController {
    private EmailService emailService;
    private UserService userService;

    public AddressBookController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @FXML
    public void setupStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBookController.class.getResource("/com/app/addressbook/AddressBook.fxml"));
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
}
