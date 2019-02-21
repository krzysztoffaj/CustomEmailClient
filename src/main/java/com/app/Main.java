package com.app;

import com.app.emailbrowser.EmailBrowserController;
import com.app.repository.dbrepository.DbEmailRepository;
import com.app.repository.dbrepository.DbUserRepository;
import com.app.services.EmailService;
import com.app.services.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        EmailBrowserController.setupStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
