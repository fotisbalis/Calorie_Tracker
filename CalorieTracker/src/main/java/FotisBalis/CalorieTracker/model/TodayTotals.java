package FotisBalis.CalorieTracker.model;

import java.time.LocalDate;

public class TodayTotals {
    private final LocalDate mealDate;
    private final double calories;
    private final double fat;
    private final double carbs;
    private final double protein;

    public TodayTotals(LocalDate mealDate, double calories, double fat, double carbs, double protein) {
        this.mealDate = mealDate;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public double getCalories() {
        return calories;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getProtein() {
        return protein;
    }
}
