package com.kema.simulator.ssh;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

import com.kema.simulator.ssh.command.ConfigCommand;
import org.apache.sshd.common.Factory;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.PropertyResolverUtils;
import org.apache.sshd.common.util.OsUtils;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.shell.ParameterResolver;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.shell.result.ResultHandlerConfig;
import org.springframework.shell.standard.StandardParameterResolver;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import(ResultHandlerConfig.class)
public class SshdConfiguration {

	@Bean
	SshdServer sshdServer() throws IOException {

		SshdServer sshdServer = new SshdServer("TEST-CISCO9K", getCmdFactory());
		
		final long idleTimeoutValue = TimeUnit.SECONDS.toMillis(600L);
        PropertyResolverUtils.updateProperty(sshdServer.getSshd(), FactoryManager.IDLE_TIMEOUT, idleTimeoutValue);

        final long disconnectTimeoutValue = TimeUnit.SECONDS.toMillis(600L);
        PropertyResolverUtils.updateProperty(sshdServer.getSshd(), FactoryManager.DISCONNECT_TIMEOUT, disconnectTimeoutValue);
		
		sshdServer.getSshd().setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		sshdServer.getSshd().setPort(22);
		sshdServer.getSshd().setPasswordAuthenticator(new PasswordAuthenticator() {
			public boolean authenticate(String username, String password, ServerSession session) {
				return true;
			}
		});
		//sshdServer.getSshd().setShellFactory(new ProcessShellFactory(OsUtils.WINDOWS_COMMAND));
		sshdServer.getSshd().start();
		return sshdServer;
	}

    @Bean
    PlatformTransactionManager sshdTransactionManager()
    {
        return new JpaTransactionManager(
                sshdEntityManagerFactory().getObject());
    }

    @Bean
    public JpaVendorAdapter sshdJpaVendorAdapter()
    {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.H2);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean sshdEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("SSHD");
        factoryBean.setBeanName("sshdEntityManagerFactory");
        factoryBean.setPackagesToScan("com.dm.ssh.sshd.model");
        factoryBean.setDataSource(sshdDataSource());
        factoryBean.setJpaVendorAdapter(sshdJpaVendorAdapter());
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.sshd")
    DataSource sshdDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean
    MyShell shellWrapper(){
        MyShell shell = new MyShell(new DefaultResultHandler());
        return shell;
    }

    @Bean
    BaseSpringShellCommand getCommand(){
	    return new BaseSpringShellCommand(shellWrapper());
    }

    @Bean
    Factory<Command> getCmdFactory(){
	    return () -> getCommand();
	}

	@Bean(name = "standardParameterResolver")
    ParameterResolver standardParameterResolver(){
	    return new StandardParameterResolver(getConversionService());
    }

    @Bean
    ConversionService getConversionService(){
	    return new ApplicationConversionService();
    }


    @Bean
    @Qualifier("spring-shell")
    public ConversionService shellConversionService(ApplicationContext applicationContext) {
        Collection<Converter> converters = applicationContext.getBeansOfType(Converter.class).values();
        Collection<GenericConverter> genericConverters = applicationContext.getBeansOfType(GenericConverter.class).values();
        Collection<ConverterFactory> converterFactories = applicationContext.getBeansOfType(ConverterFactory.class).values();

        DefaultConversionService defaultConversionService = new DefaultConversionService();
        for (Converter converter : converters) {
            defaultConversionService.addConverter(converter);
        }
        for (GenericConverter genericConverter : genericConverters) {
            defaultConversionService.addConverter(genericConverter);
        }
        for (ConverterFactory converterFactory : converterFactories) {
            defaultConversionService.addConverterFactory(converterFactory);
        }
        return defaultConversionService;
    }

    @Bean
    @ConditionalOnMissingBean(Validator.class)
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }


}
