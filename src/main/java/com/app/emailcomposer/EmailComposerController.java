package com.app.emailcomposer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;


public class EmailComposerController {
    @FXML
    TextArea emailBody;

    @FXML
    Button send;
    @FXML
    HBox additionalOperations;
    @FXML
    Button addressBook, attachFile, save, delete;

    @FXML
    public void initialize() {
        setButtonsWidthToFillHbox();
    }

    @FXML
    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, attachFile, save, delete};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(additionalOperations.widthProperty().divide(operations.length));
        }
    }
}