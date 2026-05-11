package FotisBalis.CalorieTracker.model;

import java.time.LocalDate;

public class TodayTotals {
    private final LocalDate mealDate;
    private final int calories;
    private final int fat;
    private final int carbs;
    private final int protein;

    public TodayTotals(LocalDate mealDate, int calories, int fat, int carbs, int protein) {
        this.mealDate = mealDate;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public int getCalories() {
        return calories;
    }

    public int getFat() {
        return fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getProtein() {
        return protein;
    }
}
