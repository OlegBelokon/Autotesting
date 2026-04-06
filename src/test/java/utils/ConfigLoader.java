package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("api.properties")) {
            if (input == null) {
                throw new RuntimeException("api.properties not found in classpath");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load api.properties", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}