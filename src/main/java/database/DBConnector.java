package database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static java.sql.Connection connection;
    private String login = "";
    private String password = "";
    private String url = "jdbc:postgresql://localhost:8697/studs";
    public DBConnector(String login, String password) {
        this.login = login;
        this.password = password;
        try {
            connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connectToDatabase() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        connection = DriverManager.getConnection(url, login, password);
        setConnection(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DBConnector.connection = connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    private String hashPassword(String password) {
        String sha1 = password;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            sha1 = new String(md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Problem with hashing! Password isn't hashed");
        }
        return sha1;
    }
}
