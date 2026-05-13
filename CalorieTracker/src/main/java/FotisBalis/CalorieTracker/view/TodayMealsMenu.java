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
import FotisBalis.CalorieTracker.model.TodayMeal;
import FotisBalis.CalorieTracker.model.TodayTotals;

public class TodayMealsMenu extends JFrame {
    private final MealController mealController;
    private final JPanel mealsPanel;
    private final JLabel totalsLabel;

    public TodayMealsMenu(JFrame parent) {
        this.mealController = new MealController();

        setTitle("Today's Meals");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 520);
        setMinimumSize(new Dimension(760, 520));
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 8));
        JLabel titleLabel = new JLabel("Today's Meals", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        totalsLabel = new JLabel("", SwingConstants.CENTER);
        headerPanel.add(totalsLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

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
        loadTodayMeals();
    }

    private void loadTodayMeals() {
        mealsPanel.removeAll();

        try {
            List<TodayMeal> meals = mealController.listTodayMeals();
            TodayTotals totals = mealController.showTodayTotals();

            if (totals != null) {
                totalsLabel.setText(String.format(
                    "Totals: Calories %.2f | Fat %.2f gr | Carbs %.2f gr | Protein %.2f gr",
                    totals.getCalories(),
                    totals.getFat(),
                    totals.getCarbs(),
                    totals.getProtein()
                ));
            } else {
                totalsLabel.setText("No meals found for today.");
            }

            for (TodayMeal meal : meals) {
                mealsPanel.add(createMealRow(meal));
            }
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }

        mealsPanel.revalidate();
        mealsPanel.repaint();
    }

    private JPanel createMealRow(TodayMeal meal) {
        JPanel rowPanel = new JPanel(new BorderLayout(12, 12));
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 12, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel nameLabel = new JLabel(meal.getMealName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        rowPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel nutritionPanel = new JPanel(new GridLayout(2, 2, 8, 4));
        nutritionPanel.add(new JLabel("Calories: " + formatDecimal(meal.getCalories())));
        nutritionPanel.add(new JLabel("Fat: " + formatDecimal(meal.getFatGr()) + " gr"));
        nutritionPanel.add(new JLabel("Carbs: " + formatDecimal(meal.getCarbsGr()) + " gr"));
        nutritionPanel.add(new JLabel("Protein: " + formatDecimal(meal.getProteinGr()) + " gr"));
        rowPanel.add(nutritionPanel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteTodayMeal(meal));
        rowPanel.add(deleteButton, BorderLayout.EAST);

        return rowPanel;
    }

    private void deleteTodayMeal(TodayMeal meal) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Delete \"" + meal.getMealName() + "\" from today's meals?",
            "Delete Today's Meal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            mealController.deleteTodayMeal(meal.getMealId());
            loadTodayMeals();
        } catch (SQLException ex) {
            showErrorMessage("Database error: " + ex.getMessage());
        }
    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Today's Meals", JOptionPane.ERROR_MESSAGE);
    }
}
