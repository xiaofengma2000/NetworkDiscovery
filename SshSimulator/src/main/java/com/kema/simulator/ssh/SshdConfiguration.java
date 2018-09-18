package com.kema.simulator.ssh;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.sshd.common.Factory;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.PropertyResolverUtils;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.shell.result.ResultHandlerConfig;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages =
        { "com.kema.simulator.ssh.repository" }, entityManagerFactoryRef = "sshdEntityManagerFactory", transactionManagerRef = "sshdTransactionManager")
@EntityScan(basePackages =
        { "com.kema.simulator.ssh.common" })
public class SshdConfiguration {

    @Autowired
    protected ApplicationContext applicationContext;

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
        factoryBean.setPackagesToScan("com.kema.simulator.ssh.common");
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

//    @Bean
//    MyShell shellWrapper(){
//	    //just to create it to satisfy the dependency, not used
//        //A new shell instance to be created when a request is created
//        MyShell shell = new MyShell(new DefaultResultHandler());
//        return shell;
//    }

    @Bean
    Factory<Command> getCmdFactory(){
	    return () -> new SpringShellCommandSupport(new SpringShell(applicationContext, new DefaultResultHandler()));
	}

//	@Bean(name = "standardParameterResolver")
//    ParameterResolver standardParameterResolver(){
//	    return new StandardParameterResolver(getConversionService());
//    }
//
//    @Bean
//    ConversionService getConversionService(){
//	    return new ApplicationConversionService();
//    }

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
