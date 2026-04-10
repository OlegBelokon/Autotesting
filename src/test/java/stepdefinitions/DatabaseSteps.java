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

    // Хранилище SQL-запросов по имени
    private static final Map<String, String> sqlStorage = new HashMap<>();

    // 1. Сохранить SQL запрос с именем (DocString)
    @Given("сохранить SQL запрос как {string}")
    public void saveSqlQuery(String name, String sql) {
        sqlStorage.put(name, sql);
        System.out.println("[DB] SQL сохранён с именем '" + name + "':\n" + sql);
    }

    // 2. Отправить во временную БД запрос по имени
    @Given("отправить во временную базу данных запрос {string}")
    public void executeTempSqlByName(String name) throws SQLException {
        String sql = getSqlByName(name);
        TempDatabaseUtil.executeUpdate(sql);
    }

    // 3. Отправить в постоянную БД запрос по имени
    @Given("отправить в базу данных запрос {string}")
    public void executePermSqlByName(String name) throws SQLException {
        String sql = getSqlByName(name);
        PermDatabaseUtil.executeUpdate(sql);
    }

    // 4. Отправить во временную БД запрос по имени и проверить параметры
    @Then("отправить во временную базу данных запрос {string} и проверить параметры")
    public void executeTempSqlAndCheckParams(String name, DataTable table) throws SQLException {
        String sql = getSqlByName(name);
        List<Map<String, Object>> results = TempDatabaseUtil.executeQuery(sql);
        assertFalse(results.isEmpty(), "Запрос не вернул ни одной записи.");
        Map<String, String> expected = toMap(table);
        boolean match = results.stream().anyMatch(row -> rowMatches(row, expected));
        assertTrue(match, "Не найдена запись, соответствующая параметрам: " + expected);
    }

    // 5. Отправить в постоянную БД запрос по имени и проверить параметры
    @Then("отправить в базу данных запрос {string} и проверить параметры")
    public void executePermSqlAndCheckParams(String name, DataTable table) throws SQLException {
        String sql = getSqlByName(name);
        List<Map<String, Object>> results = PermDatabaseUtil.executeQuery(sql);
        assertFalse(results.isEmpty(), "Запрос не вернул ни одной записи.");
        Map<String, String> expected = toMap(table);
        boolean match = results.stream().anyMatch(row -> rowMatches(row, expected));
        assertTrue(match, "Не найдена запись, соответствующая параметрам: " + expected);
    }

    // Вспомогательный метод для получения SQL из хранилища
    private String getSqlByName(String name) {
        String sql = sqlStorage.get(name);
        if (sql == null) {
            throw new IllegalArgumentException("SQL запрос с именем '" + name + "' не найден. Сначала сохраните его через шаг 'сохранить SQL запрос как \"name\"'.");
        }
        return sql;
    }

    // Проверка соответствия строки ожидаемым параметрам
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

    // Преобразование DataTable в Map
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