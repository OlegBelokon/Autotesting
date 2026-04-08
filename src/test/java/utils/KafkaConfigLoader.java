package utils;

import java.io.InputStream;
import java.util.Properties;

public class KafkaConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String getBootstrapServers() {
        return get("kafka.bootstrap.servers");
    }

    public static String getConsumerGroupId() {
        return get("kafka.consumer.group.id");
    }
}