package stepdefinitions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class CucumberHooks {

    // Этот метод будет выполняться ПЕРЕД КАЖДЫМ сценарием
    @Before
    public void setUp(Scenario scenario) {
        // Проверяем, есть ли у сценария тег @ui
        if (scenario.getSourceTagNames().contains("@ui")) {
            // Настройка Selenide только для UI-тестов
            Configuration.browser = "chrome";
            Configuration.browserSize = "1920x1080";
            Configuration.headless = false;  // true — если нужен фоновый режим
            //Configuration.baseUrl = "https://www.google.com";
            Configuration.timeout = 10000;

            // Открываем браузер (можно сразу открыть базовый URL)
            open();

            System.out.println("Браузер запущен для UI-теста: " + scenario.getName());
        } else {
            System.out.println("API тест, браузер не нужен: " + scenario.getName());
        }
    }

    // Этот метод будет выполняться ПОСЛЕ КАЖДОГО сценария
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@ui")) {
            closeWebDriver();
            System.out.println("Браузер закрыт после UI-теста.");
        }
    }
}