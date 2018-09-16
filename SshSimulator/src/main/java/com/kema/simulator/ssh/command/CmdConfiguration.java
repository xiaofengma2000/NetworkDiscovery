package com.kema.simulator.ssh.command;

import com.kema.simulator.ssh.BaseSpringShellCommand;
import com.kema.simulator.ssh.MyShell;
import com.kema.simulator.ssh.SshdServer;
import org.apache.sshd.common.Factory;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.PropertyResolverUtils;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.shell.ParameterResolver;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.shell.standard.StandardParameterResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class CmdConfiguration {

    ConfigCommand getConfigCommand(){
	    return new ConfigCommand();
    }

}
