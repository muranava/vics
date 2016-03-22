package com.infinityworks.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="canvass")
public class CanvassConfig {
    private String pafApiBaseUrl;
    private String pafApiTimeout;
    private String pafApiToken;
    private String addressLookupBaseUrl;
    private String addressLookupToken;

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

    public Integer getPafApiTimeout() {
        return Integer.valueOf(pafApiTimeout);
    }

    public void setPafApiTimeout(String pafApiTimeout) {
        this.pafApiTimeout = pafApiTimeout;
    }

    public String getAddressLookupToken() {
        return addressLookupToken;
    }

    public void setAddressLookupToken(String addressLookupToken) {
        this.addressLookupToken = addressLookupToken;
    }

    public String getAddressLookupBaseUrl() {
        return addressLookupBaseUrl;
    }

    public void setAddressLookupBaseUrl(String addressLookupBaseUrl) {
        this.addressLookupBaseUrl = addressLookupBaseUrl;
    }
}
