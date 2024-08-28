package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao usersDao = new UserDaoJDBCImpl();

    @Override
    public void createUsersTable() {
        usersDao.createUsersTable();
    }

    @Override
    public void dropUsersTable() {
        usersDao.dropUsersTable();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        usersDao.saveUser(name, lastName, age);
        System.out.println("User c именем " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        usersDao.removeUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    @Override
    public void cleanUsersTable() {
        usersDao.cleanUsersTable();
    }
}
