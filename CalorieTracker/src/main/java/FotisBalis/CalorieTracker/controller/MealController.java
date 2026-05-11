package FotisBalis.CalorieTracker.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import FotisBalis.CalorieTracker.model.DatabaseConnection;
import FotisBalis.CalorieTracker.model.SavedMeal;
import FotisBalis.CalorieTracker.model.TodayTotals;

public class MealController {

    public void newSavedMeal(String mealName, int calories, int fatGr, int carbsGr, int proteinGr) throws SQLException {
        String sql = "{CALL new_saved_meal(?, ?, ?, ?, ?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, mealName);
            statement.setInt(2, calories);
            statement.setInt(3, fatGr);
            statement.setInt(4, carbsGr);
            statement.setInt(5, proteinGr);
            statement.execute();
        }
    }

    public List<SavedMeal> listSavedMeals() throws SQLException {
        String sql = "{CALL list_saved_meals()}";
        List<SavedMeal> savedMeals = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                savedMeals.add(new SavedMeal(
                    resultSet.getInt("saved_meal_id"),
                    resultSet.getString("meal_name"),
                    resultSet.getInt("calories"),
                    resultSet.getInt("fat_gr"),
                    resultSet.getInt("carbs_gr"),
                    resultSet.getInt("protein_gr")
                ));
            }
        }

        return savedMeals;
    }

    public void addMealToToday(String mealName, int calories, int fatGr, int carbsGr, int proteinGr) throws SQLException {
        String sql = "{CALL add_meal_to_today(?, ?, ?, ?, ?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, mealName);
            statement.setInt(2, calories);
            statement.setInt(3, fatGr);
            statement.setInt(4, carbsGr);
            statement.setInt(5, proteinGr);
            statement.execute();
        }
    }

    public void addSavedMealToToday(String mealName) throws SQLException {
        String sql = "{CALL add_saved_meal_to_today(?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, mealName);
            statement.execute();
        }
    }

    public void deleteSavedMeal(String mealName) throws SQLException {
        String sql = "delete from saved_meal where meal_name = ?";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, mealName);
            statement.executeUpdate();
        }
    }

    public TodayTotals showTodayTotals() throws SQLException {
        String sql = "{CALL show_today_totals()}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return new TodayTotals(
                    resultSet.getDate("meal_date").toLocalDate(),
                    resultSet.getInt("calories"),
                    resultSet.getInt("fat"),
                    resultSet.getInt("carbs"),
                    resultSet.getInt("protein")
                );
            }
        }

        return null;
    }
}
