# RabbitMQ Guide

## Architecture Overview

### Core Components
1. **Exchange**
   - Receives messages from producers
   - Routes messages to queues
   - Types: Direct, Fanout, Topic, Headers

2. **Queue**
   - Stores messages
   - Delivers to consumers
   - Can be durable or transient

3. **Binding**
   - Links exchanges to queues
   - Defines routing rules
   - Uses routing keys

4. **Producer**
   - Publishes messages to exchanges
   - Can specify routing keys
   - Handles acknowledgments

5. **Consumer**
   - Subscribes to queues
   - Processes messages
   - Sends acknowledgments

### Key Concepts

#### Message Flow
1. Producer → Exchange
2. Exchange → Queue (via binding)
3. Queue → Consumer
4. Consumer → Acknowledgment

#### Exchange Types
1. **Direct Exchange**
   - Exact routing key match
   - One-to-one routing
   - Simple routing logic

2. **Fanout Exchange**
   - Broadcast to all queues
   - No routing key needed
   - One-to-many routing

3. **Topic Exchange**
   - Pattern-based routing
   - Wildcard support
   - Many-to-many routing

4. **Headers Exchange**
   - Header-based routing
   - Complex matching rules
   - Flexible routing

## Best Practices

### 1. Queue Design
- **Naming Convention**
  ```
  <environment>.<service>.<purpose>
  Example: prod.orders.notifications
  ```

- **Queue Properties**
  ```java
  Map<String, Object> args = new HashMap<>();
  args.put("x-message-ttl", 60000);           // Message TTL
  args.put("x-max-length", 10000);            // Max queue length
  args.put("x-dead-letter-exchange", "dlx");  // Dead letter exchange
  args.put("x-dead-letter-routing-key", "dlq"); // DLQ routing key
  ```

### 2. Producer Configuration
```java
ConnectionFactory factory = new ConnectionFactory();
factory.setHost("localhost");
factory.setPort(5672);
factory.setUsername("guest");
factory.setPassword("guest");
factory.setVirtualHost("/");

// Enable publisher confirms
factory.setPublisherConfirms(true);

// Enable publisher returns
factory.setPublisherReturns(true);

// Set heartbeat
factory.setRequestedHeartbeat(30);
```

### 3. Consumer Configuration
```java
// Basic consumer setup
channel.basicQos(1);  // Prefetch count
channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                             AMQP.BasicProperties properties, byte[] body) {
        try {
            // Process message
            processMessage(body);
            // Acknowledge message
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            // Negative acknowledge and requeue
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        }
    }
});
```

### 4. Error Handling
- Implement dead letter queues
- Use message TTL
- Handle connection failures
- Implement retry mechanisms

### 5. Performance Tuning
- Optimize prefetch count
- Use message batching
- Configure appropriate memory limits
- Monitor queue sizes

## Common Patterns

### 1. Dead Letter Queue
```java
// Create DLX
channel.exchangeDeclare("dlx", "direct", true);

// Create DLQ
channel.queueDeclare("dlq", true, false, false, null);
channel.queueBind("dlq", "dlx", "dlq");

// Create main queue with DLX
Map<String, Object> args = new HashMap<>();
args.put("x-dead-letter-exchange", "dlx");
args.put("x-dead-letter-routing-key", "dlq");
channel.queueDeclare("main-queue", true, false, false, args);
```

### 2. Message Persistence
```java
// Producer
AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
    .deliveryMode(2)  // Persistent
    .contentType("application/json")
    .build();

channel.basicPublish("exchange", "routing-key", props, message.getBytes());

// Consumer
channel.queueDeclare("queue", true, false, false, null);  // Durable queue
```

### 3. Message Acknowledgment
```java
// Producer
channel.confirmSelect();  // Enable publisher confirms
channel.basicPublish("exchange", "routing-key", null, message.getBytes());
channel.waitForConfirmsOrDie(5000);  // Wait for confirmation

// Consumer
channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                             AMQP.BasicProperties properties, byte[] body) {
        try {
            processMessage(body);
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        }
    }
});
```

## Anti-patterns to Avoid

1. **Large Messages**
   - Increased memory usage
   - Higher latency
   - Network congestion

2. **Infinite Queues**
   - Memory issues
   - Performance degradation
   - Recovery problems

3. **No Message TTL**
   - Queue growth
   - Resource consumption
   - Stale messages

4. **No Error Handling**
   - Message loss
   - System instability
   - Debugging difficulties

## Monitoring and Maintenance

### Key Metrics
1. **Queue Metrics**
   - Message count
   - Consumer count
   - Memory usage
   - Disk usage

2. **Connection Metrics**
   - Active connections
   - Channel count
   - Connection errors
   - Heartbeat status

3. **Performance Metrics**
   - Message rate
   - Acknowledgment rate
   - Error rate
   - Memory usage

### Maintenance Tasks
1. **Regular Checks**
   - Monitor queue sizes
   - Check connection status
   - Verify bindings
   - Review policies

2. **Cleanup**
   - Remove unused queues
   - Clean up old messages
   - Archive logs
   - Update configurations

## Security

### 1. Authentication
- Username/password
- SSL/TLS
- SASL
- LDAP

### 2. Authorization
- Virtual hosts
- User permissions
- Resource limits
- Access control

### 3. Encryption
- SSL/TLS
- Message encryption
- Password hashing
- Secure connections

## Deployment Considerations

### 1. Clustering
- Node configuration
- Cluster formation
- Load balancing
- High availability

### 2. Network
- Port configuration
- Firewall rules
- Network isolation
- Load balancing

### 3. Monitoring
- Management plugin
- Prometheus metrics
- Grafana dashboards
- Alerting

## Resources
- [Official Documentation](https://www.rabbitmq.com/documentation.html)
- [Management Plugin](https://www.rabbitmq.com/management.html)
- [Clustering Guide](https://www.rabbitmq.com/clustering.html)
- [Security Guide](https://www.rabbitmq.com/security.html) 