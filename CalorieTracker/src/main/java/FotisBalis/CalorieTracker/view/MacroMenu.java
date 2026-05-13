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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import FotisBalis.CalorieTracker.controller.MealController;
import FotisBalis.CalorieTracker.model.Per100Food;

public class MacroMenu extends JFrame {
    private final MealController mealController;
    private final JPanel foodsPanel;

    public MacroMenu(JFrame parent) {
        this.mealController = new MealController();

        setTitle("Macros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel("Macros per 100gr", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        foodsPanel = new JPanel();
        foodsPanel.setLayout(new BoxLayout(foodsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(foodsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(closeButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        loadFoods();
    }

    private void loadFoods() {
        foodsPanel.removeAll();

        try {
            List<Per100Food> foods = mealController.listPer100Foods();

            if (foods.isEmpty()) {
                JLabel emptyLabel = new JLabel("No foods found.", SwingConstants.CENTER);
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
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel nameLabel = new JLabel(food.getFoodName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        rowPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel nutritionPanel = new JPanel(new GridLayout(2, 2, 8, 4));
        nutritionPanel.add(new JLabel("Calories: " + formatDecimal(food.getCalories())));
        nutritionPanel.add(new JLabel("Fat: " + formatDecimal(food.getFatGr()) + " gr"));
        nutritionPanel.add(new JLabel("Carbs: " + formatDecimal(food.getCarbsGr()) + " gr"));
        nutritionPanel.add(new JLabel("Protein: " + formatDecimal(food.getProteinGr()) + " gr"));
        rowPanel.add(nutritionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));

        JButton addButton = new JButton("Add To Today's Meals");
        addButton.addActionListener(e -> addFoodToToday(food));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteFood(food));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        rowPanel.add(buttonPanel, BorderLayout.EAST);

        return rowPanel;
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
                "Macros",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            showErrorMessage(ex.getMessage());
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private void deleteFood(Per100Food food) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Delete per 100g food \"" + food.getFoodName() + "\"?",
            "Delete Macro Food",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            mealController.deletePer100Food(food.getFoodName());
            loadFoods();
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
        JOptionPane.showMessageDialog(this, message, "Macros", JOptionPane.ERROR_MESSAGE);
    }
}
