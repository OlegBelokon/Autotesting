package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.DatabaseUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSteps {

    private static final Map<String, String> sqlStorage = new HashMap<>();

    // 1. Сохранить SQL запрос с именем
    @Given("сохранить SQL запрос как {string}")
    public void saveSqlQuery(String name, String sql) {
        sqlStorage.put(name, sql);
        System.out.println("[DB] SQL сохранён с именем '" + name + "':\n" + sql);
    }

    // 2. Отправить во временную БД запрос (с автоматическим созданием TEMP TABLE)
    @Given("отправить во временную базу данных запрос {string}")
    public void executeTempSqlByName(String name) throws SQLException {
        String sql = getSqlByName(name);
        if (sql.trim().toUpperCase().startsWith("CREATE TABLE")) {
            sql = sql.replaceFirst("(?i)CREATE TABLE", "CREATE TEMP TABLE");
        }
        DatabaseUtil.executeUpdate(sql);
    }

    // 3. Отправить в постоянную БД запрос (без изменений)
    @Given("отправить в базу данных запрос {string}")
    public void executePermSqlByName(String name) throws SQLException {
        String sql = getSqlByName(name);
        DatabaseUtil.executeUpdate(sql);
    }

    // 4. Отправить во временную БД запрос (SELECT) и проверить параметры
    @Then("отправить во временную базу данных запрос {string} и проверить параметры")
    public void executeTempSqlAndCheckParams(String name, DataTable table) throws SQLException {
        String sql = getSqlByName(name);
        List<Map<String, Object>> results = DatabaseUtil.executeQuery(sql);
        assertFalse(results.isEmpty(), "Запрос не вернул ни одной записи.");
        Map<String, String> expected = toMap(table);
        boolean match = results.stream().anyMatch(row -> rowMatches(row, expected));
        assertTrue(match, "Не найдена запись, соответствующая параметрам: " + expected);
    }

    // 5. Отправить в постоянную БД запрос (SELECT) и проверить параметры
    @Then("отправить в базу данных запрос {string} и проверить параметры")
    public void executePermSqlAndCheckParams(String name, DataTable table) throws SQLException {
        String sql = getSqlByName(name);
        List<Map<String, Object>> results = DatabaseUtil.executeQuery(sql);
        assertFalse(results.isEmpty(), "Запрос не вернул ни одной записи.");
        Map<String, String> expected = toMap(table);
        boolean match = results.stream().anyMatch(row -> rowMatches(row, expected));
        assertTrue(match, "Не найдена запись, соответствующая параметрам: " + expected);
    }

    @Before
    public void openConnection() {
        // Принудительно откроем соединение перед сценарием
        try {
            DatabaseUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось открыть соединение с БД", e);
        }
    }

    @After("@tempDb")
    public void cleanup(Scenario scenario) {
        try {
            DatabaseUtil.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            // игнорируем, если таблицы нет
        }
        try {
            DatabaseUtil.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @After("@db")
    public void cleanUp() {
        try {
            DatabaseUtil.executeUpdate("TRUNCATE TABLE testers, users, orders RESTART IDENTITY");
        } catch (SQLException e) {
            System.err.println("[DB] Ошибка очистки: " + e.getMessage());
        }
    }

    private String getSqlByName(String name) {
        String sql = sqlStorage.get(name);
        if (sql == null) {
            throw new IllegalArgumentException("SQL запрос с именем '" + name + "' не найден.");
        }
        return sql;
    }

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