# Database Sharding

Database sharding is the process of splitting a large database into smaller, more manageable pieces called shards, each holding a subset of the data.

## Sharding Strategies
- **Horizontal Sharding:** Rows are distributed across shards.
- **Vertical Sharding:** Columns are distributed across shards.
- **Directory-Based Sharding:** Uses a lookup table to find the shard for a given key.

## Java Example: Simple Shard Selection
```java
class ShardManager {
    private final int numShards;
    public ShardManager(int numShards) { this.numShards = numShards; }
    public int getShardId(String key) {
        return Math.abs(key.hashCode()) % numShards;
    }
}
```

## Best Practices
- Choose a good sharding key
- Plan for resharding
- Monitor shard health and balance 