package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {}

    private void initSessionFactory() {
        this.sessionFactory = Util.getHibernateSessionFactory();
    }


    @Override
    public void createUsersTable() {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sqlCreateTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                    + "user_id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(50) NOT NULL, "
                    + "last_name VARCHAR(50) NOT NULL, "
                    + "age TINYINT UNSIGNED NOT NULL CHECK (age >= 0 AND age <= 127)"
                    + ");";
            session.createNativeQuery(sqlCreateTableQuery).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    System.err.println("Ошибка при откате транзакции: "
                            + rollbackException.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при создании таблицы пользователей", e);
        }
    }

    @Override
    public void dropUsersTable() {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sqlDropTableQuery = "DROP TABLE IF EXISTS users;";
            session.createNativeQuery(sqlDropTableQuery).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    System.err.println("Ошибка при откате транзакции: "
                            + rollbackException.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при удалении таблицы из базы данных", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sqlSaveUserQuery = "INSERT INTO users (name, last_name, age) VALUES (:name, :lastName, :age);";
            session.createNativeQuery(sqlSaveUserQuery)
                    .setParameter("name", name)
                    .setParameter("lastName", lastName)
                    .setParameter("age", age)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    System.err.println("Ошибка при откате транзакции: "
                            + rollbackException.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sqlRemoveUserQuery = "DELETE FROM users WHERE user_id = :id";
            session.createNativeQuery(sqlRemoveUserQuery)
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    System.err.println("Ошибка при откате транзакции: "
                            + rollbackException.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при удалении определенного пользователя из таблиц", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        Transaction transaction = null;
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sqlGetAllUsersQuery = "SELECT user_id, name, last_name, age FROM users;";
            Query<Object[]> query = session.createNativeQuery(sqlGetAllUsersQuery);
            List<Object[]> results = query.getResultList();
            for (Object[] result : results) {
                User user = new User();
                user.setId(Long.parseLong(result[0].toString()));
                user.setName(result[1].toString());
                user.setLastName(result[2].toString());
                user.setAge(Byte.parseByte(result[3].toString()));
                users.add(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    System.err.println("Ошибка при откате транзакции: "
                            + rollbackException.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при получении списка пользователей из таблицы", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sqlCleanUsersTableQuery = "TRUNCATE TABLE users;";
            session.createNativeQuery(sqlCleanUsersTableQuery).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    System.err.println("Ошибка при откате транзакции: "
                            + rollbackException.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при очистке таблицы пользователей", e);
        }
    }
}
