package com.krzysztoffaj.customemailclient;

import com.krzysztoffaj.customemailclient.repositories.EmailRepository;
import com.krzysztoffaj.customemailclient.repositories.UserRepository;
import com.krzysztoffaj.customemailclient.repositories.dbrepositories.DbEmailRepository;
import com.krzysztoffaj.customemailclient.repositories.dbrepositories.DbUserRepository;
import com.krzysztoffaj.customemailclient.repositories.txtrepositories.TxtEmailRepository;
import com.krzysztoffaj.customemailclient.repositories.txtrepositories.TxtUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({"com.krzysztoffaj.customemailclient"})
public class AppConfig {

    @Bean(name = "emailRepository")
    public EmailRepository getEmailRepository() {
        return new DbEmailRepository(getUserRepository());
    }

    @Bean(name = "userRepository")
    public UserRepository getUserRepository() {
        return new DbUserRepository();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.krzysztoffaj.customemailclient.entities");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());

        return factory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("root1234");
        dataSource.setUrl("jdbc:mysql://localhost:3306/CustomEmailClient?autoReconnect=true&useSSL=false&useUnicode=true" +
                          "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        properties.put("hibernate.hbm2ddl.auto", "validate");

        return properties;
    }
}
