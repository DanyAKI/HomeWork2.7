package server;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class AuthenticationService {

    // private static final Set<User> users = Set.of(
      //       new User("user1", "l1", "p1"),
        //     new User("user2", "l2", "p2"),
          //   new User("user3", "l3", "p3")
            // );


    public Optional<String> findUsernameByLoginAndPassword(String login, String password) {

        try( Connection connection = DatabaseConnector.getConnector()) {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM users WHERE login = ? AND password= ?");
            st.setString(1, login);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }










        //return users.stream()
         //       .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
     //           .findFirst()
         //       .map(User::getUsername);
   // }


    private static class User {
        private String username;
        private String login;
        private String password;

        public User(String username, String login, String password) {
            this.username = username;
            this.login = login;
            this.password = password;

        }

        public String getUsername() {
            return username;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(username, user.username) && Objects.equals(login, user.login) && Objects.equals(password, user.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(username, login, password);
        }
    }
}


