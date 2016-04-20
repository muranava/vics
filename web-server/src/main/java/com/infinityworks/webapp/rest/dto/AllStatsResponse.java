package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonSerialize(as = ImmutableAllStatsResponse.class)
public interface AllStatsResponse {
    List<StatsResponse> topCanvassers();
    List<StatsResponse> topWards();
    List<StatsResponse> topConstituencies();
    List<Object[]> recordContactByDate();
    long totalContacts();
    long canvassedThisWeek();
}
