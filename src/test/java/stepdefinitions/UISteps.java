package stepdefinitions;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.uk.Дано;
import utils.ElementMapper;
import utils.ScenarioContext;
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
    @И("сохраняем {string} товара {int} как {string}")
    public void saveProductAttribute(String attributeType, int numberVar, String varName) {
        String selector = ElementMapper.getSelector(attributeType);
        int index = numberVar - 1;
        String value = $$(selector).get(index).text();
        ScenarioContext.set(varName, value);
        System.out.println("Элемент с именем '" + value + "' cохранен как " + varName);
    }
    @Когда("я кликаю на {string} в карточке товара номер {int}")
    public void clickOnElementInsideProductCard(String childElementName, int productNumber) {
        int index = productNumber - 1;
        String cardSelector = ElementMapper.getSelector("карточка_товара");
        String childSelector = ElementMapper.getSelector(childElementName);

        SelenideElement productCard = $$(cardSelector).get(index);
        productCard.$(childSelector).click();
    }
    @И("находим элемент в списке {string} по имени из переменной {string} и сохраняем как {string}")
    public void findElementInListByName(String listSelectorKey, String itemNameVar, String varName) {
        String itemName = ScenarioContext.getString(itemNameVar);
        String listSelector = ElementMapper.getSelector(listSelectorKey);
        ElementsCollection items = $$(listSelector);
        SelenideElement found = items.findBy(Condition.text(itemName));
        if (!found.exists()) {
            throw new IllegalArgumentException("Элемент с именем '" + itemName + "' не найден в списке " + listSelectorKey);
        }
        ScenarioContext.set(varName, found);
        System.out.println("Элемент с именем: " + found + " cохранен как: " + varName);
    }
    @И("проверяем, что атрибут {string} элемента {string} равен {string}")
    public void checkPriceOfSavedElement(String attributeName,String elementVarName, String expectedPriceVar) {
        SelenideElement element = (SelenideElement) ScenarioContext.get(elementVarName);
        String expectedPrice = ScenarioContext.getString(expectedPriceVar);
        String priceSelector = ElementMapper.getSelector(attributeName);
        element.$(priceSelector).shouldHave(Condition.text(expectedPrice));
        System.out.println("Цена товара: " + expectedPrice);
    }
    @Before
    public void setUp(Scenario scenario) {

    }

    @After
    public void tearDown(Scenario scenario) {

    }
}