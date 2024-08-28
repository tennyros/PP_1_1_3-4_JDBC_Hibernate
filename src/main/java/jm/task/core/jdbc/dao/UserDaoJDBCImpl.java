package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {

    }

    public void initConnection() {
        this.connection = Util.getDBConnection()
                .orElseThrow(()
                        -> new RuntimeException(
                        "Не удалось установить соединение к базе данных"));
    }

    public void createUsersTable() {
        if (connection == null) {
            initConnection();
        }

        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                + "id TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(50) NOT NULL, "
                + "lastName VARCHAR(50) NOT NULL, "
                + "age TINYINT UNSIGNED NOT NULL CHECK (age >= 0 AND age <= 150)"
                + ");";

        try (PreparedStatement statement = connection.prepareStatement(createUserTableQuery)) {
            statement.executeUpdate(createUserTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы users", e);
        }
    }

    public void dropUsersTable() {
        if (connection == null) {
            initConnection();
        }

        String dropUserTableQuery = "DROP TABLE IF EXISTS users";

        try (PreparedStatement statement = connection.prepareStatement(dropUserTableQuery)) {
            statement.executeUpdate(dropUserTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы users", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        if (connection == null) {
            initConnection();
        }

        String addUserQuery = "INSERT INTO first_project_schema.users "
                + "(id, name, lastName, age) "
                + "VALUES (?, ?, ?, ?)"
                + " ON DUPLICATE KEY UPDATE "
                + " name = ?, lastName = ?, age = ?;";

        try (PreparedStatement statement = connection.prepareStatement(addUserQuery)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate(addUserQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении пользователя в таблицу users", e);
        }
    }

    public void removeUserById(long id) {
        if (connection == null) {
            initConnection();
        }

        String removeUserByIdQuery = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(removeUserByIdQuery)) {
            statement.setLong(1, id);
            statement.executeQuery(removeUserByIdQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользавателя из таблицы users", e);
        }
    }

    public List<User> getAllUsers() {
        if (connection == null) {
            initConnection();
        }

        List<User> users = new ArrayList<>();
        String getAllUsersQuery = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(getAllUsersQuery)) {
            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении данных обо всех пользователях в таблице users", e);
        }
        return users;
    }

    public void cleanUsersTable() {
        if (connection == null) {
            initConnection();
        }


    }
}
