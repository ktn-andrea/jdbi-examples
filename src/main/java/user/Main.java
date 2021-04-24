package user;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());

        int max = 10;
        Random rand = new Random();
        int r = rand.nextInt(max)+1;

        UserGenerator generator = new UserGenerator(new Locale("hu"));
        List<User> users = jdbi.withExtension(UserDao.class, dao -> {
                dao.createTable();
                for (int i = 0; i < max; i++) {
                    dao.insertUser(generator.createUser());
                }

                User testUser = dao.getUserById((long)r).get();
                System.out.println("----------------------");
                System.out.println(testUser);
                System.out.format("Information about the user of ID %d:\n", r);
                System.out.println("\tUsername: " + testUser.getUsername());
                System.out.println("\tE-mail: " + testUser.getEmail());
                System.out.println("\tBirthdate: " + testUser.getBirthDate());
                dao.deleteUser(dao.getUserById((long)r).get());
                System.out.format("Deleted user of ID %d.\n", r);
                System.out.println("----------------------");

                return dao.listUsers();
            });

        users.forEach(System.out::println);

    }

}
