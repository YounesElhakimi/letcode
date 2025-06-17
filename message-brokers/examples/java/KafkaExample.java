import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Comprehensive Kafka Example
 * Demonstrates:
 * 1. Basic producer and consumer
 * 2. Error handling
 * 3. Message batching
 * 4. Consumer groups
 * 5. Manual offset management
 */
public class KafkaExample {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "example-topic";
    private static final String GROUP_ID = "example-group";

    public static void main(String[] args) {
        // Start producer and consumer in separate threads
        Thread producerThread = new Thread(() -> runProducer());
        Thread consumerThread = new Thread(() -> runConsumer());

        producerThread.start();
        consumerThread.start();
    }

    /**
     * Kafka Producer Example
     */
    private static void runProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Producer reliability settings
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        
        // Performance settings
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        
        // Exactly-once semantics
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "example-transactional-id");

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            // Start transaction
            producer.initTransactions();
            
            for (int i = 0; i < 10; i++) {
                try {
                    producer.beginTransaction();
                    
                    // Create message
                    String key = "key-" + i;
                    String value = "value-" + i;
                    
                    // Send message
                    ProducerRecord<String, String> record = 
                        new ProducerRecord<>(TOPIC, key, value);
                    
                    Future<RecordMetadata> future = producer.send(record, 
                        (metadata, exception) -> {
                            if (exception != null) {
                                System.err.println("Error sending message: " + exception.getMessage());
                            } else {
                                System.out.printf("Message sent to partition %d, offset %d%n",
                                    metadata.partition(), metadata.offset());
                            }
                        });
                    
                    // Wait for the message to be sent
                    RecordMetadata metadata = future.get();
                    
                    // Commit transaction
                    producer.commitTransaction();
                    
                    // Simulate some processing time
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    // Abort transaction on error
                    producer.abortTransaction();
                    System.err.println("Error in producer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Kafka Consumer Example
     */
    private static void runConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        // Consumer reliability settings
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        // Performance settings
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        
        // Exactly-once semantics
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // Subscribe to topic
            consumer.subscribe(Collections.singletonList(TOPIC));
            
            while (true) {
                try {
                    // Poll for messages
                    ConsumerRecords<String, String> records = 
                        consumer.poll(Duration.ofMillis(100));
                    
                    // Process records
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf("Received message: key = %s, value = %s, " +
                            "partition = %d, offset = %d%n",
                            record.key(), record.value(),
                            record.partition(), record.offset());
                        
                        // Process message
                        processMessage(record);
                    }
                    
                    // Commit offsets after processing
                    consumer.commitSync();
                    
                } catch (Exception e) {
                    System.err.println("Error in consumer: " + e.getMessage());
                    // Implement retry logic or dead letter queue here
                }
            }
        }
    }

    /**
     * Process a single message
     */
    private static void processMessage(ConsumerRecord<String, String> record) {
        try {
            // Simulate message processing
            System.out.println("Processing message: " + record.value());
            Thread.sleep(100); // Simulate work
            
            // Simulate occasional errors
            if (Math.random() < 0.1) {
                throw new RuntimeException("Random processing error");
            }
            
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            // Implement error handling (e.g., send to dead letter queue)
        }
    }
} 