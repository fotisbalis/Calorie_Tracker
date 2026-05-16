package FotisBalis.CalorieTracker.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import FotisBalis.CalorieTracker.model.Per100Food;

public class AllMacrosMenu extends MacroFoodsMenu {
    private final JTextField searchField;

    public AllMacrosMenu(AppNavigator navigator) {
        super(navigator, "All Foods", "Nothing found.");
        searchField = new JTextField();

        JPanel searchPanel = new JPanel(new java.awt.BorderLayout(8, 0));
        searchPanel.add(new JLabel("Search:"), java.awt.BorderLayout.WEST);
        searchPanel.add(searchField, java.awt.BorderLayout.CENTER);
        setTopContent(searchPanel);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                initializeSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                initializeSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                initializeSearch();
            }

            private void initializeSearch() {
                loadFoods();
            }
        });

        initializeFoodsView();
    }

    @Override
    protected List<Per100Food> fetchFoods() throws SQLException {
        List<Per100Food> foods = mealController.listPer100Foods();
        String searchText = searchField.getText().trim().toLowerCase(Locale.ROOT);

        if (searchText.isEmpty()) {
            return foods;
        }

        List<Per100Food> filteredFoods = new ArrayList<>();
        for (Per100Food food : foods) {
            if (food.getFoodName().toLowerCase(Locale.ROOT).contains(searchText)) {
                filteredFoods.add(food);
            }
        }

        return filteredFoods;
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
