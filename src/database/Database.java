package database;
import java.sql.*;

public class Database {
    private static Connection connection;

    public static Connection connect() {
        if (connection != null) return connection;

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://" +
                    System.getenv("DB_HOST") + ":5432/" +
                    System.getenv("DB_NAME");

            connection = DriverManager.getConnection(
                    url,
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS")
            );

            System.out.println("Connected to PostgreSQL.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}