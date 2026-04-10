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
    private static final Map<String, String> urlStorage = new HashMap<>();
    private static final Map<String, Response> responseStorage = new HashMap<>();
    private Response lastResponse;

    // ======================= GET =======================
    @Given("отправляю GET запрос {string} с параметрами пути и сохраняю ответ как {string}")
    public void sendGetWithPathParams(String endpointName, DataTable pathParamsTable, String saveKey) {
        Map<String, String> pathParams = toMap(pathParamsTable);
        Response response = RestApiUtil.sendGet(endpointName, pathParams);
        lastResponse = response;
        responseStorage.put(saveKey, response);
        System.out.println("[REST] GET " + endpointName + " -> статус: " + response.getStatusCode());
    }

    // ======================= POST =======================
    @Given("сохраняю URL как {string} для POST запроса {string} с параметрами пути")
    public void saveUrlForPost(String urlKey, String endpointName, DataTable pathParamsTable) {
        Map<String, String> pathParams = toMap(pathParamsTable);
        String url = RestApiUtil.resolveUrl(endpointName, pathParams);
        urlStorage.put(urlKey, url);
        System.out.println("[REST] Сохранён URL для POST: " + url);
    }

    @Given("отправляю POST запрос по сохранённому URL {string} из файла {string} с параметрами JSON")
    public void sendPostBySavedUrl(String urlKey, String jsonFilePath, DataTable jsonParamsTable) throws Exception {
        String url = urlStorage.get(urlKey);
        if (url == null) {
            throw new IllegalArgumentException("URL не найден: " + urlKey);
        }
        Map<String, String> jsonParams = toMap(jsonParamsTable);
        String baseJson = JsonUtil.loadJsonFromFile(jsonFilePath);
        String finalJson = JsonUtil.applyParameters(baseJson, jsonParams);
        Response response = RestApiUtil.sendPostToUrl(url, finalJson);
        lastResponse = response;
        responseStorage.put(urlKey, response);
        System.out.println("[REST] POST " + url + " -> статус: " + response.getStatusCode());
    }

    // POST без предварительного сохранения (когда нет path-параметров)
    @Given("отправляю POST запрос {string} из файла {string} с параметрами JSON")
    public void sendPostDirect(String endpointName, String jsonFilePath, DataTable jsonParamsTable) throws Exception {
        Map<String, String> jsonParams = toMap(jsonParamsTable);
        String baseJson = JsonUtil.loadJsonFromFile(jsonFilePath);
        String finalJson = JsonUtil.applyParameters(baseJson, jsonParams);
        Response response = RestApiUtil.sendPost(endpointName, new HashMap<>(), finalJson);
        lastResponse = response;
        System.out.println("[REST] POST " + endpointName + " -> статус: " + response.getStatusCode());
    }

    // ======================= PUT =======================
    @Given("сохраняю URL как {string} для PUT запроса {string} с параметрами пути")
    public void saveUrlForPut(String urlKey, String endpointName, DataTable pathParamsTable) {
        Map<String, String> pathParams = toMap(pathParamsTable);
        String url = RestApiUtil.resolveUrl(endpointName, pathParams);
        urlStorage.put(urlKey, url);
        System.out.println("[REST] Сохранён URL для PUT: " + url);
    }

    @Given("отправляю PUT запрос по сохранённому URL {string} из файла {string} с параметрами JSON")
    public void sendPutBySavedUrl(String urlKey, String jsonFilePath, DataTable jsonParamsTable) throws Exception {
        String url = urlStorage.get(urlKey);
        if (url == null) {
            throw new IllegalArgumentException("URL не найден: " + urlKey);
        }
        Map<String, String> jsonParams = toMap(jsonParamsTable);
        String baseJson = JsonUtil.loadJsonFromFile(jsonFilePath);
        String finalJson = JsonUtil.applyParameters(baseJson, jsonParams);
        Response response = RestApiUtil.sendPutToUrl(url, finalJson);
        lastResponse = response;
        responseStorage.put(urlKey, response);
        System.out.println("[REST] PUT " + url + " -> статус: " + response.getStatusCode());
    }

    // ======================= Проверка ответов =======================
    @Then("проверяю что ответ последнего запроса содержит параметры")
    public void verifyLastResponseContainsParams(DataTable expectedParams) {
        if (lastResponse == null) {
            throw new IllegalStateException("Нет последнего запроса.");
        }
        verifyResponse(lastResponse.getBody().asString(), expectedParams);
    }

    @Then("проверяю что ответ {string} содержит параметры")
    public void verifySavedResponseContainsParams(String responseKey, DataTable expectedParams) {
        Response resp = responseStorage.get(responseKey);
        if (resp == null) {
            throw new IllegalArgumentException("Ответ с ключом '" + responseKey + "' не найден.");
        }
        verifyResponse(resp.getBody().asString(), expectedParams);
    }

    // ======================= Вспомогательные методы =======================
    private void verifyResponse(String body, DataTable expectedParams) {
        Map<String, String> expected = toMap(expectedParams);
        boolean match = JsonUtil.matchesParameters(body, expected);
        assertTrue(match, "Ответ не содержит ожидаемые параметры: " + expected + "\nОтвет: " + body);
    }

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