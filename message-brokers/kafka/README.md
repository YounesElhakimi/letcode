# Apache Kafka Guide

## Architecture Overview

### Core Components
1. **Broker**
   - Handles message storage and delivery
   - Manages partitions and replication
   - Coordinates with other brokers

2. **Topic**
   - Category/feed name for messages
   - Divided into partitions
   - Can have multiple replicas

3. **Partition**
   - Ordered, immutable sequence of records
   - Allows parallel processing
   - Provides message ordering within partition

4. **Producer**
   - Publishes messages to topics
   - Can specify partition
   - Handles retries and acknowledgments

5. **Consumer**
   - Subscribes to topics
   - Processes messages
   - Maintains offset position

6. **Consumer Group**
   - Group of consumers working together
   - Shares partitions among members
   - Enables parallel processing

### Key Concepts

#### Message Ordering
- Messages within a partition are ordered
- No ordering guarantee across partitions
- Use partition key for related messages

#### Replication
- Each partition has multiple replicas
- One leader, multiple followers
- Automatic leader election
- Fault tolerance

#### Retention
- Time-based retention
- Size-based retention
- Log compaction for key-based retention

## Best Practices

### 1. Topic Design
- **Naming Convention**
  ```
  <environment>.<domain>.<event-type>.<version>
  Example: prod.orders.order-created.v1
  ```

- **Partitioning Strategy**
  - Consider throughput requirements
  - Plan for future growth
  - Use meaningful partition keys

- **Replication Factor**
  - Minimum of 3 for production
  - Consider data center layout
  - Balance availability vs. storage

### 2. Producer Configuration
```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("acks", "all");                    // Strong durability
props.put("retries", 3);                     // Retry failed sends
props.put("batch.size", 16384);              // Batch size
props.put("linger.ms", 1);                   // Wait for more records
props.put("buffer.memory", 33554432);        // Buffer size
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
```

### 3. Consumer Configuration
```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("group.id", "my-group");
props.put("enable.auto.commit", "false");    // Manual commit
props.put("auto.offset.reset", "earliest");  // Start from beginning
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
```

### 4. Error Handling
- Implement retry mechanisms
- Use dead letter queues
- Monitor consumer lag
- Handle deserialization errors

### 5. Performance Tuning
- Optimize batch size
- Configure appropriate buffer sizes
- Tune fetch size
- Monitor and adjust partition count

## Common Patterns

### 1. Exactly-Once Processing
```java
// Producer
props.put("enable.idempotence", "true");
props.put("transactional.id", "my-transactional-id");

// Consumer
props.put("isolation.level", "read_committed");
```

### 2. Dead Letter Queue
```java
// Create DLQ topic
kafka-topics.sh --create --topic orders-dlq \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 3

// Consumer with DLQ handling
try {
    processMessage(record);
} catch (Exception e) {
    sendToDLQ(record, e);
}
```

### 3. Message Schema Evolution
- Use Avro or Protobuf
- Implement schema registry
- Handle backward/forward compatibility
- Version your schemas

## Anti-patterns to Avoid

1. **Too Many Partitions**
   - Increased overhead
   - Higher latency
   - Resource contention

2. **Small Messages**
   - Inefficient network usage
   - Increased overhead
   - Higher latency

3. **Infinite Retention**
   - Storage costs
   - Performance impact
   - Recovery time

4. **Single Consumer Group**
   - Limited scalability
   - Single point of failure
   - Processing bottlenecks

## Monitoring and Maintenance

### Key Metrics
1. **Broker Metrics**
   - CPU usage
   - Memory usage
   - Disk I/O
   - Network I/O

2. **Topic Metrics**
   - Messages per second
   - Bytes per second
   - Partition count
   - Replication factor

3. **Consumer Metrics**
   - Consumer lag
   - Processing time
   - Error rate
   - Rebalance events

### Maintenance Tasks
1. **Regular Checks**
   - Monitor disk usage
   - Check replication status
   - Verify consumer groups
   - Review topic configurations

2. **Cleanup**
   - Remove old topics
   - Clean up temporary files
   - Archive old logs
   - Update configurations

## Security

### 1. Authentication
- SSL/TLS
- SASL
- Kerberos
- OAuth2

### 2. Authorization
- ACLs
- Role-based access
- Topic-level permissions
- Operation-level permissions

### 3. Encryption
- Data in transit
- Data at rest
- Message encryption
- Key management

## Deployment Considerations

### 1. Hardware
- Fast disks (SSD)
- Sufficient memory
- Network capacity
- CPU cores

### 2. Network
- Low latency
- High bandwidth
- Network isolation
- Load balancing

### 3. Monitoring
- Prometheus
- Grafana
- Alerting
- Logging

## Resources
- [Official Documentation](https://kafka.apache.org/documentation/)
- [Confluent Platform](https://docs.confluent.io/platform/current/platform.html)
- [Kafka Operations](https://kafka.apache.org/documentation/#operations)
- [Kafka Security](https://kafka.apache.org/documentation/#security) 