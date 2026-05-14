package FotisBalis.CalorieTracker.view;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import FotisBalis.CalorieTracker.model.Per100Food;

public class AllMacrosMenu extends MacroFoodsMenu {

    public AllMacrosMenu(JFrame parent) {
        super(parent, "All Foods", "No foods found in per_100_food.");
    }

    @Override
    protected List<Per100Food> fetchFoods() throws SQLException {
        return mealController.listPer100Foods();
    }

    @Override
    protected JButton createFavoriteButton(Per100Food food) {
        JButton favoriteButton = new JButton(food.isFavorite() ? "Remove From Favorites" : "Add To Favorites");
        favoriteButton.addActionListener(e -> {
            boolean nextFavoriteState = !food.isFavorite();
            String message = nextFavoriteState
                ? food.getFoodName() + " added to favorites."
                : food.getFoodName() + " removed from favorites.";
            setFavorite(food, nextFavoriteState, message);
        });
        return favoriteButton;
    }
}
