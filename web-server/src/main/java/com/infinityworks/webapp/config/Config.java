package com.infinityworks.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.converter.PropertyToRowsConverter;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.filter.CorsConfig;
import com.infinityworks.webapp.pdf.CanvassTableConfig;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.GotvTableConfig;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.pdf.renderer.LogoRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@Configuration
public class Config {
    @Bean
    public RestErrorHandler errorHandler() {
        return new RestErrorHandler();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModules(new JavaTimeModule(), new Jdk8Module(), new GuavaModule())
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RequestValidator requestValidator(ObjectMapper mapper) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return new RequestValidator(factory.getValidator(), mapper);
    }

    @Bean
    public CorsConfig allowedHostsForCORS(Environment env) {
        String allowedHosts = env.getRequiredProperty("canvass.cors.hosts");
        String methods = env.getRequiredProperty("canvass.cors.methods");
        Set<String> hosts = new HashSet<>(asList(allowedHosts.split(",")));
        return new CorsConfig(hosts, methods);
    }

    @Bean
    @Qualifier("canvass")
    public TableBuilder canvassTableBuilder() {
        return new TableBuilder(new CanvassTableConfig());
    }

    @Bean
    @Qualifier("gotv")
    public TableBuilder gotvTableBuilder() {
        return new TableBuilder(new GotvTableConfig());
    }

    @Bean
    @Qualifier("canvass")
    public DocumentBuilder canvassDocumentBuilder(LogoRenderer logoRenderer) {
        return new DocumentBuilder(logoRenderer, new CanvassTableConfig());
    }

    @Bean
    @Qualifier("gotv")
    public DocumentBuilder gotvDocumentBuilder(LogoRenderer logoRenderer) {
        return new DocumentBuilder(logoRenderer, new GotvTableConfig());
    }

    @Bean
    public LogoRenderer logoRenderer() {
        URL logo = Resources.getResource("logo.png");
        return new LogoRenderer(logo);
    }

    @Bean
    public PropertyToRowsConverter propertyToRowConverter() {
        return new PropertyToRowsConverter();
    }

    @Bean
    public PafRequestExecutor commandExecutor() {
        return new PafRequestExecutor() {
        };
    }
}
