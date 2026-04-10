package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {
    // Параметры подключения из docker-compose (порт 5432)
    private static final String URL = ConfigLoader.getDbUrl();
    private static final String USER = ConfigLoader.getDbUser();
    private static final String PASSWORD = ConfigLoader.getDbPassword();

    static {
        // Проверка, что параметры загружены (опционально)
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException("Database configuration not found in application.properties");
        }
    }
    static {
        System.out.println("DB URL: " + ConfigLoader.getDbUrl());
        System.out.println("DB User: " + ConfigLoader.getDbUser());
        System.out.println("DB Pass: " + ConfigLoader.getDbPassword());
    }
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return resultSetToList(rs);
        }
    }

    public static int executeUpdate(String sql) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    private static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            results.add(row);
        }
        return results;
    }
}