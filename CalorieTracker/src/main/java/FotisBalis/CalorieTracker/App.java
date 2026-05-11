package FotisBalis.CalorieTracker;

import java.sql.Connection;
import java.sql.SQLException;

import FotisBalis.CalorieTracker.model.DatabaseConnection;
import FotisBalis.CalorieTracker.view.MainMenu;

public class App {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }

        MainMenu.main(args);
    }
}
