package com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/emailbrowser/EmailBrowserView.fxml"));
        primaryStage.setTitle("Custom Email Client");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
