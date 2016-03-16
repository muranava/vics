package com.infinityworks.webapp.paf.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinityworks.webapp.config.PafApiConfig;
import com.infinityworks.webapp.paf.client.command.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class PafClientConfig {

    @Bean
    public PafClient pafClient2(Retrofit retrofit) {
        return retrofit.create(PafClient.class);
    }

    @Bean
    public OkHttpClient okHttpClient(PafApiConfig pafApiConfig) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("X-Authorization", pafApiConfig.getPafApiToken()).build();
            return chain.proceed(request);
        }).build();
    }

    @Bean
    public PafRequestExecutor requestExecutor() {
        return new PafRequestExecutor();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client, ObjectMapper objectMapper, PafApiConfig pafApiConfig) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(pafApiConfig.getPafApiBaseUrl())
                .client(client)
                .build();
    }

    // TODO provide better abstraction for these factories
    @Bean
    public GetVotersCommandFactory getVotersCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, PafApiConfig pafApiConfig) {
        return new GetVotersCommandFactory(pafClient, pafApiConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public RecordVoteCommandFactory recordVoteCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, PafApiConfig pafApiConfig) {
        return new RecordVoteCommandFactory(pafClient, pafApiConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public RecordContactCommandFactory recordContactCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, PafApiConfig pafApiConfig) {
        return new RecordContactCommandFactory(pafClient, pafApiConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public GetStreetsCommandFactory getStreetsCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, PafApiConfig pafApiConfig) {
        return new GetStreetsCommandFactory(pafClient, pafApiConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public SearchVotersCommandFactory searchVotersCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, PafApiConfig pafApiConfig) {
        return new SearchVotersCommandFactory(pafClient, pafApiConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public DeleteContactCommandFactory deleteContactCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, PafApiConfig pafApiConfig) {
        return new DeleteContactCommandFactory(pafClient, pafApiConfig.getPafApiTimeout(), requestExecutor);
    }
}
