package com.infinityworks.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import com.infinityworks.webapp.pdf.CanvassTableConfig;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.converter.PropertyToRowsConverter;
import com.infinityworks.webapp.pdf.GotvTableConfig;
import com.infinityworks.webapp.pdf.renderer.FlagsKeyRenderer;
import com.infinityworks.webapp.pdf.renderer.LogoRenderer;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.filter.CorsConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.infinityworks.webapp.domain"})
@EnableJpaRepositories(basePackages = {"com.infinityworks.webapp.repository"})
@EnableTransactionManagement
public class Config {
    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

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
    public RestErrorHandler errorHandler() {
        return new RestErrorHandler();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModules(new JavaTimeModule(), new Jdk8Module(), new GuavaModule())
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
    public ClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper, ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();

        converters.stream()
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .forEach(converter -> {
                    MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                    jsonConverter.setObjectMapper(objectMapper);
                });
        return restTemplate;
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
        return new DocumentBuilder(logoRenderer, new FlagsKeyRenderer(), new CanvassTableConfig());
    }

    @Bean
    @Qualifier("gotv")
    public DocumentBuilder gotvDocumentBuilder(LogoRenderer logoRenderer) {
        return new DocumentBuilder(logoRenderer, new FlagsKeyRenderer(), new GotvTableConfig());
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
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setMaxConnTotal(DEFAULT_MAX_TOTAL_CONNECTIONS)
                .build();
    }
}
