package com.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    public void showEmail(ListView<String> emailDetails, TextArea emailBody, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/fxml/MainWindow.fxml"));
        EmailExtractorFromTxt extractor = new EmailExtractorFromTxt(path);
        List<String> email = extractor.getEmail();
        List<String> list = new ArrayList<>();
        list.add("From:\t" + extractor.getSender(email));
        list.add("To:\t\t" + extractor.getReceivers(email));
        list.add("Subject:\t" + extractor.getSubject(email));
        list.add("Date:\t" + extractor.getDate(email));
        ObservableList<String> observableList = FXCollections.observableArrayList(list);
        emailDetails.getItems().clear();
        emailDetails.getItems().addAll(observableList);
        emailDetails.setMouseTransparent(true);
        emailBody.setText(extractor.getBody(email));
        emailBody.setEditable(false);
    }

    public void addEmailToList(ListView<String> listedEmails, ListView<String> emailDetails, TextArea emailBody, String path) throws IOException {
        EmailExtractorFromTxt extractor = new EmailExtractorFromTxt(path);
        List<String> email = extractor.getEmail();

        List<String> list = new ArrayList<>();
        list.add(extractor.getSender(email) + "\n" +
                extractor.getSubject(email) + "\n" +
                extractor.getDate(email));
        ObservableList<String> observableList = FXCollections.observableArrayList(list);
        listedEmails.setCellFactory(param -> new ListCell<String>() {
            {
                prefWidthProperty().bind(listedEmails.widthProperty().subtract(2));
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

        listedEmails.getItems().addAll(observableList);
    }
}
