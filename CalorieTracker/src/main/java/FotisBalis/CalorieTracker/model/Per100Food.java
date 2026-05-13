package FotisBalis.CalorieTracker.model;

public class Per100Food {
    private final int per100FoodId;
    private final String foodName;
    private final double calories;
    private final double fatGr;
    private final double carbsGr;
    private final double proteinGr;

    public Per100Food(int per100FoodId, String foodName, double calories, double fatGr, double carbsGr, double proteinGr) {
        this.per100FoodId = per100FoodId;
        this.foodName = foodName;
        this.calories = calories;
        this.fatGr = fatGr;
        this.carbsGr = carbsGr;
        this.proteinGr = proteinGr;
    }

    public int getPer100FoodId() {
        return per100FoodId;
    }

    public String getFoodName() {
        return foodName;
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
