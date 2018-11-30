package com.app.emailbrowser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.*;

public class EmailBrowserController {
    @FXML
    ListView<String> emailList;

    @FXML
    ListView<String> emailDetails;
    @FXML
    TextArea emailBody;

    @FXML
    HBox operationsPane;
    @FXML
    Button addressBook, newEmail, reply, replyToAll, forward, delete, mark, save;

    private String selectedMailbox = "Inbox";

    private final EmailBrowserModel emailBrowserModel = new EmailBrowserModelTxt();

    @FXML
    public void initialize() {
        getEmailList(selectedMailbox);
        setButtonsWidthToFillHbox();
    }

    @FXML
    private void setButtonsWidthToFillHbox() {
        Button[] operations = {addressBook, newEmail, reply, replyToAll, forward, delete, mark, save};
        for (Button operation : operations) {
            operation.prefWidthProperty().bind(operationsPane.widthProperty().divide(operations.length));
        }
    }

    @FXML
    private void handleRefreshClick() {
        getEmailList(selectedMailbox);
    }

    @FXML
    private void handleMailboxesClick(ActionEvent event) {
        selectedMailbox = ((Button) event.getSource()).getText();
        getEmailList(selectedMailbox);
    }

    @FXML
    private void handleSelectedEmail() {
//        String workingDirectory = String.valueOf(Paths.get(".").toAbsolutePath());
//        String extractedFilename = emailList.getSelectionModel().getSelectedItem();
//        if (extractedFilename != null) {
//            String preparedFilename = EmailBrowserModelTxt.prepareFilename(extractedFilename);

//            emailManager.showEmail(emailDetails, emailBody,
//                    String.valueOf(Paths.get(workingDirectory, "emails", selectedMailbox, preparedFilename + ".txt")));
        showEmailDetails();
        showEmailBody();
    }

    @FXML
    private void getEmailList(String mailbox) {
        emailList.getItems().clear();


        String workingDirectory = String.valueOf(Paths.get(".").toAbsolutePath());
        File[] files = new File(workingDirectory + "/emails/" + mailbox).listFiles();
        Arrays.sort(requireNonNull(files), Collections.reverseOrder());
        for (File file : files) {
            addEmailToList();
        }
    }

    private void showEmailBody() {
        emailBody.setText(emailBrowserModel.getBody(emailBrowserModel.getEmail()));
        emailBody.setEditable(false);
    }

    private void showEmailDetails() {
        List<String> email = emailBrowserModel.getEmail();
        emailDetails.getItems().clear();
        emailDetails.getItems().add("From:\t" + emailBrowserModel.getSender(email));
        emailDetails.getItems().add("To:\t\t" + emailBrowserModel.getReceivers(email));
        emailDetails.getItems().add("Subject:\t" + emailBrowserModel.getSubject(email));
        emailDetails.getItems().add("Date:\t" + emailBrowserModel.getDate(email));
//        emailDetails.setMouseTransparent(true);

//        emailDetails.getItems().clear();
//        emailDetails.getItems().addAll(observableList);
    }

    private void addEmailToList() {
        List<String> email = emailBrowserModel.getEmail();

        List<String> emailDisplayedInList = new ArrayList<>();
        emailDisplayedInList.add(emailBrowserModel.getSender(email) + "\n" +
                emailBrowserModel.getSubject(email) + "\n" +
                emailBrowserModel.getDate(email));
        ObservableList<String> observableList = FXCollections.observableArrayList(emailDisplayedInList);
        emailList.setCellFactory(param -> new ListCell<String>() {
            {
                prefWidthProperty().bind(emailList.widthProperty().subtract(2));
                setMaxWidth(Control.USE_PREF_SIZE);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });

        emailList.getItems().addAll(observableList);
    }
}