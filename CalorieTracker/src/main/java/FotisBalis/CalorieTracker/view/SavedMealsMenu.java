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
import javax.swing.SwingConstants;

import FotisBalis.CalorieTracker.controller.MealController;
import FotisBalis.CalorieTracker.model.SavedMeal;

public class SavedMealsMenu extends JFrame {
    private final MealController mealController;
    private final JPanel mealsPanel;

    public SavedMealsMenu(JFrame parent) {
        this.mealController = new MealController();

        setTitle("Saved Meals");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel("Saved Meals", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        mealsPanel = new JPanel();
        mealsPanel.setLayout(new BoxLayout(mealsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(mealsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");

        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(closeButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        loadSavedMeals();
    }

    private void loadSavedMeals() {
        mealsPanel.removeAll();

        try {
            List<SavedMeal> savedMeals = mealController.listSavedMeals();

            if (savedMeals.isEmpty()) {
                JLabel emptyLabel = new JLabel("No saved meals found.", SwingConstants.CENTER);
                emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
                mealsPanel.add(emptyLabel);
            } else {
                for (SavedMeal savedMeal : savedMeals) {
                    mealsPanel.add(createMealRow(savedMeal));
                }
            }
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }

        mealsPanel.revalidate();
        mealsPanel.repaint();
    }

    private JPanel createMealRow(SavedMeal savedMeal) {
        JPanel rowPanel = new JPanel(new BorderLayout(12, 12));
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 12, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel nameLabel = new JLabel(savedMeal.getMealName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        rowPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel nutritionPanel = new JPanel(new GridLayout(2, 2, 8, 4));
        nutritionPanel.add(new JLabel("Calories: " + savedMeal.getCalories()));
        nutritionPanel.add(new JLabel("Fat: " + savedMeal.getFatGr() + " gr"));
        nutritionPanel.add(new JLabel("Carbs: " + savedMeal.getCarbsGr() + " gr"));
        nutritionPanel.add(new JLabel("Protein: " + savedMeal.getProteinGr() + " gr"));
        rowPanel.add(nutritionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));

        JButton addButton = new JButton("Add To Today's Meals");
        addButton.addActionListener(e -> addSavedMealToToday(savedMeal));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSavedMeal(savedMeal));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        rowPanel.add(buttonPanel, BorderLayout.EAST);

        return rowPanel;
    }

    private void addSavedMealToToday(SavedMeal savedMeal) {
        try {
            mealController.addSavedMealToToday(savedMeal.getMealName());
            JOptionPane.showMessageDialog(
                this,
                savedMeal.getMealName() + " added to today's meals.",
                "Saved Meals",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private void deleteSavedMeal(SavedMeal savedMeal) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Delete saved meal \"" + savedMeal.getMealName() + "\"?",
            "Delete Saved Meal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            mealController.deleteSavedMeal(savedMeal.getMealName());
            loadSavedMeals();
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Saved Meals", JOptionPane.ERROR_MESSAGE);
    }
}
