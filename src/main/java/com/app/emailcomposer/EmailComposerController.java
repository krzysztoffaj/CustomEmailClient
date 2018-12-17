package com.app.emailcomposer;

import com.app.common.Email;
import com.app.common.EmailMarks;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;


public class EmailComposerController {
    @FXML
    Button send;
    @FXML
    HBox additionalOperations;
    @FXML
    Button addressBook, attachFile, save, delete;

    @FXML
    TextField receivers;
    @FXML
    TextField subject;

    @FXML
    TextArea emailBody;

    private Email email = new Email();
    private final EmailComposerModel model = new EmailComposerModelTxt();

    @FXML
    public static void setupStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EmailComposerController.class.getResource("/com/app/emailcomposer/EmailComposerView.fxml"));
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Email composer");
        secondaryStage.setScene(new Scene(fxmlLoader.load(), 1000, 750));
        secondaryStage.setMinWidth(600);
        secondaryStage.setMinHeight(400);
        secondaryStage.show();
    }

    @FXML
    private void initialize() {
        setButtonsWidthToFillHbox();
    }

    @FXML
    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, attachFile, save, delete};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(additionalOperations.widthProperty().divide(operations.length));
        }
    }

    @FXML
    private void handleSendClick() {
        email.setSender("Simple User <simple.user@yahoo.com>");
        email.setSubject(subject.getText());
        email.setReceivers(Arrays.asList(receivers.getText().split("\\s*,\\s*")));
        email.setMark(String.valueOf(EmailMarks.UNMARKED));
        email.setDate(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
        email.setBody(emailBody.getText());

        Thread sendEmail = new Thread(() -> {
            model.sendEmail(email);
            Platform.runLater(() -> {
                Stage stage = (Stage) send.getScene().getWindow();
                stage.close();
            });
        });
        sendEmail.setDaemon(true);
        sendEmail.start();
    }
}