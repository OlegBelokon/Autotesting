package stepdefinitions;

import io.cucumber.java.en.Then;
import utils.DatabaseUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseSteps {

    @Then("в базе данных есть запись с именем {string}")
    public void checkRecordInDatabase(String expectedName) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = '" + expectedName + "'";
        List<Map<String, Object>> results = DatabaseUtil.executeQuery(sql);
        assertTrue(results.size() == 1, "Запись с именем " + expectedName + " не найдена или найдено больше одной.");
    }

    @Then("в таблице {string} количество записей равно {int}")
    public void checkTableSize(String tableName, int expectedCount) throws SQLException {
        String sql = "SELECT COUNT(*) as cnt FROM " + tableName;
        List<Map<String, Object>> results = DatabaseUtil.executeQuery(sql);
        int actualCount = Math.toIntExact((Long) results.get(0).get("cnt"));
        assertEquals(expectedCount, actualCount, "Количество записей в таблице " + tableName + " не совпадает.");
    }
}