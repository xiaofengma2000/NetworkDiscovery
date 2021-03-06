package com.kema.simulator.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude =
        {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class})
public class SshdMain {

    private static final Logger LOG =
            LoggerFactory.getLogger(SshdMain.class);

    public static void main(String[] args) {
        SpringApplication.run(SshdMain.class, args);
    }

}
