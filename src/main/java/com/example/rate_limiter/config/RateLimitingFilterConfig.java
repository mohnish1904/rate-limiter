package com.example.rate_limiter.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitingFilterConfig implements Filter {

    private final RateLimiterConfig rateLimiterConfig;

    public RateLimitingFilterConfig(RateLimiterConfig rateLimiterConfig) {
        this.rateLimiterConfig = rateLimiterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        // get http session
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession(true);

        // here we can define custom logic for getting app key
        String appKey = "some-app-key";


        Bucket bucket = (Bucket) session.getAttribute("throttler-" + appKey);

        if (bucket == null) {
            bucket = rateLimiterConfig.bucket();
            session.setAttribute("throttler-" + appKey, bucket);
        }

        if (rateLimiterConfig.bucket().tryConsume(1)){
            filterChain.doFilter(servletRequest, servletResponse); // Pass the request, when not rate limited
        } else
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // Exception for too much request
    }
}
