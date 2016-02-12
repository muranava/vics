package com.infinityworks.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="canvass")
public class CanvassConfig {
    private String pafApiBaseUrl;
    private String pafApiToken;

    public void setPafApiToken(String pafApiToken) {
        this.pafApiToken = pafApiToken;
    }

    public String getPafApiBaseUrl() {
        return pafApiBaseUrl;
    }

    public String getPafApiToken() {
        return pafApiToken;
    }

    public void setPafApiBaseUrl(String pafApiBaseUrl) {
        this.pafApiBaseUrl = pafApiBaseUrl;
    }
}
