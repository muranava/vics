package com.infinityworks.webapp.clients.paf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.clients.paf.command.*;
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
    public OkHttpClient okHttpClient(CanvassConfig canvassConfig) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("X-Authorization", canvassConfig.getPafApiToken()).build();
            return chain.proceed(request);
        }).build();
    }

    @Bean
    public PafRequestExecutor requestExecutor() {
        return new PafRequestExecutor();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client, ObjectMapper objectMapper, CanvassConfig canvassConfig) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(canvassConfig.getPafApiBaseUrl())
                .client(client)
                .build();
    }

    // TODO provide better abstraction for these factories
    @Bean
    public GetVotersCommandFactory getVotersCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, CanvassConfig canvassConfig) {
        return new GetVotersCommandFactory(pafClient, canvassConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public RecordVoteCommandFactory recordVoteCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, CanvassConfig canvassConfig) {
        return new RecordVoteCommandFactory(pafClient, canvassConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public RecordContactCommandFactory recordContactCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, CanvassConfig canvassConfig) {
        return new RecordContactCommandFactory(pafClient, canvassConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public GetStreetsCommandFactory getStreetsCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, CanvassConfig canvassConfig) {
        return new GetStreetsCommandFactory(pafClient, canvassConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public SearchVotersCommandFactory searchVotersCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, CanvassConfig canvassConfig) {
        return new SearchVotersCommandFactory(pafClient, canvassConfig.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public DeleteContactCommandFactory deleteContactCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, CanvassConfig canvassConfig) {
        return new DeleteContactCommandFactory(pafClient, canvassConfig.getPafApiTimeout(), requestExecutor);
    }
}
