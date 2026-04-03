package stepdefinitions;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import static com.codeborne.selenide.CollectionCondition.allMatch;
import static com.codeborne.selenide.Selenide.*;

public class GoogleSearchSteps {

    @Given("Я открываю главную страницу Google")
    public void openGoogle() {
        // Открываем страницу. Базовый URL мы уже задали в хуке
        open("/");
        // Проверим, что страница загрузилась, дождавшись появления поля поиска
        $(By.name("q")).shouldBe(Condition.visible);
    }

    @When("Я ввожу {string} в поле поиска и нажимаю Enter")
    public void searchFor(String searchTerm) {
        $(By.name("q")).setValue(searchTerm).pressEnter();
    }

    @Then("Я вижу, что заголовки результатов содержат слово {string}")
    public void resultsContain(String expectedWord) {
        $$("#search .g h3").shouldHave(allMatch("contain text", element -> element.getText().contains(expectedWord)));
    }
}