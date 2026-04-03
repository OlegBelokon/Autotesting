package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {
    private int result;

    @Given("я открыл калькулятор")
    public void i_open_calculator() {
        // Ничего не делаем для примера
    }

    @When("я складываю {int} и {int}")
    public void i_add(int a, int b) {
        result = a + b;
    }

    @Then("результат должен быть {int}")
    public void result_should_be(int expectedResult) {
        assertEquals(expectedResult, result);
    }
}