package com.example.rate_limiter.standalone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class RateLimiter {

    /**
     *  For implementing rate limiter, there are following considerations :
     *  1. Hashmap for maintaining records
     *  2. Synchronized to avoid race conditions
     *  3. Checking the timeout
     *  4. Removing/clearing the records post timeout
     */

    private static final HashMap<String, List<Long>> requestRecord = new HashMap<>();
    private static final Integer LIMIT_COUNT = 3;
    private static final long LIMIT_TIMEOUT = 10000;

    public synchronized Boolean isRequestAccepted(String userId) {

        // check if userId present in hashmap
        if (requestRecord.containsKey(userId)){

            List<Long> newTimestampList = new ArrayList<>(requestRecord.get(userId));

            newTimestampList.add(System.currentTimeMillis());

            newTimestampList = newTimestampList.stream()
                    .filter(timestamp -> System.currentTimeMillis() - timestamp < LIMIT_TIMEOUT)
                    .collect(Collectors.toList());
            requestRecord.put(userId, newTimestampList);

            return newTimestampList.size() <= LIMIT_COUNT;
        } else {
            requestRecord.put(userId, List.of(System.currentTimeMillis()));
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        RateLimiter rateLimiter = new RateLimiter();

        System.out.println(rateLimiter.isRequestAccepted("user1")); // true
        System.out.println(rateLimiter.isRequestAccepted("user1")); // true
        System.out.println(rateLimiter.isRequestAccepted("user1")); // true
        System.out.println(rateLimiter.isRequestAccepted("user1")); // false
        System.out.println(rateLimiter.isRequestAccepted("user1")); // false
        System.out.println(rateLimiter.isRequestAccepted("user2")); // true

        sleep(LIMIT_TIMEOUT+100);
        System.out.println(rateLimiter.isRequestAccepted("user1")); // true

    }
}
