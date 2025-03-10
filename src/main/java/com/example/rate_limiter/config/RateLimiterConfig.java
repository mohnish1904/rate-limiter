package com.example.rate_limiter.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Bucket bucket(){

        // greedy refill - refill the tokens as soon as possible
//        Bandwidth limit = Bandwidth.builder().capacity(3).refillGreedy(3, Duration.ofSeconds(5)).build();

        // interval refill - refill whole tokens after a duration
        Bandwidth limitInterval = Bandwidth.builder().capacity(3).refillIntervally(1, Duration.ofSeconds(10)).build();

        return Bucket.builder().addLimit(limitInterval).build();
    }
}
