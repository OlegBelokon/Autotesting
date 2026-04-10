package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestApiUtil {
    private static final String BASE_URL = ConfigLoader.get("rest.base.url");

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public static Response sendGet(String endpointName, Object... pathParams) {
        String endpoint = ConfigLoader.get("rest.endpoint." + endpointName);
        if (endpoint == null) {
            throw new IllegalArgumentException("Эндпоинт не найден: " + endpointName);
        }
        RequestSpecification request = given();
        if (pathParams.length > 0) {
            request.pathParams(buildPathParams(pathParams));
        }
        return request.when().get(endpoint);
    }

    public static Response sendPost(String endpointName, String bodyJson, Object... pathParams) {
        String endpoint = ConfigLoader.get("rest.endpoint." + endpointName);
        if (endpoint == null) {
            throw new IllegalArgumentException("Эндпоинт не найден: " + endpointName);
        }
        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body(bodyJson);
        if (pathParams.length > 0) {
            request.pathParams(buildPathParams(pathParams));
        }
        return request.when().post(endpoint);
    }

    public static Response sendPut(String endpointName, String bodyJson, Object... pathParams) {
        String endpoint = ConfigLoader.get("rest.endpoint." + endpointName);
        if (endpoint == null) {
            throw new IllegalArgumentException("Эндпоинт не найден: " + endpointName);
        }
        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body(bodyJson);
        if (pathParams.length > 0) {
            request.pathParams(buildPathParams(pathParams));
        }
        return request.when().put(endpoint);
    }

    private static java.util.Map<String, Object> buildPathParams(Object... params) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        for (int i = 0; i < params.length; i += 2) {
            if (i + 1 < params.length) {
                map.put(params[i].toString(), params[i + 1]);
            }
        }
        return map;
    }
}