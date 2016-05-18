package com.infinityworks.webapp.domain;

import com.infinityworks.webapp.clients.paf.dto.ConstituencyStats;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(init = "with*")
public interface ConstituencyReport {
    String constituencyCode();
    String constituencyName();
    ConstituencyStats stats();
}
