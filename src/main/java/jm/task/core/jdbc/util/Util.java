package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class Util {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private Util() {
        throw new UnsupportedOperationException("У данного класса не может быть экземляра");
    }

    public static Optional<Connection> getDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return Optional.of(DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Ошибка загрузки драйвера базы данных", e);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }
}
