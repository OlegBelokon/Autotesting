package utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerUtil {
    private final KafkaConsumer<String, String> consumer;
    private final List<String> consumedMessages = new ArrayList<>();

    public KafkaConsumerUtil(String topic) {
        this(ConfigLoader.getBootstrapServers(), ConfigLoader.getConsumerGroupId(), topic);
    }

    public KafkaConsumerUtil(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "true");

        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public List<String> pollMessages(Duration timeout) {
        ConsumerRecords<String, String> records = consumer.poll(timeout);
        List<String> messages = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            messages.add(record.value());
            consumedMessages.add(record.value());
        }
        return messages;
    }

    public List<String> getAllMessagesSinceSubscribe() {
        return new ArrayList<>(consumedMessages);
    }

    public void clear() {
        consumedMessages.clear();
    }

    public void close() {
        consumer.close();
    }
}