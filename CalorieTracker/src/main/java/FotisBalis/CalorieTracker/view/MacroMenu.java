package FotisBalis.CalorieTracker.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MacroMenu extends JPanel {

    public MacroMenu(AppNavigator navigator) {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Macros Database", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton allFoodsButton = new JButton("All Foods");
        JButton favoritesButton = new JButton("Favorite Foods");
        JButton manualFoodsButton = new JButton("Manual Foods");
        JButton backButton = new JButton("Back");

        Dimension buttonSize = new Dimension(200, 38);
        configureButton(allFoodsButton, buttonSize);
        configureButton(favoritesButton, buttonSize);
        configureButton(manualFoodsButton, buttonSize);
        configureButton(backButton, buttonSize);

        allFoodsButton.addActionListener(e -> navigator.showAllMacros());
        favoritesButton.addActionListener(e -> navigator.showFavoriteMacros());
        manualFoodsButton.addActionListener(e -> navigator.showManualMacros());
        backButton.addActionListener(e -> navigator.goBack());

        buttonPanel.add(allFoodsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(favoritesButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(manualFoodsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(backButton);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(buttonPanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(contentPanel, new GridBagConstraints());

        add(centerPanel, BorderLayout.CENTER);
    }

    private void configureButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(CENTER_ALIGNMENT);
    }
}
