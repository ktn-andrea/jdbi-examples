package user;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(User.class)
public interface UserDao {

    @SqlUpdate("""
        CREATE TABLE users (
            id IDENTITY PRIMARY KEY,
            username VARCHAR,
            password VARCHAR,
            name VARCHAR,
            email VARCHAR ,
            gender ENUM('FEMALE', 'MALE'),
            birthdate DATE,
            enabled BOOLEAN
        )
        """
    )
    void createTable();

    @SqlUpdate("INSERT INTO users(username, password, name, email, gender, birthdate, enabled) VALUES (:username, :password, :name, :email, :gender, :birthDate, :enabled)")
    @GetGeneratedKeys("id")
    long insertUser(@BindBean User user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    Optional<User> getUserById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    Optional<User> getUserByUsername(@Bind("username") String username);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void deleteUser(@BindBean User user);

    @SqlQuery("SELECT * FROM users ORDER BY id")
    List<User> listUsers();

}
