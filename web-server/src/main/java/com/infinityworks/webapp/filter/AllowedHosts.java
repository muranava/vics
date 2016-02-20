package com.infinityworks.webapp.filter;

import com.google.common.base.MoreObjects;

import java.util.Set;

public class AllowedHosts {
    private final Set<String> hosts;

    public AllowedHosts(Set<String> hosts) {
        this.hosts = hosts;
    }

    public Set<String> getHosts() {
        return hosts;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("hosts", hosts)
                .toString();
    }
}
