package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestApiUtil {
    private static final String BASE_URL = ConfigLoader.get("rest.base.url");

    static {
        RestAssured.baseURI = BASE_URL;
    }

    // Замена плейсхолдеров <ключ> в URL на значения из map
    public static String resolveUrl(String endpointName, Map<String, String> pathParams) {
        String urlTemplate = ConfigLoader.get("rest.endpoint." + endpointName);
        if (urlTemplate == null) {
            throw new IllegalArgumentException("Эндпоинт не найден: " + endpointName);
        }
        String resolved = urlTemplate;
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            String placeholder = "<" + entry.getKey() + ">";
            resolved = resolved.replace(placeholder, entry.getValue());
        }
        if (resolved.matches(".*<[^>]+>.*")) {
            throw new IllegalArgumentException("В URL остались незаменённые плейсхолдеры: " + resolved);
        }
        return resolved;
    }

    // GET с path-параметрами
    public static Response sendGet(String endpointName, Map<String, String> pathParams) {
        String url = resolveUrl(endpointName, pathParams);
        return given().when().get(url);
    }

    // POST/PUT/DELETE на полный URL (без замены плейсхолдеров)
    public static Response sendPostToUrl(String url, String bodyJson) {
        return given()
                .header("Content-Type", "application/json")
                .body(bodyJson)
                .when().post(url);
    }

    public static Response sendPutToUrl(String url, String bodyJson) {
        return given()
                .header("Content-Type", "application/json")
                .body(bodyJson)
                .when().put(url);
    }

    public static Response sendDeleteToUrl(String url) {
        return given().when().delete(url);
    }

    // POST/PUT/DELETE с path-параметрами (строят URL из endpointName)
    public static Response sendPost(String endpointName, Map<String, String> pathParams, String bodyJson) {
        String url = resolveUrl(endpointName, pathParams);
        return sendPostToUrl(url, bodyJson);
    }

    public static Response sendPut(String endpointName, Map<String, String> pathParams, String bodyJson) {
        String url = resolveUrl(endpointName, pathParams);
        return sendPutToUrl(url, bodyJson);
    }

    public static Response sendDelete(String endpointName, Map<String, String> pathParams) {
        String url = resolveUrl(endpointName, pathParams);
        return sendDeleteToUrl(url);
    }
}