package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import utils.JsonUtil;
import utils.RestApiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestSteps {
    private static final Map<String, String> responseStorage = new HashMap<>();
    private Response lastResponse;

    // ======================= GET =======================
    @Given("отправляю GET запрос {string} и сохраняю ответ как {string}")
    public void sendGetAndSave(String endpointName, String saveKey) {
        Response response = RestApiUtil.sendGet(endpointName);
        lastResponse = response;
        String body = response.getBody().asString();
        responseStorage.put(saveKey, body);
        System.out.println("[REST] GET " + endpointName + " -> статус: " + response.getStatusCode());
    }

    // ======================= POST =======================
    @Given("отправляю POST запрос на {string} из файла {string} с параметрами")
    public void sendPostFromFile(String endpointName, String filePath, DataTable table) throws Exception {
        Map<String, String> params = toMap(table);
        String baseJson = JsonUtil.loadJsonFromFile(filePath);
        String finalJson = JsonUtil.applyParameters(baseJson, params);
        Response response = RestApiUtil.sendPost(endpointName, finalJson);
        lastResponse = response;
        System.out.println("[REST] POST " + endpointName + " -> статус: " + response.getStatusCode());
    }

    // ======================= PUT =======================
    @Given("отправляю PUT запрос на {string} из файла {string} с параметрами")
    public void sendPutFromFile(String endpointName, String filePath, DataTable table) throws Exception {
        Map<String, String> params = toMap(table);
        String baseJson = JsonUtil.loadJsonFromFile(filePath);
        String finalJson = JsonUtil.applyParameters(baseJson, params);
        Response response = RestApiUtil.sendPut(endpointName, finalJson);
        lastResponse = response;
        System.out.println("[REST] PUT " + endpointName + " -> статус: " + response.getStatusCode());
    }

    // ======================= Проверка ответа =======================
    @Then("проверяю что ответ {string} содержит параметры")
    public void verifyResponseContainsParams(String responseKey, DataTable table) {
        String responseBody = responseStorage.get(responseKey);
        if (responseBody == null && lastResponse != null) {
            responseBody = lastResponse.getBody().asString();
        }
        if (responseBody == null) {
            throw new IllegalArgumentException("Ответ с ключом '" + responseKey + "' не найден.");
        }
        Map<String, String> expectedParams = toMap(table);
        boolean match = JsonUtil.matchesParameters(responseBody, expectedParams);
        assertTrue(match, "Ответ не содержит ожидаемые параметры: " + expectedParams + "\nОтвет: " + responseBody);
    }

    // Вспомогательный метод для преобразования DataTable в Map
    private Map<String, String> toMap(DataTable table) {
        Map<String, String> map = new HashMap<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> row : rows) {
            if (row.size() >= 2) {
                map.put(row.get(0), row.get(1));
            }
        }
        return map;
    }
}