package FotisBalis.CalorieTracker.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.UIManager;

import FotisBalis.CalorieTracker.controller.MealController;
import FotisBalis.CalorieTracker.model.TodayTotals;

public class MainMenu extends JFrame {
    private final MealController mealController;

    public MainMenu() {
        this.mealController = new MealController();

        setTitle("Calorie Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 500));

        JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Calorie Tracker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton addMealButton = new JButton("Add Meal");
        JButton savedMealsButton = new JButton("Saved Meals");
        JButton todayTotalsButton = new JButton("Today's Totals");
        JButton exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(180, 36);
        configureButton(addMealButton, buttonSize);
        configureButton(savedMealsButton, buttonSize);
        configureButton(todayTotalsButton, buttonSize);
        configureButton(exitButton, buttonSize);

        addMealButton.addActionListener(e -> showAddMealDialog());
        savedMealsButton.addActionListener(e -> openSavedMealsMenu());
        todayTotalsButton.addActionListener(e -> showTodayTotalsDialog());
        exitButton.addActionListener(e -> dispose());

        buttonPanel.add(addMealButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(savedMealsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(todayTotalsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(exitButton);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(buttonPanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(contentPanel, new GridBagConstraints());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void configureButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void showAddMealDialog() {
        JTextField mealNameField = new JTextField();
        JTextField caloriesField = new JTextField();
        JTextField fatField = new JTextField();
        JTextField carbsField = new JTextField();
        JTextField proteinField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Meal Name:"));
        panel.add(mealNameField);
        panel.add(new JLabel("Calories:"));
        panel.add(caloriesField);
        panel.add(new JLabel("Fat (gr):"));
        panel.add(fatField);
        panel.add(new JLabel("Carbs (gr):"));
        panel.add(carbsField);
        panel.add(new JLabel("Protein (gr):"));
        panel.add(proteinField);

        Object[] options = { "Add To Today", "Save Meal", "Cancel" };
        int choice = JOptionPane.showOptionDialog(
            this,
            panel,
            "Add Meal",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        try {
            String mealName = mealNameField.getText().trim();
            validateMealName(mealName);

            int calories = parseIntegerField(caloriesField, "Calories");
            int fat = parseIntegerField(fatField, "Fat");
            int carbs = parseIntegerField(carbsField, "Carbs");
            int protein = parseIntegerField(proteinField, "Protein");

            if (choice == 0) {
                mealController.addMealToToday(mealName, calories, fat, carbs, protein);
                showInfoMessage("Meal added to today.");
            } else if (choice == 1) {
                mealController.newSavedMeal(mealName, calories, fat, carbs, protein);
                showInfoMessage("Meal saved.");
            }
        } catch (IllegalArgumentException ex) {
            showErrorMessage(ex.getMessage());
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private void openSavedMealsMenu() {
        SavedMealsMenu savedMealsMenu = new SavedMealsMenu(this);
        savedMealsMenu.setVisible(true);
    }

    private void showTodayTotalsDialog() {
        try {
            TodayTotals totals = mealController.showTodayTotals();

            if (totals == null) {
                showInfoMessage("No meals found for today.");
                return;
            }

            String message = String.format(
                "Date: %s%nCalories: %d%nFat: %d gr%nCarbs: %d gr%nProtein: %d gr",
                totals.getMealDate(),
                totals.getCalories(),
                totals.getFat(),
                totals.getCarbs(),
                totals.getProtein()
            );

            showInfoMessage(message);
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private int parseIntegerField(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    private void validateMealName(String mealName) {
        if (mealName.isEmpty()) {
            throw new IllegalArgumentException("Meal name is required.");
        }
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Calorie Tracker", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Calorie Tracker", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }
}
