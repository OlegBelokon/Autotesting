package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.PermDatabaseUtil;
import utils.TempDatabaseUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSteps {

    private String lastSql; // хранит последний сохранённый SQL

    // 1. Сохранить SQL запрос
    @Given("сохранить SQL запрос как {string}")
    public void saveSqlQuery(String sql) {
        this.lastSql = sql;
    }

    // 2. Отправить во временную БД запрос (использует сохранённый)
    @Given("отправить во временную базу данных запрос")
    public void executeTempSql() throws SQLException {
        assertNotNull(lastSql, "Нет сохранённого SQL. Используйте шаг 'сохранить SQL запрос'.");
        TempDatabaseUtil.executeUpdate(lastSql);
    }

    // 3. Отправить в постоянную БД запрос (использует сохранённый)
    @Given("отправить в базу данных запрос")
    public void executePermSql() throws SQLException {
        assertNotNull(lastSql, "Нет сохранённого SQL. Используйте шаг 'сохранить SQL запрос'.");
        PermDatabaseUtil.executeUpdate(lastSql);
    }

    // 4. Отправить во временную БД запрос и проверить параметры
    @Then("отправить во временную базу данных запрос {string} и проверить параметры")
    public void executeTempSqlAndCheckParams(String sql, DataTable table) throws SQLException {
        List<Map<String, Object>> results = TempDatabaseUtil.executeQuery(sql);
        assertFalse(results.isEmpty(), "Запрос не вернул ни одной записи.");
        Map<String, String> expected = toMap(table);
        boolean match = results.stream().anyMatch(row -> rowMatches(row, expected));
        assertTrue(match, "Не найдена запись, соответствующая параметрам: " + expected);
    }

    // 5. Отправить в постоянную БД запрос и проверить параметры
    @Then("отправить в базу данных запрос {string} и проверить параметры")
    public void executePermSqlAndCheckParams(String sql, DataTable table) throws SQLException {
        List<Map<String, Object>> results = PermDatabaseUtil.executeQuery(sql);
        assertFalse(results.isEmpty(), "Запрос не вернул ни одной записи.");
        Map<String, String> expected = toMap(table);
        boolean match = results.stream().anyMatch(row -> rowMatches(row, expected));
        assertTrue(match, "Не найдена запись, соответствующая параметрам: " + expected);
    }

    // Вспомогательный метод для проверки соответствия строки ожидаемым параметрам
    private boolean rowMatches(Map<String, Object> row, Map<String, String> expected) {
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String column = entry.getKey();
            String expectedValue = entry.getValue();
            Object actualValue = row.get(column);
            if (actualValue == null || !actualValue.toString().equals(expectedValue)) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> toMap(DataTable table) {
        Map<String, String> map = new HashMap<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> row : rows) {
            if (row.size() >= 2) {
                map.put(row.get(0), row.get(1));
            }
        }
        return map;
    }
}