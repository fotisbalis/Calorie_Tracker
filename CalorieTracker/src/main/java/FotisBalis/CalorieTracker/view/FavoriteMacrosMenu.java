package FotisBalis.CalorieTracker.view;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import FotisBalis.CalorieTracker.model.Per100Food;

public class FavoriteMacrosMenu extends MacroFoodsMenu {

    public FavoriteMacrosMenu(JFrame parent) {
        super(parent, "Favorite Macros", "No favorite macros found.");
    }

    @Override
    protected List<Per100Food> fetchFoods() throws SQLException {
        return mealController.listFavoritePer100Foods();
    }

    @Override
    protected JButton createFavoriteButton(Per100Food food) {
        JButton favoriteButton = new JButton("Remove From Favorites");
        favoriteButton.addActionListener(e ->
            setFavorite(food, false, food.getFoodName() + " removed from favorites.")
        );
        return favoriteButton;
    }
}
