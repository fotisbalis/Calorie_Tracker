package FotisBalis.CalorieTracker.model;

public class TodayMeal {
    private final int mealId;
    private final String mealName;
    private final double calories;
    private final double fatGr;
    private final double carbsGr;
    private final double proteinGr;

    public TodayMeal(int mealId, String mealName, double calories, double fatGr, double carbsGr, double proteinGr) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.calories = calories;
        this.fatGr = fatGr;
        this.carbsGr = carbsGr;
        this.proteinGr = proteinGr;
    }

    public int getMealId() {
        return mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public double getCalories() {
        return calories;
    }

    public double getFatGr() {
        return fatGr;
    }

    public double getCarbsGr() {
        return carbsGr;
    }

    public double getProteinGr() {
        return proteinGr;
    }
}
