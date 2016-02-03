package com.infinityworks.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.ErrorHandler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.infinityworks.webapp.domain"})
@EnableJpaRepositories(basePackages = {"com.infinityworks.webapp.repository"})
@EnableTransactionManagement
@EnableCaching
public class Config {

    @Bean(destroyMethod = "close")
    public DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        dataSourceConfig.setUsername(env.getRequiredProperty("spring.datasource.username"));
        dataSourceConfig.setPassword(env.getRequiredProperty("spring.datasource.password"));
        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModules(new JavaTimeModule(), new Jdk8Module())
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Bean
    public RequestValidator requestValidator(ObjectMapper mapper) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return new RequestValidator(factory.getValidator(), mapper);
    }

    @Bean(name = "primitivesValidator")
    public Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
