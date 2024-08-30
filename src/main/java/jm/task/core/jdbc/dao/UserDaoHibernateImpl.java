package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Function;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory sessionFactory = Util.getHibernateSessionFactory();

    public UserDaoHibernateImpl() {}

    @Override
    public void createUsersTable() {
        String sqlCreateTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL, "
                + "age TINYINT UNSIGNED NOT NULL CHECK (age >= 0 AND age <= 127)"
                + ");";
        executeDDLQuery(sqlCreateTableQuery);
    }

    @Override
    public void dropUsersTable() {
        String sqlDropTableQuery = "DROP TABLE IF EXISTS users;";
        executeDDLQuery(sqlDropTableQuery);
    }

    @Override
    public void cleanUsersTable() {
        String sqlCleanUsersTableQuery = "TRUNCATE TABLE users;";
        executeDDLQuery(sqlCleanUsersTableQuery);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeDMLorDQLQuery(session -> {
            String sqlSaveUserQuery = "INSERT INTO users (name, last_name, age) VALUES (:name, :lastName, :age);";
            session.createNativeQuery(sqlSaveUserQuery)
                    .setParameter("name", name)
                    .setParameter("lastName", lastName)
                    .setParameter("age", age)
                    .executeUpdate();
            return null;
        });
    }

    @Override
    public void removeUserById(long id) {
        executeDMLorDQLQuery(session -> {
            String sqlRemoveUserQuery = "DELETE FROM users WHERE user_id = :id";
            session.createNativeQuery(sqlRemoveUserQuery)
                    .setParameter("id", id)
                    .executeUpdate();
            return null;
        });
    }

    @Override
    public List<User> getAllUsers() {
        return executeDMLorDQLQuery(session -> {
            String sqlGetAllUsersQuery = "SELECT user_id, name, last_name, age FROM users;";
            return session
                    .createNativeQuery(sqlGetAllUsersQuery, User.class)
                    .getResultList();
        });
    }

    private void executeDDLQuery(String sql) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
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
            throw new RuntimeException("Ошибка при обработке DDL запроса", e);
        }
    }

    private <R> R executeDMLorDQLQuery(Function<Session, R> operation) {
        Transaction transaction = null;
        R result;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = operation.apply(session);
            transaction.commit();
        } catch (Exception e) {
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (Exception rollbackException) {
                System.err.println("Ошибка при откате транзакции: "
                        + rollbackException.getMessage());
            }
            throw new RuntimeException("Ошибка при обработке DML или DQL запроса", e);
        }
        return result;
    }
}