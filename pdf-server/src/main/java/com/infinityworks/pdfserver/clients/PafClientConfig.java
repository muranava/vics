package com.infinityworks.pdfserver.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pdfserver.config.Properties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class PafClientConfig {

    @Bean
    public PafClient pafClient(Properties properties) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModules(new JavaTimeModule(), new Jdk8Module(), new GuavaModule())
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        OkHttpClient client = new OkHttpClient.Builder()
                .hostnameVerifier((s, sslSession) -> true)
                .readTimeout(properties.getPafApiTimeout(), TimeUnit.SECONDS)
                .writeTimeout(properties.getPafApiTimeout(), TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("X-Authorization", properties.getPafApiToken())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(properties.getPafApiBaseUrl())
                .client(client)
                .build();

        return retrofit.create(PafClient.class);
    }
}
