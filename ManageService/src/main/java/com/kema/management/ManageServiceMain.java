package com.kema.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude =
        {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class})
public class ManageServiceMain {

    private static final Logger LOG =
            LoggerFactory.getLogger(ManageServiceMain.class);

    public static void main(String[] args) {
        SpringApplication.run(ManageServiceMain.class, args);
    }

}
