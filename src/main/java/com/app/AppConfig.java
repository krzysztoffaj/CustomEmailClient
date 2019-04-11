package com.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.app"})
public class AppConfig {

//    @Bean(name = "emailBrowserController")
//    public EmailBrowserController getEmailBrowserController() {
//        // Setter injection
////        DefaultCustomerService customerService = new DefaultCustomerService();
////        customerService.setCustomerRepository(getCustomerRepository());
//
//        // Constructor injection
////        CustomerService customerService = new DefaultCustomerService(getCustomerRepository());
//
//        // Autowired approach
//
//        return new EmailBrowserController(getEmailService(), getUserService());
//    }
//
//    @Bean(name = "emailService")
//    public EmailService getEmailService() {
//        return new DefaultEmailService(getEmailRepository(), getUserService());
//    }
//
//    @Bean(name = "emailService")
//    public UserService getUserService() {
//        return new DefaultUserService(getUserRepository());
//    }
//
//    @Bean(name = "emailRepository")
//    public EmailRepository getEmailRepository() {
//        return new TxtEmailRepository(getUserRepository());
//    }
//
//    @Bean(name = "userRepository")
//    public UserRepository getUserRepository() {
//        return new TxtUserRepository();
//    }
}
