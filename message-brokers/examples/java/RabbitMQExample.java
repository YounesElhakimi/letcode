import com.rabbitmq.client.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Comprehensive RabbitMQ Example
 * Demonstrates:
 * 1. Basic producer and consumer
 * 2. Different exchange types
 * 3. Message persistence
 * 4. Dead letter queues
 * 5. Error handling
 */
public class RabbitMQExample {
    private static final String HOST = "localhost";
    private static final int PORT = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String VIRTUAL_HOST = "/";

    // Exchange names
    private static final String DIRECT_EXCHANGE = "direct-exchange";
    private static final String FANOUT_EXCHANGE = "fanout-exchange";
    private static final String TOPIC_EXCHANGE = "topic-exchange";
    private static final String DLX = "dlx";

    // Queue names
    private static final String DIRECT_QUEUE = "direct-queue";
    private static final String FANOUT_QUEUE_1 = "fanout-queue-1";
    private static final String FANOUT_QUEUE_2 = "fanout-queue-2";
    private static final String TOPIC_QUEUE = "topic-queue";
    private static final String DLQ = "dlq";

    public static void main(String[] args) {
        try {
            // Start producer and consumer in separate threads
            Thread producerThread = new Thread(() -> runProducer());
            Thread consumerThread = new Thread(() -> runConsumer());

            producerThread.start();
            consumerThread.start();
        } catch (Exception e) {
            System.err.println("Error in main: " + e.getMessage());
        }
    }

    /**
     * RabbitMQ Producer Example
     */
    private static void runProducer() {
        try {
            // Create connection factory
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            factory.setPort(PORT);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);
            factory.setVirtualHost(VIRTUAL_HOST);

            // Enable publisher confirms
            factory.setPublisherConfirms(true);
            factory.setPublisherReturns(true);

            // Create connection and channel
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                // Declare exchanges
                channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT, true);
                channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT, true);
                channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC, true);
                channel.exchangeDeclare(DLX, BuiltinExchangeType.DIRECT, true);

                // Declare queues with DLX
                Map<String, Object> args = new HashMap<>();
                args.put("x-dead-letter-exchange", DLX);
                args.put("x-dead-letter-routing-key", "dlq");

                channel.queueDeclare(DIRECT_QUEUE, true, false, false, args);
                channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, args);
                channel.queueDeclare(FANOUT_QUEUE_2, true, false, false, args);
                channel.queueDeclare(TOPIC_QUEUE, true, false, false, args);
                channel.queueDeclare(DLQ, true, false, false, null);

                // Bind queues to exchanges
                channel.queueBind(DIRECT_QUEUE, DIRECT_EXCHANGE, "direct-key");
                channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHANGE, "");
                channel.queueBind(FANOUT_QUEUE_2, FANOUT_EXCHANGE, "");
                channel.queueBind(TOPIC_QUEUE, TOPIC_EXCHANGE, "topic.#");
                channel.queueBind(DLQ, DLX, "dlq");

                // Enable publisher confirms
                channel.confirmSelect();

                // Add confirm listener
                channel.addConfirmListener(new ConfirmListener() {
                    @Override
                    public void handleAck(long deliveryTag, boolean multiple) {
                        System.out.println("Message confirmed: " + deliveryTag);
                    }

                    @Override
                    public void handleNack(long deliveryTag, boolean multiple) {
                        System.err.println("Message nacked: " + deliveryTag);
                    }
                });

                // Add return listener
                channel.addReturnListener(returnMessage -> {
                    System.err.println("Message returned: " + new String(returnMessage.getBody()));
                });

                // Send messages
                for (int i = 0; i < 10; i++) {
                    String message = "Message " + i;

                    // Send to direct exchange
                    sendMessage(channel, DIRECT_EXCHANGE, "direct-key", message);

                    // Send to fanout exchange
                    sendMessage(channel, FANOUT_EXCHANGE, "", message);

                    // Send to topic exchange
                    sendMessage(channel, TOPIC_EXCHANGE, "topic.message", message);

                    // Simulate some processing time
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in producer: " + e.getMessage());
        }
    }

    /**
     * Send a message with persistence and error handling
     */
    private static void sendMessage(Channel channel, String exchange, String routingKey, String message) 
            throws IOException {
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
            .deliveryMode(2)  // Persistent
            .contentType("text/plain")
            .build();

        try {
            channel.basicPublish(exchange, routingKey, true, props, message.getBytes());
            System.out.println("Sent message: " + message);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    /**
     * RabbitMQ Consumer Example
     */
    private static void runConsumer() {
        try {
            // Create connection factory
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            factory.setPort(PORT);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);
            factory.setVirtualHost(VIRTUAL_HOST);

            // Create connection and channel
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                // Set prefetch count
                channel.basicQos(1);

                // Create consumer
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    try {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println("Received message: " + message);

                        // Process message
                        processMessage(message);

                        // Acknowledge message
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (Exception e) {
                        // Negative acknowledge and requeue
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                        System.err.println("Error processing message: " + e.getMessage());
                    }
                };

                // Start consuming from all queues
                channel.basicConsume(DIRECT_QUEUE, false, deliverCallback, consumerTag -> {});
                channel.basicConsume(FANOUT_QUEUE_1, false, deliverCallback, consumerTag -> {});
                channel.basicConsume(FANOUT_QUEUE_2, false, deliverCallback, consumerTag -> {});
                channel.basicConsume(TOPIC_QUEUE, false, deliverCallback, consumerTag -> {});
                channel.basicConsume(DLQ, false, deliverCallback, consumerTag -> {});

                // Keep consumer running
                while (true) {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in consumer: " + e.getMessage());
        }
    }

    /**
     * Process a single message
     */
    private static void processMessage(String message) throws Exception {
        // Simulate message processing
        System.out.println("Processing message: " + message);
        Thread.sleep(100); // Simulate work

        // Simulate occasional errors
        if (Math.random() < 0.1) {
            throw new RuntimeException("Random processing error");
        }
    }
} 