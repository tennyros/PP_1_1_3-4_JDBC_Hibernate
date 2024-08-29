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

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static SessionFactory sessionFactory;

    private Util() {
        throw new UnsupportedOperationException("У данного класса не может быть экземляра");
    }

    public static Optional<Connection> getJDBCConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return Optional.of(DriverManager.getConnection(
                    JDBCPropertiesUtil.get(URL_KEY),
                    JDBCPropertiesUtil.get(USERNAME_KEY),
                    JDBCPropertiesUtil.get(PASSWORD_KEY)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Ошибка загрузки драйвера базы данных", e);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    public static SessionFactory getHibernateSessionFactory() {
        if (sessionFactory == null) {
            try (InputStream input = Util.class.getClassLoader().getResourceAsStream("hibernate.properties")) {
                Properties properties = new Properties();
                properties.load(input);
                properties.forEach((key, value) -> System.out.println(key + " : " + value));
                Configuration configuration = new Configuration();
                configuration.addAnnotatedClass(User.class);
                configuration.addProperties(properties);
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
}

