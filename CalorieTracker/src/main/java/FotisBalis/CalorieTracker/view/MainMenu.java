package FotisBalis.CalorieTracker.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

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

public class MainMenu extends JFrame implements AppNavigator {
    private static final String HOME = "home";
    private static final String SAVED_MEALS = "saved_meals";
    private static final String MACRO_MENU = "macro_menu";
    private static final String ALL_MACROS = "all_macros";
    private static final String FAVORITE_MACROS = "favorite_macros";
    private static final String MANUAL_MACROS = "manual_macros";
    private static final String TODAY_MEALS = "today_meals";

    private final MealController mealController;
    private final Deque<String> history;
    private final JPanel rootPanel;
    private String currentScreen;

    public MainMenu() {
        this.mealController = new MealController();
        this.history = new ArrayDeque<>();

        setTitle("Calorie Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(760, 520));

        rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        showScreen(HOME, false);
    }

    @Override
    public void showHome() {
        history.clear();
        showScreen(HOME, false);
    }

    @Override
    public void showSavedMeals() {
        showScreen(SAVED_MEALS, true);
    }

    @Override
    public void showMacroMenu() {
        showScreen(MACRO_MENU, true);
    }

    @Override
    public void showAllMacros() {
        showScreen(ALL_MACROS, true);
    }

    @Override
    public void showFavoriteMacros() {
        showScreen(FAVORITE_MACROS, true);
    }

    @Override
    public void showManualMacros() {
        showScreen(MANUAL_MACROS, true);
    }

    @Override
    public void showTodayMeals() {
        showScreen(TODAY_MEALS, true);
    }

    @Override
    public void goBack() {
        if (history.isEmpty()) {
            showScreen(HOME, false);
            return;
        }

        String previousScreen = history.pop();
        showScreen(previousScreen, false);
    }

    private void showScreen(String screenId, boolean addToHistory) {
        if (addToHistory && currentScreen != null) {
            history.push(currentScreen);
        }

        currentScreen = screenId;
        rootPanel.removeAll();
        rootPanel.add(createScreen(screenId), BorderLayout.CENTER);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    private JPanel createScreen(String screenId) {
        switch (screenId) {
            case SAVED_MEALS:
                return new SavedMealsMenu(this);
            case MACRO_MENU:
                return new MacroMenu(this);
            case ALL_MACROS:
                return new AllMacrosMenu(this);
            case FAVORITE_MACROS:
                return new FavoriteMacrosMenu(this);
            case MANUAL_MACROS:
                return new ManualMacrosMenu(this);
            case TODAY_MEALS:
                return new TodayMealsMenu(this);
            case HOME:
            default:
                return buildHomePanel();
        }
    }

    private JPanel buildHomePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Calorie Tracker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton addMealButton = new JButton("Add New Meal");
        JButton savedMealsButton = new JButton("Saved Meals");
        JButton macrosButton = new JButton("Add Meal from Database");
        JButton todayTotalsButton = new JButton("Today's Totals");
        JButton exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(180, 36);
        configureButton(addMealButton, buttonSize);
        configureButton(savedMealsButton, buttonSize);
        configureButton(macrosButton, buttonSize);
        configureButton(todayTotalsButton, buttonSize);
        configureButton(exitButton, buttonSize);

        addMealButton.addActionListener(e -> showAddMealDialog());
        savedMealsButton.addActionListener(e -> showSavedMeals());
        macrosButton.addActionListener(e -> showMacroMenu());
        todayTotalsButton.addActionListener(e -> showTodayMeals());
        exitButton.addActionListener(e -> dispose());

        buttonPanel.add(addMealButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(savedMealsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(macrosButton);
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

        return mainPanel;
    }

    private void configureButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void showAddMealDialog() {
        JTextField mealNameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField caloriesField = new JTextField();
        JTextField fatField = new JTextField();
        JTextField carbsField = new JTextField();
        JTextField proteinField = new JTextField();
        JLabel noteLabel = new JLabel("*Quantity is required only when adding a food to the database.");

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Meal Name:"));
        panel.add(mealNameField);
        panel.add(new JLabel("Quantity (gr):"));
        panel.add(quantityField);
        panel.add(new JLabel("Calories:"));
        panel.add(caloriesField);
        panel.add(new JLabel("Fat (gr):"));
        panel.add(fatField);
        panel.add(new JLabel("Carbs (gr):"));
        panel.add(carbsField);
        panel.add(new JLabel("Protein (gr):"));
        panel.add(proteinField);
        panel.add(noteLabel);
        panel.add(new JLabel(""));

        Object[] options = { "Add To Today", "Save Meal", "Add To Database", "Close" };

        while (true) {
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

            if (choice == 3 || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }

            try {
                String mealName = mealNameField.getText().trim();
                validateMealName(mealName);
                double quantity = 0;
                if (choice == 2) {
                    quantity = parseDecimalField(quantityField, "Quantity");
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Quantity must be greater than 0.");
                    }
                }

                double calories = parseDecimalField(caloriesField, "Calories");
                double fat = parseDecimalField(fatField, "Fat");
                double carbs = parseDecimalField(carbsField, "Carbs");
                double protein = parseDecimalField(proteinField, "Protein");

                if (choice == 0) {
                    mealController.addMealToToday(mealName, calories, fat, carbs, protein);
                    showInfoMessage("Meal added to today.");
                } else if (choice == 1) {
                    mealController.newSavedMeal(mealName, calories, fat, carbs, protein);
                    showInfoMessage("Meal saved.");
                } else if (choice == 2) {
                    mealController.newPer100Meal(mealName, quantity, calories, fat, carbs, protein);
                    showInfoMessage("Meal added to database.");
                }
            } catch (IllegalArgumentException ex) {
                showErrorMessage(ex.getMessage());
            } catch (SQLException ex) {
            	if(choice == 1)
                	showErrorMessage("Meal with that name has already been saved.");
            	else if(choice == 2)
            		showErrorMessage("Food with that name has already been added.");
            	else
            		showErrorMessage("Database error: " + ex.getMessage());
            }
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
