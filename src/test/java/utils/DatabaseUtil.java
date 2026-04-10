package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = ConfigLoader.getDbUrl();
            String user = ConfigLoader.getDbUser();
            String password = ConfigLoader.getDbPassword();
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("[DB] Новое соединение открыто");
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("[DB] Соединение закрыто");
        }
        connection = null;
    }

    public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return resultSetToList(rs);
        }
    }

    public static int executeUpdate(String sql) throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            int affected = stmt.executeUpdate(sql);
            System.out.println("[DB] Выполнен UPDATE/INSERT, затронуто строк: " + affected);
            return affected;
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