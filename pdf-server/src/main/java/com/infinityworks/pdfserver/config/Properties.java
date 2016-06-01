package com.infinityworks.pdfserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="pdfserver")
public class Properties {
    private String pafApiBaseUrl;
    private String pafApiTimeout;
    private String pafApiToken;

    public String getPafApiToken() {
        return pafApiToken;
    }

    public void setPafApiToken(String pafApiToken) {
        this.pafApiToken = pafApiToken;
    }

    public Integer getPafApiTimeout() {
        return Integer.valueOf(pafApiTimeout);
    }

    public void setPafApiTimeout(String pafApiTimeout) {
        this.pafApiTimeout = pafApiTimeout;
    }

    public String getPafApiBaseUrl() {
        return pafApiBaseUrl;
    }

    public void setPafApiBaseUrl(String pafApiBaseUrl) {
        this.pafApiBaseUrl = pafApiBaseUrl;
    }
}
