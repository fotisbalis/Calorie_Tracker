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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MacroMenu extends JFrame {

    public MacroMenu(JFrame parent) {
        setTitle("Macros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(500, 420));

        JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Macros", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton allFoodsButton = new JButton("All Foods");
        JButton favoritesButton = new JButton("Favorite Macros");
        JButton backButton = new JButton("Back To Main Menu");

        Dimension buttonSize = new Dimension(200, 38);
        configureButton(allFoodsButton, buttonSize);
        configureButton(favoritesButton, buttonSize);
        configureButton(backButton, buttonSize);

        allFoodsButton.addActionListener(e -> {
            AllMacrosMenu allMacrosMenu = new AllMacrosMenu(parent);
            allMacrosMenu.setVisible(true);
            dispose();
        });

        favoritesButton.addActionListener(e -> {
            FavoriteMacrosMenu favoriteMacrosMenu = new FavoriteMacrosMenu(parent);
            favoriteMacrosMenu.setVisible(true);
            dispose();
        });

        backButton.addActionListener(e -> dispose());

        buttonPanel.add(allFoodsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(favoritesButton);
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

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void configureButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(CENTER_ALIGNMENT);
    }
}
