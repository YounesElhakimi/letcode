# Message Queues

Message queues enable asynchronous communication between different parts of a system by passing messages through a queue.

## Common Patterns
- **Producer-Consumer:** Producers send messages; consumers process them.
- **Work Queues:** Distribute tasks among multiple workers.

## Java Example: Simple Producer-Consumer
```java
import java.util.concurrent.*;
class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        Thread producer = new Thread(() -> {
            try { queue.put(1); } catch (InterruptedException e) {}
        });
        Thread consumer = new Thread(() -> {
            try { queue.take(); } catch (InterruptedException e) {}
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
```

## Best Practices
- Ensure message durability
- Handle retries and failures
- Monitor queue length and latency 