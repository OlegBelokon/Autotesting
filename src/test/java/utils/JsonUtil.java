package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import net.minidev.json.JSONArray;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Применяет параметры из таблицы к исходному JSON (или создаёт новый).
     * Таблица: | jsonPath | value |
     */
    public static String applyParameters(String baseJson, Map<String, String> parameters) throws Exception {
        JsonNode root;
        if (baseJson == null || baseJson.trim().isEmpty()) {
            root = mapper.createObjectNode();
        } else {
            root = mapper.readTree(baseJson);
        }

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String path = entry.getKey();
            String value = entry.getValue();
            setJsonPathValue(root, path, value);
        }

        return mapper.writeValueAsString(root);
    }

    private static void setJsonPathValue(JsonNode root, String path, String value) throws Exception {
        // Разбираем путь: поддерживаем простые точечные пути, например "user.name"
        String[] parts = path.split("\\.");
        JsonNode current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.has(part)) {
                ((ObjectNode) current).set(part, mapper.createObjectNode());
            }
            current = current.get(part);
        }
        String lastKey = parts[parts.length - 1];
        ((ObjectNode) current).put(lastKey, value);
    }

    /**
     * Проверяет, соответствует ли фактический JSON ожидаемым параметрам.
     * @param actualJson строка JSON
     * @param expectedParams карта ожидаемых значений (путь -> значение)
     * @return true если все пути существуют и значения совпадают (как строки)
     */
    public static boolean matchesParameters(String actualJson, Map<String, String> expectedParams) {
        try {
            for (Map.Entry<String, String> entry : expectedParams.entrySet()) {
                String path = entry.getKey();
                String expectedValue = entry.getValue();
                Object actualValue = JsonPath.read(actualJson, path);
                String actualStr = actualValue.toString();
                if (!expectedValue.equals(actualStr)) {
                    return false;
                }
            }
            return true;
        } catch (PathNotFoundException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public static String applyParametersWithJsonPath(String baseJson, Map<String, String> parameters) {
        Configuration config = Configuration.builder()
                .jsonProvider(new JacksonJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();
        DocumentContext doc = JsonPath.using(config).parse(baseJson);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String path = entry.getKey();
            Object value = entry.getValue();
            // Пытаемся преобразовать число/boolean, если нужно
            doc.set(JsonPath.compile(path), value);
        }
        return doc.jsonString();
    }
    /**
     * Загружает JSON из файла.
     * Поддерживает:
     * - абсолютный путь (C:/projects/file.json)
     * - относительный путь от корня classpath (json/order.json)
     * - относительный путь от рабочей директории (./src/test/resources/json/order.json)
     */
    public static String loadJsonFromFile(String filePath) throws Exception {
        // Пробуем сначала как абсолютный путь или путь относительно рабочей директории
        java.nio.file.Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return new String(Files.readAllBytes(path));
        }
        // Пробуем как ресурс classpath
        try (InputStream is = JsonUtil.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException("Файл не найден: " + filePath + " (проверено classpath и файловая система)");
            }
            return new String(is.readAllBytes());
        }
    }
}