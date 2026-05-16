package FotisBalis.CalorieTracker.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import FotisBalis.CalorieTracker.controller.MealController;
import FotisBalis.CalorieTracker.model.Per100Food;

public abstract class MacroFoodsMenu extends JPanel {
    protected final MealController mealController;
    private final AppNavigator navigator;
    private final JPanel contentPanel;
    private final JPanel foodsPanel;
    private final String emptyMessage;
    private final String title;

    protected MacroFoodsMenu(AppNavigator navigator, String title, String emptyMessage) {
        this(navigator, title, "USDA FoodData Central", emptyMessage);
    }

    protected MacroFoodsMenu(AppNavigator navigator, String title, String subtitle, String emptyMessage) {
        this.mealController = new MealController();
        this.navigator = navigator;
        this.emptyMessage = emptyMessage;
        this.title = title;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout(0, 12));

        foodsPanel = new JPanel();
        foodsPanel.setLayout(new BoxLayout(foodsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(foodsPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> this.navigator.goBack());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    protected final void setTopContent(JPanel panel) {
        contentPanel.add(panel, BorderLayout.NORTH);
    }

    protected final void initializeFoodsView() {
        loadFoods();
    }

    protected void loadFoods() {
        foodsPanel.removeAll();

        try {
            List<Per100Food> foods = fetchFoods();

            if (foods.isEmpty()) {
                JLabel emptyLabel = new JLabel(emptyMessage, SwingConstants.CENTER);
                emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
                foodsPanel.add(emptyLabel);
            } else {
                for (Per100Food food : foods) {
                    foodsPanel.add(createFoodRow(food));
                }
            }
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }

        foodsPanel.revalidate();
        foodsPanel.repaint();
    }

    private JPanel createFoodRow(Per100Food food) {
        JPanel rowPanel = new JPanel(new BorderLayout(12, 12));
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 12, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel nameLabel = new JLabel("<html><body style='width: 520px'>" + food.getFoodName() + "</body></html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        rowPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel nutritionPanel = new JPanel(new GridLayout(2, 2, 8, 4));
        nutritionPanel.add(new JLabel("Calories: " + formatDecimal(food.getCalories())));
        nutritionPanel.add(new JLabel("Fat: " + formatDecimal(food.getFatGr()) + " gr"));
        nutritionPanel.add(new JLabel("Carbs: " + formatDecimal(food.getCarbsGr()) + " gr"));
        nutritionPanel.add(new JLabel("Protein: " + formatDecimal(food.getProteinGr()) + " gr"));
        rowPanel.add(nutritionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 8, 0));

        JButton addButton = new JButton("Add To Today's Meals");
        addButton.addActionListener(e -> addFoodToToday(food));

        JButton favoriteButton = createFavoriteButton(food);

        buttonPanel.add(addButton);
        buttonPanel.add(favoriteButton);
        rowPanel.add(buttonPanel, BorderLayout.SOUTH);

        return rowPanel;
    }

    protected abstract List<Per100Food> fetchFoods() throws SQLException;

    protected abstract JButton createFavoriteButton(Per100Food food);

    protected void setFavorite(Per100Food food, boolean favorite, String successMessage) {
        try {
            mealController.setPer100FoodFavorite(food.getPer100FoodId(), favorite);
            JOptionPane.showMessageDialog(this, successMessage, title, JOptionPane.INFORMATION_MESSAGE);
            loadFoods();
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private void addFoodToToday(Per100Food food) {
        JTextField quantityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Food:"));
        panel.add(new JLabel(food.getFoodName()));
        panel.add(new JLabel("Quantity (gr):"));
        panel.add(quantityField);

        int choice = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add Per 100g Food",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            double quantity = parseDecimalField(quantityField, "Quantity");
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }
            mealController.addPer100FoodToToday(food.getFoodName(), quantity);
            JOptionPane.showMessageDialog(
                this,
                food.getFoodName() + " added to today's meals.",
                title,
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            showErrorMessage(ex.getMessage());
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private double parseDecimalField(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
