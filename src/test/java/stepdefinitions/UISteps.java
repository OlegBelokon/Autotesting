package stepdefinitions;

import com.codeborne.selenide.Condition;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.uk.Дано;
import utils.ElementMapper;
import utils.SecretLoader;
import utils.UrlMapper;

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
    @И("вводим в поле {string} текст из переменной {string}")
    public void enterTextFromSecret(String elementName, String secretKey) {
        String selector = ElementMapper.getSelector(elementName);
        String value = SecretLoader.get(secretKey);
        $(selector).setValue(value);
    }
    // === Нажатия на элементы ===
    @И("нажимаем на кнопку {string}")
    public void clickButton(String buttonName) {
        $(ElementMapper.getSelector(buttonName)).click();
    }

    @Тогда("проверяем, что в поле {string} содержится текст {string}")
    public void checkTextInElement(String elementName, String messageText) {
        String selector = ElementMapper.getSelector(elementName);
        $(selector).shouldBe(Condition.visible).shouldHave(Condition.exactText(messageText));
    }
}