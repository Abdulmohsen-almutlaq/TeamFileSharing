package database;
import java.sql.*;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() {
        // Private constructor
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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