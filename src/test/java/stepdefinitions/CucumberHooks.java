package stepdefinitions;

import com.codeborne.selenide.Configuration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class CucumberHooks {

    @Before
    public void setUp(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@ui")) {
            // Все настройки читаются из selenide.properties,
            open();
            System.out.println("Браузер запущен для UI-теста: " + scenario.getName());
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@ui")) {
            closeWebDriver();
            System.out.println("Браузер закрыт после UI-теста.");
        }
    }
}