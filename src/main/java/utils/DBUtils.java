package utils;

import java.sql.*;
import java.util.*;

public class DBUtils {
    private static Connection connection;

    // Use properties from your ConfigurationReader
    public static void createConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                    ConfigReader.getProperty("db.url"),
                    ConfigReader.getProperty("db.username"),
                    ConfigReader.getProperty("db.password")
                );
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to Database", e);
            }
        }
    }

    // Parameterized Query Method
    public static List<Map<String, Object>> getQueryResultMap(String query, Object... params) {
        List<Map<String, Object>> rowList = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Dynamically set parameters (?, ?, ?)
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet resultSet = pstmt.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> colMap = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        colMap.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    rowList.add(colMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    // Method for DELETE/UPDATE/INSERT
    public static void executeUpdate(String query, Object... params) {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}