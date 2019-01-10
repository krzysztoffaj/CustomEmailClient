package com.app.addressbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddressBookController {

    @FXML
    public static void setupStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddressBookController.class.getResource("/com/app/addressbook/AddressBookView.fxml"));
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Address book");
        secondaryStage.setScene(new Scene(fxmlLoader.load(), 800, 600));
        secondaryStage.setMinWidth(600);
        secondaryStage.setMinHeight(400);
        secondaryStage.show();
    }
}
