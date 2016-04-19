package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableVoterAddress.class)
@JsonSerialize(as = ImmutableVoterAddress.class)
public interface VoterAddress {
    @Nullable @JsonProperty("sub_building_name") String subBuildingName();
    @Nullable @JsonProperty("building_name") String buildingName();
    @Nullable @JsonProperty("building_number") String buildingNumber();
    @Nullable @JsonProperty("dependent_street") String dependentStreet();
    @Nullable @JsonProperty("main_street") String mainStreet();
    @Nullable @JsonProperty("dependent_locality") String dependentLocality();
    @Nullable @JsonProperty("post_town") String postTown();
    @Nullable @JsonProperty("postcode") String postcode();
    @Nullable @JsonProperty("latitude_etrs89") Double latitude();
    @Nullable @JsonProperty("longitude_etrs89") Double longitude();
    @Nullable @JsonProperty("parliamentary_constituency_code") String constituencyCode();
    @Nullable @JsonProperty("parliamentary_constituency_name") String constituencyName();
    @Nullable @JsonProperty("electoral_ward_code") String wardCode();
    @Nullable @JsonProperty("electoral_ward_name") String wardName();
}
