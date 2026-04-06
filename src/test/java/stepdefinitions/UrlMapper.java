package stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import java.util.Map;

public class UrlMapper {
    private static final Map<String, String> urls;

    static {
        try (InputStream input = UrlMapper.class.getClassLoader().getResourceAsStream("elements.yaml")) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            Map<String, Object> root = mapper.readValue(input, Map.class);
            urls = (Map<String, String>) root.get("urls");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load URLs", e);
        }
    }

    public static String getUrl(String name) {
        String url = urls.get(name);
        if (url == null) {
            throw new IllegalArgumentException("URL not found: " + name);
        }
        return url;
    }
}