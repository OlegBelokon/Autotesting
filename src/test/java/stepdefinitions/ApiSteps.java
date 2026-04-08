package stepdefinitions;

import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import utils.ConfigLoader;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiSteps {
    private RequestSpecification request;
    private Response response;
    //private String baseUrl = "https://jsonplaceholder.typicode.com"; // можно вынести в конфиг
    //private String baseUrl = ConfigLoader.getProperty("api.base.url");

    @Когда("я отправляю GET запрос на {string}")
    public void sendGetRequest(String endpoint) {
        response = request.when().get(endpoint);
        System.out.println("Response: " + response.asString());
    }

    @Когда("я отправляю POST запрос на {string} с телом:")
    public void sendPostRequest(String endpoint, String body) {
        response = request
                .header("Content-Type", "application/json")
                .body(body)
                .when().post(endpoint);
    }

    @Тогда("статус ответа {int}")
    public void checkStatus(int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Тогда("в ответе есть поле {string} со значением {string}")
    public void checkFieldEquals(String jsonPath, String expectedValue) {
        response.then().body(jsonPath, equalTo(expectedValue));
    }

    @Тогда("массив {string} содержит {int} элементов")
    public void checkArraySize(String jsonPath, int expectedSize) {
        response.then().body(jsonPath, hasSize(expectedSize));
    }

    // Универсальная проверка (для чисел, boolean)
    @Тогда("в ответе поле {string} равно {int}")
    public void checkFieldInt(String jsonPath, int expected) {
        response.then().body(jsonPath, equalTo(expected));
    }

    // Сохранить значение из ответа в переменную (для цепочек вызовов)
    private String extractedValue;

    @Тогда("я сохраняю значение поля {string} как {string}")
    public void saveValue(String jsonPath, String varName) {
        extractedValue = response.jsonPath().getString(jsonPath);
        System.out.println("Saved " + varName + " = " + extractedValue);
    }
}