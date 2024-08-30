package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("Max", "Smith", (byte) 127);
        service.saveUser("John", "Doe", (byte) 30);
        service.saveUser("Jane", "Doe", (byte) 35);
        service.saveUser("Emma", "Turing", (byte) 20);
        System.out.println(service.getAllUsers());
        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
