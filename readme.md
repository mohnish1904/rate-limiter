# Rate Limiting 

Rate limiting is a technique used in computing to control the amount of incoming or outgoing traffic to or from a network. application or an API.
This practice is vital for ensuring the stability security, and performance of systems that handles large volumes of requests, particularly in environments where multiple users interact with the system concurrently.

Here I have tried to put a way to create a rate limiter in spring boot using ***Bucket-4j***.

## Using plain java

Before that to understand the concept of rate limiter using plain java, there is an implementation at
>java/com/example/rate_limiter/standalone/RateLimiter.java

For creating a rate limiter we have to take following things into consideration.

1. A data structure to hold the record of users and their corresponding requests (here we used hashmap).
2. Updating the record data structure with new timestamp every time a new request come.
3. Clearing out the timestamp records which exceeds the timeout.
4. Check if the record count is less than the limiter count.

````
-> This hashmap is for storing the timestamp records for particular user
HashMap<String, List<Long>> requestRecord = new HashMap<>();

-> Logic to implement the Rate limitier.
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
````

## Using Bucket-4j

For detail explanation refer official documentation - 
>https://bucket4j.com/8.14.0/toc.html
> 

