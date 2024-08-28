package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDaoJDBCImpl userDaoJDBC = new UserDaoJDBCImpl();
        userDaoJDBC.createUsersTable();
        userDaoJDBC.saveUser("Max", "Smith", (byte) 25);
        userDaoJDBC.saveUser("John", "Doe", (byte) 30);
        userDaoJDBC.saveUser("Jane", "Doe", (byte) 35);
        userDaoJDBC.saveUser("Emma", "Turing", (byte) 20);
        System.out.println(userDaoJDBC.getAllUsers());
        userDaoJDBC.cleanUsersTable();
        userDaoJDBC.dropUsersTable();
    }
}
