package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:3306/first_project_schema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private Util() {
        throw new UnsupportedOperationException("У класса не может быть экземляра");
    }

    public static Optional<Connection> getDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return Optional.of(DriverManager.getConnection(URL, USERNAME, PASSWORD));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Ошибка загрузки драйвера базы данных", e);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }
}
