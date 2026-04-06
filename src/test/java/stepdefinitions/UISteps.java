package stepdefinitions;

import com.codeborne.selenide.Condition;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.uk.Дано;
import utils.ElementMapper;

import static com.codeborne.selenide.Selenide.*;

public class UISteps {

    //Открываем URL
    @Дано("открываем страницу {string}")
    public void openPage(String nameOrUrl) {
        String url;
        if (nameOrUrl.startsWith("http://") || nameOrUrl.startsWith("https://")) {
            url = nameOrUrl; // полный URL
        } else {
            url = UrlMapper.getUrl(nameOrUrl); // имя из конфига
        }
        open(url);
    }
    @Тогда("ввожу в поле {string} текст из переменной {string}")
    public void enterTextFromSecret(String elementName, String secretKey) {
        String selector = ElementMapper.getSelector(elementName);
        String value = SecretLoader.get(secretKey);
        $(selector).setValue(value);
    }
    // === Нажатия на элементы ===
    @И("нажимаю на кнопку {string}")
    public void clickButton(String buttonName) {
        $(ElementMapper.getSelector(buttonName)).click();
    }

    @И("Я нажимаю на элемент {string}")
    public void clickElement(String elementName) {
        $(ElementMapper.getSelector(elementName)).click();
    }

    // === Ввод текста ===
    @Когда("Я ввожу {string} в поле {string}")
    public void enterText(String text, String fieldName) {
        $(ElementMapper.getSelector(fieldName)).setValue(text);
    }

    @И("Я очищаю поле {string}")
    public void clearField(String fieldName) {
        $(ElementMapper.getSelector(fieldName)).clear();
    }

    // === Проверки видимости ===
    @Тогда("Я вижу элемент {string}")
    public void seeElement(String elementName) {
        $(ElementMapper.getSelector(elementName)).shouldBe(Condition.visible);
    }

    @Тогда("Я не вижу элемент {string}")
    public void notSeeElement(String elementName) {
        $(ElementMapper.getSelector(elementName)).shouldBe(Condition.hidden);
    }

    // === Проверка текста на элементе ===
    @Тогда("Элемент {string} содержит текст {string}")
    public void elementContainsText(String elementName, String expectedText) {
        $(ElementMapper.getSelector(elementName)).shouldHave(Condition.text(expectedText));
    }

    @Тогда("Элемент {string} имеет точный текст {string}")
    public void elementHasExactText(String elementName, String expectedText) {
        $(ElementMapper.getSelector(elementName)).shouldHave(Condition.exactText(expectedText));
    }

    // === Дополнительные полезные шаги ===
    @Когда("Я жду {int} секунд")
    public void waitSeconds(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }

    @Когда("Я обновляю страницу")
    public void refreshPage() {
        refresh();
    }

    @Когда("Я перехожу назад")
    public void back() {
        back();
    }

    @Когда("Я перехожу вперёд")
    public void forward() {
        forward();
    }
}