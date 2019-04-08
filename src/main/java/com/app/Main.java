package com.app;

import com.app.controllers.EmailBrowserController;
import com.app.repository.txtrepository.TxtEmailRepository;
import com.app.repository.txtrepository.TxtUserRepository;
import com.app.services.DefaultEmailService;
import com.app.services.DefaultUserService;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);

        EmailBrowserController browserController = appContext.getBean("emailBrowserController", EmailBrowserController.class);

        browserController.setupStage(stage);

//        new EmailBrowserController(
//                new DefaultEmailService(new TxtEmailRepository(new TxtUserRepository()), new DefaultUserService(new TxtUserRepository())),
//                new DefaultUserService(new TxtUserRepository())
//        ).setupStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
