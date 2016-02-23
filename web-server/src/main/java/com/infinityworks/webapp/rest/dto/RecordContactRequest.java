package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordContactRequest {

    @NotEmpty
    private final String ern;
    @NotNull
    @Min(1)
    @Max(5)
    private final Integer intention;
    @NotNull
    @Min(1)
    @Max(5)
    private final Integer likelihood;
    @NotNull
    private final Boolean cost;
    @NotNull
    private final Boolean sovereignty;
    @NotNull
    private final Boolean border;
    @NotNull
    private final Boolean lift;
    @NotNull
    private final Boolean hasPV;
    @NotNull
    private final Boolean wantsPV;
    @NotNull
    private final Boolean deceased;
    @NotNull
    private final Boolean poster;
    @NotEmpty
    private final String wardCode;

    @JsonCreator
    public RecordContactRequest(@JsonProperty("ern") String ern,
                                @JsonProperty("intention") Integer intention,
                                @JsonProperty("likelihood") Integer likelihood,
                                @JsonProperty("cost") Boolean cost,
                                @JsonProperty("sovereignty") Boolean sovereignty,
                                @JsonProperty("border") Boolean border,
                                @JsonProperty("lift") Boolean lift,
                                @JsonProperty("hasPV") Boolean hasPV,
                                @JsonProperty("wantsPV") Boolean wantsPV,
                                @JsonProperty("deceased") Boolean deceased,
                                @JsonProperty("poster") Boolean poster,
                                @JsonProperty("wardCode") String wardCode) {
        this.ern = ern;
        this.intention = intention;
        this.likelihood = likelihood;
        this.cost = cost;
        this.sovereignty = sovereignty;
        this.border = border;
        this.lift = lift;
        this.hasPV = hasPV;
        this.wantsPV = wantsPV;
        this.deceased = deceased;
        this.poster = poster;
        this.wardCode = wardCode;
    }

    public String getErn() {
        return ern;
    }

    public Integer getIntention() {
        return intention;
    }

    public Integer getLikelihood() {
        return likelihood;
    }

    public Boolean getCost() {
        return cost;
    }

    public Boolean getSovereignty() {
        return sovereignty;
    }

    public Boolean getBorder() {
        return border;
    }

    public Boolean getLift() {
        return lift;
    }

    public Boolean getHasPV() {
        return hasPV;
    }

    public Boolean getWantsPV() {
        return wantsPV;
    }

    public Boolean getDeceased() {
        return deceased;
    }

    public Boolean getPoster() {
        return poster;
    }

    public String getWardCode() {
        return wardCode;
    }
}
