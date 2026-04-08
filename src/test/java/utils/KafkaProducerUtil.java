package utils;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducerUtil {
    private final org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;

    public KafkaProducerUtil() {
        this(KafkaConfigLoader.getBootstrapServers());
    }

    public KafkaProducerUtil(String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    public Future<RecordMetadata> send(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        return producer.send(record);
    }

    public void sendSync(String topic, String key, String value) throws Exception {
        send(topic, key, value).get();
    }

    public void close() {
        producer.close();
    }
}