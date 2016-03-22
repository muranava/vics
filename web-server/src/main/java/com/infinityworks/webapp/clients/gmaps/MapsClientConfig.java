package com.infinityworks.webapp.clients.gmaps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommandFactory;
import com.infinityworks.webapp.config.CanvassConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class MapsClientConfig {
    @Bean
    public MapsClient mapsClient(CanvassConfig canvassConfig, ObjectMapper objectMapper) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("X-Authorization", canvassConfig.getAddressLookupToken()).build();
            return chain.proceed(request);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(canvassConfig.getAddressLookupBaseUrl())
                .client(client)
                .build();

        return retrofit.create(MapsClient.class);
    }

    @Bean
    public AddressLookupCommandFactory addressLookupCommandFactory(MapsClient mapsClient, CanvassConfig canvassConfig) {
        return new AddressLookupCommandFactory(mapsClient, canvassConfig.getPafApiTimeout(), canvassConfig.getAddressLookupToken(), new MapsRequestExecutor());
    }
}
