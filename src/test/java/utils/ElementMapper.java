import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import java.util.Map;

public class ElementMapper {
    private static final Map<String, String> map;

    static {
        try (InputStream input = ElementMapper.class.getClassLoader().getResourceAsStream("elements.yaml")) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            map = mapper.readValue(input, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки elements.yaml", e);
        }
    }

    public static String getSelector(String elementName) {
        String selector = map.get(elementName.toLowerCase());
        if (selector == null) {
            throw new IllegalArgumentException("Элемент не найден: " + elementName);
        }
        return selector;
    }
}