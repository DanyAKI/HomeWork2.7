package server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnector {
    public static Connection getConnector(){
        try{
            return DriverManager.getConnection("jdbc:SQLite://localhost: 3306/geekbrains", "root", "root");
        } catch (SQLException e) {
            throw new RuntimeException("sww during getting a connector.", e);
        }
    }

    public static void  close(Connection connection){

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("sww during getting a connector.", e);
        }
    }

    public static void rollback(Connection connection){

        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("sww during a rollback.", e);
        }
    }

}

