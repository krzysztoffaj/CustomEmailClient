package com.app.emailcomposer;

import com.app.common.Email;
import com.app.common.EmailMarks;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    Button addressBook, attachFile, saveDraft, delete;

    @FXML
    TextField receivers;
    @FXML
    TextField subject;

    @FXML
    TextArea emailBody;

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
//        fxmlLoader.setController(this);
    }

    @FXML
    private void initialize() {
        setButtonsWidthToFillHbox();
    }

    @FXML
    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, attachFile, saveDraft, delete};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(additionalOperations.widthProperty().divide(operations.length));
        }
    }

    @FXML
    private void handleSendClick() {
        Email email = setEmailProperties();

        if (receivers.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please specify receivers.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else if (!email.getReceivers().stream().allMatch(s -> s.contains("@")) ||
                !email.getReceivers().stream().allMatch(s -> s.contains("."))) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid receivers entry.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Thread sendEmail = new Thread(() -> {
            model.sendEmail(email);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.NONE, "E-mail sent!", ButtonType.OK);
                alert.showAndWait();
                Stage stage = (Stage) send.getScene().getWindow();
                stage.close();
            });
        });
        sendEmail.setDaemon(true);
        sendEmail.start();
    }

    @FXML
    public void handleAttachFileClick() {
        Alert alert = new Alert(Alert.AlertType.NONE, "It's gonna be implemented someday, I swear...", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public void handleSaveDraftClick() {
        Email email = setEmailProperties();

        Thread saveEmailAsDraft = new Thread(() -> {
            model.saveDraft(email);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.NONE, "E-mail saved as draft!", ButtonType.OK);
                alert.showAndWait();
                Stage stage = (Stage) saveDraft.getScene().getWindow();
                stage.close();
            });
        });
        saveEmailAsDraft.setDaemon(true);
        saveEmailAsDraft.start();
    }

    @FXML
    public void deleteEmail() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete this draft?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) delete.getScene().getWindow();
            stage.close();
        }
    }

    private Email setEmailProperties() {
        Email email = new Email();

        email.setSender("Simple User <simple.user@yahoo.com>");
        email.setSubject(subject.getText());
        email.setReceivers(Arrays.asList(receivers.getText().split("\\s*,\\s*")));email.setMark(String.valueOf(EmailMarks.UNMARKED));
        email.setDate(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
        email.setBody(emailBody.getText());

        return email;
    }
}