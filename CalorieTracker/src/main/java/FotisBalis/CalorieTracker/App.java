package FotisBalis.CalorieTracker;

import java.sql.Connection;
import java.sql.DriverManager;

public class App {
    public static void main(String[] args) {
        
    	String url = "jdbc:mysql://127.0.0.1:3306/calorie_tracker";
        String user = "root";
        String password = "admin123";
        
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successful.");
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
