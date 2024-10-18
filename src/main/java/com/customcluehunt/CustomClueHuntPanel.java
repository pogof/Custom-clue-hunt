package com.customcluehunt;

import javax.swing.*;
import net.runelite.client.ui.PluginPanel;

import java.awt.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomClueHuntPanel extends PluginPanel {

    private final JPanel mainMenuPanel = new JPanel();

    private final JPanel creationModePanel = new JPanel();
    private final JScrollPane creationModeScrollPane = new JScrollPane(creationModePanel);
    private final GridBagConstraints gbc = new GridBagConstraints();

    private final CardLayout cardLayout = new CardLayout();

    private final JButton createCustomClueButton = new JButton("Create Custom Clue");
    private final JButton solveCustomClueButton = new JButton("Solve Custom Clue");

    private final JButton backButton = new JButton("Back to Main Menu");

    private final JLabel messageLabel = new JLabel(); // Add this line

    private final JButton addClueStepButton = new JButton("Add Clue Step");
    private int clueStepCounter = 1;

    public CustomClueHuntPanel() {
        super();
        setLayout(cardLayout);

        setupMainMenuPanel();
        setupCreationModePanel();

        add(mainMenuPanel, "MainMenu");
        add(creationModePanel, "CreationMode");

        cardLayout.show(this, "MainMenu");



    }

    private void setupMainMenuPanel() {
        mainMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 10, 10, 10);

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;

        mainMenuPanel.add(new JLabel("Custom Clue Hunt"));
        //add(new JLabel("Custom Clue Hunt"));

        gbcMain.gridy++;
        mainMenuPanel.add(createCustomClueButton, gbcMain);

        gbcMain.gridy++;
        mainMenuPanel.add(solveCustomClueButton, gbcMain);

        createCustomClueButton.addActionListener(e -> cardLayout.show(CustomClueHuntPanel.this, "CreationMode"));
    }

    private void setupCreationModePanel() {
        creationModePanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        creationModePanel.add(new JLabel("Creation Mode"), gbc);

        gbc.gridy++;
        creationModePanel.add(backButton, gbc);
        backButton.addActionListener(e -> {
            cardLayout.show(CustomClueHuntPanel.this, "MainMenu");
            resetCreationModePanel(); // Reset the panel when switching back to main menu
        });

        gbc.gridy++;
        creationModePanel.add(addClueStepButton, gbc);
        addClueStepButton.addActionListener(e -> addClueStep());

        // Wrap the creationModePanel in a JScrollPane
        creationModeScrollPane.setViewportView(creationModePanel);
        creationModeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        creationModeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void resetCreationModePanel() {
        creationModePanel.removeAll(); // Remove all components
        gbc.gridx = 0;
        gbc.gridy = 0;

        creationModePanel.add(new JLabel("Creation Mode"), gbc);

        gbc.gridy++;
        creationModePanel.add(backButton, gbc);

        gbc.gridy++;
        creationModePanel.add(addClueStepButton, gbc);

        creationModePanel.revalidate();
        creationModePanel.repaint();
        clueStepCounter = 1;
    }


    private void addClueStep() {
        gbc.gridx = 0;
        gbc.gridy = creationModePanel.getComponentCount(); // Get the current number of components

        JLabel clueStepLabel = new JLabel("Clue Step #" + clueStepCounter + " hint text");
        JTextArea clueStepTextArea = new JTextArea(3, 20);
        String[] options = {"Dig", "Emote", "Wear", "Kill"};
        JComboBox<String> clueStepComboBox = new JComboBox<>(options);
        JButton addSubstepButton = new JButton("Add Substep");



        creationModePanel.add(clueStepLabel, gbc);
        gbc.gridy++;
        creationModePanel.add(new JScrollPane(clueStepTextArea), gbc); // Wrap JTextArea in JScrollPane
        gbc.gridy++;

        // Placeholder for substeps
        JPanel substepsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints substepsGbc = new GridBagConstraints();
        substepsGbc.gridx = 0;
        substepsGbc.gridy = 0;
        substepsGbc.anchor = GridBagConstraints.WEST;
        creationModePanel.add(substepsPanel, gbc);
        gbc.gridy++;

        creationModePanel.add(clueStepComboBox, gbc);
        gbc.gridy++;
        creationModePanel.add(addSubstepButton, gbc);
        gbc.gridy++;
        creationModePanel.add(new JLabel("========================"), gbc);


        gbc.gridy++;
        creationModePanel.add(addClueStepButton, gbc); // Move the "Add Clue Step" button to the bottom

        creationModePanel.revalidate();
        creationModePanel.repaint();

        clueStepCounter++;

        // Add action listener for the "Add Substep" button
        addSubstepButton.addActionListener(e -> {
            String selectedOption = (String) clueStepComboBox.getSelectedItem();
            if ("Dig".equals(selectedOption)) {
                addDigSubstep(substepsPanel, substepsGbc);
            } else if ("Emote".equals(selectedOption)) {
                addEmoteSubstep(substepsPanel, substepsGbc);
            }else if ("Kill".equals(selectedOption)) {
                addKillSubstep(substepsPanel, substepsGbc);
            }
            // Add more conditions here for other options if needed
        });
    }

    private void addDigSubstep(JPanel substepsPanel, GridBagConstraints substepsGbc) {
        JLabel digActionLabel = new JLabel("Dig action");
        JButton getLocationButton = new JButton("Get location");
        JLabel locationLabel = new JLabel("Location: ");
        JButton removeSubstepButton = new JButton("Remove Substep");

        substepsPanel.add(digActionLabel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(getLocationButton, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(locationLabel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(removeSubstepButton, substepsGbc);
        substepsGbc.gridy++;


        substepsPanel.revalidate();
        substepsPanel.repaint();

        // Action listener for "Get location" button
        getLocationButton.addActionListener(e -> {
            // Simulate getting the player's current X and Y location
            int playerX = 100; // Replace with actual method to get X location
            int playerY = 200; // Replace with actual method to get Y location
            locationLabel.setText("Location: X=" + playerX + ", Y=" + playerY);
        });

        // Action listener for "Remove Substep" button
        removeSubstepButton.addActionListener(e -> {
            substepsPanel.remove(digActionLabel);
            substepsPanel.remove(getLocationButton);
            substepsPanel.remove(locationLabel);
            substepsPanel.remove(removeSubstepButton);
            substepsPanel.revalidate();
            substepsPanel.repaint();
        });
    }

    private void addEmoteSubstep(JPanel substepsPanel, GridBagConstraints substepsGbc) {
        // Create panels for each label and text field pair
        JPanel emoteIdPanel = new JPanel(new BorderLayout());
        JLabel emoteIdLabel = new JLabel("Emote ID");
        JTextField emoteIdTextField = new JTextField(10);
        emoteIdTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                try {
                    Integer.parseInt(textField.getText());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });
        emoteIdPanel.add(emoteIdLabel, BorderLayout.NORTH);
        emoteIdPanel.add(emoteIdTextField, BorderLayout.SOUTH);

        JPanel x1Panel = new JPanel(new BorderLayout());
        JLabel x1Label = new JLabel("X1");
        JTextField x1TextField = new JTextField(5);
        x1Panel.add(x1Label, BorderLayout.NORTH);
        x1Panel.add(x1TextField, BorderLayout.SOUTH);

        JPanel x2Panel = new JPanel(new BorderLayout());
        JLabel x2Label = new JLabel("X2");
        JTextField x2TextField = new JTextField(5);
        x2Panel.add(x2Label, BorderLayout.NORTH);
        x2Panel.add(x2TextField, BorderLayout.SOUTH);

        JPanel y1Panel = new JPanel(new BorderLayout());
        JLabel y1Label = new JLabel("Y1");
        JTextField y1TextField = new JTextField(5);
        y1Panel.add(y1Label, BorderLayout.NORTH);
        y1Panel.add(y1TextField, BorderLayout.SOUTH);

        JPanel y2Panel = new JPanel(new BorderLayout());
        JLabel y2Label = new JLabel("Y2");
        JTextField y2TextField = new JTextField(5);
        y2Panel.add(y2Label, BorderLayout.NORTH);
        y2Panel.add(y2TextField, BorderLayout.SOUTH);

        JButton removeSubstepButton = new JButton("Remove Substep");

        // Add components to the substeps panel
        substepsPanel.add(new JLabel("Emote action"), substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(emoteIdPanel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(new JLabel("Area"), substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(x1Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(x2Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(y1Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(y2Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(removeSubstepButton, substepsGbc);
        substepsGbc.gridy++;

        substepsPanel.revalidate();
        substepsPanel.repaint();

        // Action listener for "Remove Substep" button
        removeSubstepButton.addActionListener(e -> {
            substepsPanel.removeAll();
            substepsPanel.revalidate();
            substepsPanel.repaint();
        });
    }

    private void addKillSubstep(JPanel substepsPanel, GridBagConstraints substepsGbc) {
        // Create panels for each label and text field pair
        JPanel NPCIdPanel = new JPanel(new BorderLayout());
        JLabel NPCIdLabel = new JLabel("NPC ID");
        JTextField NPCIdTextField = new JTextField(10);
        NPCIdTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                try {
                    Integer.parseInt(textField.getText());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });
        NPCIdPanel.add(NPCIdLabel, BorderLayout.NORTH);
        NPCIdPanel.add(NPCIdTextField, BorderLayout.SOUTH);

        JPanel x1Panel = new JPanel(new BorderLayout());
        JLabel x1Label = new JLabel("X1");
        JTextField x1TextField = new JTextField(5);
        x1Panel.add(x1Label, BorderLayout.NORTH);
        x1Panel.add(x1TextField, BorderLayout.SOUTH);

        JPanel x2Panel = new JPanel(new BorderLayout());
        JLabel x2Label = new JLabel("X2");
        JTextField x2TextField = new JTextField(5);
        x2Panel.add(x2Label, BorderLayout.NORTH);
        x2Panel.add(x2TextField, BorderLayout.SOUTH);

        JPanel y1Panel = new JPanel(new BorderLayout());
        JLabel y1Label = new JLabel("Y1");
        JTextField y1TextField = new JTextField(5);
        y1Panel.add(y1Label, BorderLayout.NORTH);
        y1Panel.add(y1TextField, BorderLayout.SOUTH);

        JPanel y2Panel = new JPanel(new BorderLayout());
        JLabel y2Label = new JLabel("Y2");
        JTextField y2TextField = new JTextField(5);
        y2Panel.add(y2Label, BorderLayout.NORTH);
        y2Panel.add(y2TextField, BorderLayout.SOUTH);

        JButton removeSubstepButton = new JButton("Remove Substep");

        // Add components to the substeps panel
        substepsPanel.add(new JLabel("Kill action"), substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(NPCIdPanel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(new JLabel("Area"), substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(x1Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(x2Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(y1Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(y2Panel, substepsGbc);
        substepsGbc.gridy++;
        substepsPanel.add(removeSubstepButton, substepsGbc);
        substepsGbc.gridy++;

        substepsPanel.revalidate();
        substepsPanel.repaint();

        // Action listener for "Remove Substep" button
        removeSubstepButton.addActionListener(e -> {
            substepsPanel.removeAll();
            substepsPanel.revalidate();
            substepsPanel.repaint();
        });
    }




}