package FotisBalis.CalorieTracker.model;

public class SavedMeal {
    private final int savedMealId;
    private final String mealName;
    private final int calories;
    private final int fatGr;
    private final int carbsGr;
    private final int proteinGr;

    public SavedMeal(int savedMealId, String mealName, int calories, int fatGr, int carbsGr, int proteinGr) {
        this.savedMealId = savedMealId;
        this.mealName = mealName;
        this.calories = calories;
        this.fatGr = fatGr;
        this.carbsGr = carbsGr;
        this.proteinGr = proteinGr;
    }

    public int getSavedMealId() {
        return savedMealId;
    }

    public String getMealName() {
        return mealName;
    }

    public int getCalories() {
        return calories;
    }

    public int getFatGr() {
        return fatGr;
    }

    public int getCarbsGr() {
        return carbsGr;
    }

    public int getProteinGr() {
        return proteinGr;
    }
}
