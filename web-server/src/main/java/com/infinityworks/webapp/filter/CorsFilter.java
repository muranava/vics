package com.infinityworks.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class CorsFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);
    private final AllowedHosts allowedHosts;

    @Autowired
    public CorsFilter(AllowedHosts allowedHosts) {
        this.allowedHosts = allowedHosts;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestOrigin = request.getHeader("Origin");

        if (isHostAllowed(requestOrigin)) {
                    response.setHeader("Access-Control-Allow-Origin", requestOrigin);
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, X-Requested-With");

        chain.doFilter(req, res);
    }

    private boolean isHostAllowed(String origin) {
        if (allowedHosts.getHosts().contains("*") ||
            allowedHosts.getHosts().contains(origin)) {
            return true;
        } else {
            log.debug("Request failed CORS filter. origin={}, allowed={}", origin, allowedHosts);
            return false;
        }
    }

    @Override
    public void destroy() {

    }
}