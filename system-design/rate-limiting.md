# Rate Limiting

Rate limiting controls how many requests a user or service can make in a given period, protecting systems from abuse.

## Common Algorithms
- **Token Bucket:** Tokens are added at a fixed rate; requests consume tokens.
- **Leaky Bucket:** Requests are processed at a fixed rate; excess is discarded or delayed.
- **Fixed Window:** Counts requests in fixed time windows.

## Java Example: Token Bucket
```java
class TokenBucket {
    private int tokens;
    private final int capacity;
    private final int refillRate;
    private long lastRefill;
    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefill = System.currentTimeMillis();
    }
    public synchronized boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }
    private void refill() {
        long now = System.currentTimeMillis();
        int tokensToAdd = (int)((now - lastRefill) / 1000 * refillRate);
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefill = now;
        }
    }
}
```

## Best Practices
- Choose the right algorithm for your use case
- Handle distributed rate limiting carefully
- Monitor and log rate limit events 