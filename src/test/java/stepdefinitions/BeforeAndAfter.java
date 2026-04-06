package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import static com.codeborne.selenide.Selenide.open;

public class BeforeAndAfter {

    @Before
    public void setUp(Scenario scenario) {

    }

    @After
    public void tearDown(Scenario scenario) {

    }
}