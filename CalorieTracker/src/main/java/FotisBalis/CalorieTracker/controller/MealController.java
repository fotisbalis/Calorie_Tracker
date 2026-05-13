package FotisBalis.CalorieTracker.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import FotisBalis.CalorieTracker.model.DatabaseConnection;
import FotisBalis.CalorieTracker.model.Per100Food;
import FotisBalis.CalorieTracker.model.SavedMeal;
import FotisBalis.CalorieTracker.model.TodayMeal;
import FotisBalis.CalorieTracker.model.TodayTotals;

public class MealController {

    public void newSavedMeal(String mealName, double calories, double fatGr, double carbsGr, double proteinGr) throws SQLException {
        String sql = "{CALL new_saved_meal(?, ?, ?, ?, ?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, mealName);
            statement.setDouble(2, calories);
            statement.setDouble(3, fatGr);
            statement.setDouble(4, carbsGr);
            statement.setDouble(5, proteinGr);
            statement.execute();
        }
    }
    
    public void newMacros(String mealName, double quantity, double calories, double fatGr, double carbsGr, double proteinGr) throws SQLException {
        String sql = "{CALL new_per_100_food(?, ?, ?, ?, ?, ?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, mealName);
            statement.setDouble(2, quantity);
            statement.setDouble(3, calories);
            statement.setDouble(4, fatGr);
            statement.setDouble(5, carbsGr);
            statement.setDouble(6, proteinGr);
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
                    resultSet.getDouble("calories"),
                    resultSet.getDouble("fat_gr"),
                    resultSet.getDouble("carbs_gr"),
                    resultSet.getDouble("protein_gr")
                ));
            }
        }

        return savedMeals;
    }

    public List<Per100Food> listPer100Foods() throws SQLException {
        String sql = "{CALL list_per_100()}";
        List<Per100Food> foods = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                foods.add(new Per100Food(
                    resultSet.getInt("per_100_food_id"),
                    resultSet.getString("food_name"),
                    resultSet.getDouble("calories"),
                    resultSet.getDouble("fat_gr"),
                    resultSet.getDouble("carbs_gr"),
                    resultSet.getDouble("protein_gr")
                ));
            }
        }

        return foods;
    }

    public List<TodayMeal> listTodayMeals() throws SQLException {
        String sql = "select meal_id, meal_name, calories, fat_gr, carbs_gr, protein_gr from meal where meal_date = curdate() order by meal_id desc";
        List<TodayMeal> meals = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                meals.add(new TodayMeal(
                    resultSet.getInt("meal_id"),
                    resultSet.getString("meal_name"),
                    resultSet.getDouble("calories"),
                    resultSet.getDouble("fat_gr"),
                    resultSet.getDouble("carbs_gr"),
                    resultSet.getDouble("protein_gr")
                ));
            }
        }

        return meals;
    }

    public void addMealToToday(String mealName, double calories, double fatGr, double carbsGr, double proteinGr) throws SQLException {
        String sql = "{CALL add_meal_to_today(?, ?, ?, ?, ?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, mealName);
            statement.setDouble(2, calories);
            statement.setDouble(3, fatGr);
            statement.setDouble(4, carbsGr);
            statement.setDouble(5, proteinGr);
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

    public void addPer100FoodToToday(String foodName, double quantity) throws SQLException {
        String sql = "{CALL add_per_100_food_to_today(?, ?)}";

        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, foodName);
            statement.setDouble(2, quantity);
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

    public void deletePer100Food(String foodName) throws SQLException {
        String sql = "delete from per_100_food where food_name = ?";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, foodName);
            statement.executeUpdate();
        }
    }

    public void deleteTodayMeal(int mealId) throws SQLException {
        String sql = "delete from meal where meal_id = ?";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, mealId);
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
                    resultSet.getDouble("calories"),
                    resultSet.getDouble("fat"),
                    resultSet.getDouble("carbs"),
                    resultSet.getDouble("protein")
                );
            }
        }

        return null;
    }
}
