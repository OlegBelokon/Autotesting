package stepdefinitions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class CucumberHooks {

    // Это действие выполнится один раз перед запуском всех тестов
    @BeforeAll
    public static void setUp() {
        // Настраиваем Selenide
        Configuration.browser = "chrome";             // Выбираем браузер (по умолчанию и так Chrome)
        Configuration.browserSize = "1920x1080";      // Устанавливаем размер окна
        Configuration.headless = false;               // false - вы увидите браузер, true - тесты будут идти в фоне
        Configuration.baseUrl = "https://www.google.com"; // Базовый URL для тестов
        Configuration.timeout = 10000;                // Максимальное время ожидания элемента (10 секунд)

        System.out.println("Настройка Selenide завершена.");
    }

    // Это действие выполнится один раз после прохождения всех тестов
    @AfterAll
    public static void tearDown() {
        // Закрывает браузер после всех тестов
        closeWebDriver();
        System.out.println("Браузер закрыт.");
    }

    // Если хотите открывать новое окно браузера ПЕРЕД КАЖДЫМ сценарием
     @Before
     public static void openBrowser() {
         open();
     }

    // Если хотите закрывать браузер ПОСЛЕ КАЖДОГО сценария
     @After
     public static void closeBrowser() {
         closeWebDriver();
     }
}