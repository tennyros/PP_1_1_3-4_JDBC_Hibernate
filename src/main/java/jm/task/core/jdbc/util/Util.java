package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class Util {

    private static SessionFactory sessionFactory;

    private static final String URL = "jdbc:mysql://localhost:3306/first_project_schema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private Util() {
        throw new UnsupportedOperationException("У данного класса не может быть экземляра");
    }

    public static SessionFactory getHibernateSessionFactory() {
        if (sessionFactory == null) {
            try (InputStream input = Util.class.getClassLoader()
                    .getResourceAsStream("hibernate.properties")) {
                Properties properties = new Properties();
                properties.load(input);

                envVariablesHandle(properties);

                Configuration configuration = new Configuration()
                        .addAnnotatedClass(User.class)
                        .addProperties(properties);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при работе с файлом hibernate.properties", e);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при создании ServiceFactory", e);
            }
        }
        return sessionFactory;
    }

    private static void envVariablesHandle(Properties properties) {
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbUsername == null && dbPassword == null) {
            throw new RuntimeException("Не установлены переменные окружения - DB_USERNAME и(или) DB_PASSWORD");
        }
        properties.setProperty("hibernate.connection.username", dbUsername);
        properties.setProperty("hibernate.connection.password", dbPassword);
    }

    public static void sessionFactoryClose() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static Optional<Connection> getJDBCConnection() {
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

