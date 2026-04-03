package stepdefinitions;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {
    private int result;

    @Дано("я открыл калькулятор")
    public void i_open_calculator() {
        // Ничего не делаем для примера
    }

    @Когда("я складываю {int} и {int}")
    public void i_add(int a, int b) {
        result = a + b;
    }

    @Тогда("результат должен быть {int}")
    public void result_should_be(int expectedResult) {
        assertEquals(expectedResult, result);
    }
}