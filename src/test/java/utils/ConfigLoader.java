package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties not found in classpath");
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
        String servers = get("kafka.bootstrap.servers");
        if (servers == null) throw new IllegalStateException("kafka.bootstrap.servers not defined");
        return servers;
    }

    public static String getDbUrl() {
        return get("db.url");
    }

    public static String getDbUser() {
        return get("db.user");
    }

    public static String getDbPassword() {
        return get("db.password");
    }

    public static String getConsumerGroupId() {
        return get("kafka.consumer.group.id");
    }
}