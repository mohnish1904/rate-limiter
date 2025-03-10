package com.example.rate_limiter.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession(true);

        String appKey = "some-app-key"; // here we can define custom logic for getting app key

        Bucket bucket = (Bucket) session.getAttribute("throttler-" + appKey);
        if (bucket == null) {
            bucket = rateLimiterConfig.bucket();
            session.setAttribute("throttler-" + appKey, bucket);
        }

        if (rateLimiterConfig.bucket().tryConsume(1)){
            filterChain.doFilter(servletRequest, servletResponse); // Pass the request, when not rate limited
        } else
            ((HttpServletResponse) servletResponse).setStatus(429); // Exception for too much request
    }

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // init bucket registry
//        buckets = Bucket4jJCache
//                .entryProcessorBasedBuilder(cache)
//                .build();
//    }
}
