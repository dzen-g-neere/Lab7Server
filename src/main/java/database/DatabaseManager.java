package database;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class DatabaseManager {
    private static Scanner scanner;
    static {
        try {
            scanner = new Scanner(Paths.get("C:\\Users\\DZstd\\Desktop\\HeliosDATA.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String login = scanner.nextLine();
    private static String password = scanner.nextLine();
    private static final DBConnector connectionToDatabase = new DBConnector(login, password);
    private static final DBLabWork dbLabwork = new DBLabWork(connectionToDatabase.getConnection());
    private static final DBUser dbUser = new DBUser(connectionToDatabase.getConnection());


    public static DBLabWork getDBLabWork() {
        return dbLabwork;
    }

    public static DBUser getDBUser() {
        return dbUser;
    }
}

