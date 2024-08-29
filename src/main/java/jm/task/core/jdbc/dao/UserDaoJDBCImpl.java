package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {}

    private void initConnection() {
        this.connection = Util.getDBConnection()
                .orElseThrow(()
                        -> new RuntimeException(
                        "Не удалось установить соединение к базе данных"));
    }

    @Override
    public void createUsersTable() {
        if (connection == null) {
            initConnection();
        }
        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                + "id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(50) NOT NULL, "
                + "lastName VARCHAR(50) NOT NULL, "
                + "age TINYINT UNSIGNED NOT NULL CHECK (age >= 0 AND age <= 150)"
                + ");";
        try (PreparedStatement statement = connection.prepareStatement(createUserTableQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы users", e);
        }
    }

    @Override
    public void dropUsersTable() {
        if (connection == null) {
            initConnection();
        }
        String dropUserTableQuery = "DROP TABLE IF EXISTS users;";
        try (PreparedStatement statement = connection.prepareStatement(dropUserTableQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы users", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        if (connection == null) {
            initConnection();
        }
        String addUserQuery = "INSERT INTO users "
                + "(name, lastName, age) "
                + "VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(addUserQuery)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении пользователя в таблицу users", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        if (connection == null) {
            initConnection();
        }
        String removeUserByIdQuery = "DELETE FROM users WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(removeUserByIdQuery)) {
            statement.setLong(1, id);
            System.out.println(statement.executeUpdate());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользавателя из таблицы users", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        if (connection == null) {
            initConnection();
        }
        String getAllUsersQuery = "SELECT id, name, lastName, age FROM users;";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(getAllUsersQuery)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении данных обо всех пользователях в таблице users", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        if (connection == null) {
            initConnection();
        }
        String cleanUsersTableQuery = "TRUNCATE TABLE users;";
        try (PreparedStatement statement = connection.prepareStatement(cleanUsersTableQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы users", e);
        }
    }
}
