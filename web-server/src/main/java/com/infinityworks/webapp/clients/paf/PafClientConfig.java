package com.infinityworks.webapp.clients.paf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinityworks.webapp.clients.paf.command.*;
import com.infinityworks.webapp.config.AppProperties;
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
    public OkHttpClient okHttpClient(AppProperties appProperties) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("X-Authorization", appProperties.getPafApiToken())
                    .build();
            return chain.proceed(request);
        })
                .hostnameVerifier((s, sslSession) -> true)
                .build();
    }

    @Bean
    public PafRequestExecutor requestExecutor() {
        return new PafRequestExecutor();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client, ObjectMapper objectMapper, AppProperties appProperties) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(appProperties.getPafApiBaseUrl())
                .client(client)
                .build();
    }

    // TODO provide better abstraction for these factories
    @Bean
    public GetVotersCommandFactory getVotersCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, AppProperties appProperties) {
        return new GetVotersCommandFactory(pafClient, appProperties.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public RecordVoteCommandFactory recordVoteCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, AppProperties appProperties) {
        return new RecordVoteCommandFactory(pafClient, appProperties.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public RecordContactCommandFactory recordContactCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, AppProperties appProperties) {
        return new RecordContactCommandFactory(pafClient, appProperties.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public GetStreetsCommandFactory getStreetsCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, AppProperties appProperties) {
        return new GetStreetsCommandFactory(pafClient, appProperties.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public SearchVotersCommandFactory searchVotersCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, AppProperties appProperties) {
        return new SearchVotersCommandFactory(pafClient, appProperties.getPafApiTimeout(), requestExecutor);
    }

    @Bean
    public DeleteContactCommandFactory deleteContactCommandFactory(PafClient pafClient, PafRequestExecutor requestExecutor, AppProperties appProperties) {
        return new DeleteContactCommandFactory(pafClient, appProperties.getPafApiTimeout(), requestExecutor);
    }
}
