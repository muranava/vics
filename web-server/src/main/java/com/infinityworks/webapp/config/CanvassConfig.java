package com.infinityworks.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="canvass")
public class CanvassConfig {
    private String pafApiBaseUrl;

    public String getPafApiBaseUrl() {
        return pafApiBaseUrl;
    }

    public void setPafApiBaseUrl(String pafApiBaseUrl) {
        this.pafApiBaseUrl = pafApiBaseUrl;
    }
}
