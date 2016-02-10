package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Street {
    private final String mainStreet;
    private final String postTown;

    @JsonCreator
    public Street(@JsonProperty("main_street") String mainStreet,
                  @JsonProperty("post_town") String postTown) {
        this.mainStreet = mainStreet;
        this.postTown = postTown;
    }

    public String getMainStreet() {
        return mainStreet;
    }

    public String getPostTown() {
        return postTown;
    }
}
