package FotisBalis.CalorieTracker.view;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import FotisBalis.CalorieTracker.model.Per100Food;

public class ManualMacrosMenu extends MacroFoodsMenu {

    public ManualMacrosMenu(AppNavigator navigator) {
        super(navigator, "Manual Foods", "Manually Added Foods", "No manual foods found.");
        initializeFoodsView();
    }

    @Override
    protected List<Per100Food> fetchFoods() throws SQLException {
        return mealController.listManualPer100Foods();
    }

    @Override
    protected JButton createFavoriteButton(Per100Food food) {
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteManualFood(food));
        return deleteButton;
    }

    private void deleteManualFood(Per100Food food) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Delete manual food \"" + food.getFoodName() + "\"?",
            "Delete Manual Food",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            mealController.deleteManualPer100Food(food.getPer100FoodId());
            loadFoods();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Manual Foods", JOptionPane.ERROR_MESSAGE);
        }
    }
}
