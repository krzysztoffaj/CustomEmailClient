package com.app;

import com.app.controllers.EmailBrowserController;
import com.app.repository.txtrepository.TxtEmailRepository;
import com.app.repository.txtrepository.TxtUserRepository;
import com.app.services.DefaultEmailService;
import com.app.services.DefaultUserService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new EmailBrowserController(
                new DefaultEmailService(new TxtEmailRepository()),
                new DefaultUserService(new TxtUserRepository())
        ).setupStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
