package com.app;

import com.app.repositories.EmailRepository;
import com.app.repositories.UserRepository;
import com.app.repositories.dbrepositories.DbEmailRepository;
import com.app.repositories.dbrepositories.DbUserRepository;
import com.app.repositories.txtrepositories.TxtEmailRepository;
import com.app.repositories.txtrepositories.TxtUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.app"})
public class AppConfig {

    @Bean(name = "emailRepository")
    public EmailRepository getEmailRepository() {
        return new TxtEmailRepository(getUserRepository());
    }

    @Bean(name = "userRepository")
    public UserRepository getUserRepository() {
        return new TxtUserRepository();
    }
}
