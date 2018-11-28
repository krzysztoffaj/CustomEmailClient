package com.app.emailextractor;

import com.app.emailbrowser.EmailBrowserModel;
import com.app.emailbrowser.EmailBrowserModelTxt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {
    public void showEmail(ListView<String> emailDetails, TextArea emailBody, String path) throws IOException {
        EmailBrowserModel extractor = new EmailBrowserModelTxt(path);
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
        EmailBrowserModelTxt extractor = new EmailBrowserModelTxt(path);
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
