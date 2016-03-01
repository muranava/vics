package com.infinityworks.webapp.testsupport.builder.upstream;

import com.infinityworks.webapp.paf.dto.Issues;

public class IssuesBuilder {
    private Boolean cost;
    private Boolean control;
    private Boolean safety;

    public static IssuesBuilder issues() {
        return new IssuesBuilder().withDefaults();
    }

    public IssuesBuilder withDefaults() {
        withControl(false)
                .withCost(true)
                .withSafety(false);
        return this;
    }

    public IssuesBuilder withCost(Boolean cost) {
        this.cost = cost;
        return this;
    }

    public IssuesBuilder withControl(Boolean control) {
        this.control = control;
        return this;
    }

    public IssuesBuilder withSafety(Boolean safety) {
        this.safety = safety;
        return this;
    }

    public Issues build() {
        return new Issues(cost, control, safety);
    }
}