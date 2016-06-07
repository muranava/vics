package com.infinityworks.pdfserver.pdf.model;

import org.immutables.value.Value.*;

@Immutable
@Style(init = "with*")
public interface ElectorRow {
    @Parameter @Default default String house() { return ""; }
    @Parameter @Default default String street() { return ""; }
    @Parameter @Default default String name() { return ""; }
    @Parameter @Default default String telephone() { return ""; }
    @Parameter @Default default String likelihood() { return ""; }
    @Parameter @Default default String issue1() { return ""; }
    @Parameter @Default default String issue2() { return ""; }
    @Parameter @Default default String issue3() { return ""; }
    @Parameter @Default default String support() { return ""; }
    @Parameter @Default default String hasVoted() { return ""; }
    @Parameter @Default default String hasPV() { return ""; }
    @Parameter @Default default String wantsPV() { return ""; }
    @Parameter @Default default String needsLift() { return ""; }
    @Parameter @Default default String poster() { return ""; }
    @Parameter @Default default String deceased() { return ""; }
    @Parameter @Default default String ern() { return ""; }
    @Parameter @Default default String inaccessible() { return ""; }
    @Parameter @Default default String email() { return ""; }
}
