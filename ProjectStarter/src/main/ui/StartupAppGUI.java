package ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.util.*;
import model.Startup;
import persistence.JsonReader;
import persistence.JsonWriter;

import model.Event;
import model.EventLog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Represents main GUI for the startup simulator app.
// provides graphical interface
public class StartupAppGUI extends JFrame {
    private static final String JSON_STORE = "./data/startup.json";

    private List<Integer> userBaseHistory;
    private GraphPanel graphPanel;

    private Startup startup;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanel featurePanel;
    private JTextArea logArea;

    public StartupAppGUI() {
        super("Startup Simulator");
        runStartupApp();
    }

    // EFFECTS: sets up the main GUI window
    private void runStartupApp() {

        startup = new Startup(1000);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        userBaseHistory = new ArrayList<>();
        userBaseHistory.add(startup.getUserBase());
        userBaseHistory.add(startup.getUserBase());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLayout(new BorderLayout());

        graphPanel = new GraphPanel(userBaseHistory);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(buildFeaturePanel(), BorderLayout.CENTER);
        contentPanel.add(graphPanel, BorderLayout.SOUTH);

        add(buildControlPanel(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buildLogPanel(), BorderLayout.SOUTH);

        setupWindowListener();
        ;

        setVisible(true);
    }

    // EFFECTS: helper to setup windowListerner
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("=== EVENT LOG START ===");
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString());
                    System.out.println();
                }
                System.out.println("=== EVENT LOG END ===");
            }
        });

    }

    // EFFECTS: helps make buildControlPanel under line limit
    private JButton makeButton(String label, java.awt.event.ActionListener action) {
        JButton button = new JButton(label);
        button.addActionListener(action);
        return button;
    }

    // EFFECTS: constructs the top button panel (add/remove/save/load)
    public JPanel buildControlPanel() {

        JPanel panel = new JPanel(new java.awt.FlowLayout());
        addButtonsToPanel(panel);
        return panel;

    }

    // MODIFIES: panel
    // EFFECTS: adds all control buttons to given panel
    private void addButtonsToPanel(JPanel panel) {
        panel.add(makeAddButton());
        panel.add(makeRemoveButton());
        panel.add(makeSaveButton());
        panel.add(makeLoadButton());
        panel.add(makeCompleteButton());
    }

    // EFFECTS: creates and returns configured Add Feature button
    private JButton makeAddButton() {
        return makeButton("Add Feature", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleAddFeature();
            }
        });
    }

    // EFFECTS: creates and returns configured Remove Feature button
    private JButton makeRemoveButton() {
        return makeButton("Remove Feature", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleRemoveFeature();
            }
        });
    }

    // EFFECTS: creates and returns configured Save button
    private JButton makeSaveButton() {
        return makeButton("Save Startup", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                saveStartup();
            }
        });
    }

    // EFFECTS: creates and returns configured Load button
    private JButton makeLoadButton() {
        return makeButton("Load Startup", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                loadStartup();
            }
        });
    }

    // EFFECTS: creates and returns configured Complete button
    private JButton makeCompleteButton() {
        return makeButton("Mark Feature Completed", new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleMarkCompleted();
            }
        });
    }

    // EFFECTS: constructs center panel for displaying feature list
    public JPanel buildFeaturePanel() {

        featurePanel = new JPanel(new BorderLayout());
        JTextArea featureListArea = new JTextArea(getFeaturesAsString());
        featureListArea.setEditable(false);
        featurePanel.add(new JScrollPane(featureListArea), BorderLayout.CENTER);
        return featurePanel;
    }

    // EFFECTS: returna a formatted string listing all featurees of startup
    public String getFeaturesAsString() {
        StringBuilder sb = new StringBuilder();
        for (model.Feature f : startup.getFeatures()) {
            sb.append("Name: ").append(f.getName()).append("\n");
            sb.append("Cost: ").append(f.getCost()).append("\n");
            sb.append("Time: ").append(f.getEstimatedTime()).append("\n");
            sb.append("Impact: ").append(f.getUserImpact()).append("\n");
            sb.append("Status: ").append(f.isCompleted() ? "Completed" : "Incomplete").append("\n\n");
        }
        return sb.toString();

    }

    // EFFECTS: constructs the bottom log panel
    public JScrollPane buildLogPanel() {
        logArea = new JTextArea(5, 50);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        return scrollPane;
    }

    // EFFECTS: refreshses featurePanel with current list of features
    public void refreshFeaturePanel() {
        featurePanel.removeAll();
        JTextArea updatedArea = new JTextArea(getFeaturesAsString());
        updatedArea.setEditable(false);
        featurePanel.add(new JScrollPane(updatedArea), java.awt.BorderLayout.CENTER);
        featurePanel.revalidate();
        featurePanel.repaint();
    }

    // EFFECTS: Opens a dialog to add a new feature
    public void handleAddFeature() {
        try {
            String name = javax.swing.JOptionPane.showInputDialog(this, "Enter feature name:");
            if (name == null || name.isEmpty()) {
                log("Feature addition cancelled.");
                return;
            }

            String costStr = javax.swing.JOptionPane.showInputDialog(this, "Enter feature cost:");
            double cost = Double.parseDouble(costStr);

            String timeStr = javax.swing.JOptionPane.showInputDialog(this, "Enter estimated time to build:");
            double time = Double.parseDouble(timeStr);

            String impactStr = javax.swing.JOptionPane.showInputDialog(this, "Enter user impact:");
            int impact = Integer.parseInt(impactStr);

            model.Feature feature = new model.Feature(name, cost, time, impact);
            startup.addFeature(feature);

            refreshFeaturePanel();
            log("Feature '" + name + "' added successfully.");
        } catch (NumberFormatException e) {
            log("Invalid number entered. Feature not added.");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts for feature name, marks feature as completed if found,
    // updates user base
    public void handleMarkCompleted() {
        if (noFeaturesAvailable()) {
            return;
        }

        String name = getFeatureNameFromUser();
        if (isCompletionCancelled(name)) {
            return;
        }
        processFeatureCompletion(name);
    }

    // EFFECTS: returns true is no features are available to mark as complete
    private boolean noFeaturesAvailable() {
        if (startup.getFeatures().isEmpty()) {
            log("No features to mark as completed.");
            return true;
        }
        return false;
    }

    // EFFECTS: returns feature name from user
    private String getFeatureNameFromUser() {
        return JOptionPane.showInputDialog(this, "Enter the name of the feature to complete:");
    }

    // EFFECTS: returns true is completion is cancelled
    private boolean isCompletionCancelled(String name) {
        if (name == null || name.isEmpty()) {
            log("Completion cancelled.");
            return true;
        }
        return false;
    }

    // EFFECTS: completed matching feature or logs not found message
    private void processFeatureCompletion(String name) {
        for (model.Feature f : startup.getFeatures()) {
            if (f.getName().equalsIgnoreCase(name)) {
                handleFoundFeature(f);
                return;
            }
        }
        log("Feature not found: " + name);
    }

    // EFFECTS: Either logs already-completed status or completes feature
    private void handleFoundFeature(model.Feature f) {
        if (f.isCompleted()) {
            log("Feature is already marked as completed.");
        } else {
            completeFeature(f);
        }
    }

    // Effects: Marks feature complete, updates user base, refreshes UI
    private void completeFeature(model.Feature f) {
        f.markAsCompleted();
        updateUserBase(f);
        log("Feature '" + f.getName() + "' marked as completed.");
        refreshFeaturePanel();
    }

    // Effects: Modifies user base and history, repaints graph
    private void updateUserBase(model.Feature f) {
        int previousUserBase = startup.getUserBase();
        startup.increaseUserBase(f.getUserImpact());
        int newUserBase = startup.getUserBase();
        userBaseHistory.add(newUserBase != previousUserBase ? newUserBase : previousUserBase);
        log("User base updated: " + userBaseHistory);
        graphPanel.repaint();
    }

    // EFFECTS: Removes a feature (e.g., last one added)
    public void handleRemoveFeature() {
        if (noFeaturesAvailable()) {
            return;
        }

        model.Feature removed = getLastFeature();

        boolean wasCompleted = removed.isCompleted();

        startup.removeFeature(removed.getName());

        if (wasCompleted) {
            rebuildUserBaseHistoryFromCompleted();
            graphPanel.updateData(userBaseHistory);
            graphPanel.repaint();
        }

        log("Removed feature: " + removed.getName());
        refreshFeaturePanel();
    }

    // EFFECTS: returns the last feature in the list
    private model.Feature getLastFeature() {
        return startup.getFeatures().get(startup.getFeatures().size() - 1);
    }

    // MODIFIES: userBaseHistory
    // EFFECTS: rebuilds userBaseHistory from completed features only
    private void rebuildUserBaseHistoryFromCompleted() {
        userBaseHistory.clear();
        int base = 0;
        userBaseHistory.add(base);

        for (model.Feature f : startup.getFeatures()) {
            if (f.isCompleted()) {
                base += f.getUserImpact();
                userBaseHistory.add(base);
            }
        }

        if (userBaseHistory.size() == 1) {
            userBaseHistory.add(userBaseHistory.get(0));
        }

    }

    // EFFECTS: Saves the current state of startup to file
    public void saveStartup() {
        try {
            jsonWriter.open();
            jsonWriter.write(startup);
            jsonWriter.close();
            log("Saved startup to " + JSON_STORE);
        } catch (java.io.FileNotFoundException e) {
            log("Unable to save to file: " + JSON_STORE);
        }

    }

    // EFFECTS: Loads the saved startup state from file
    public void loadStartup() {
        try {
            startup = jsonReader.read();
            log("Loaded startup from " + JSON_STORE);
            refreshFeaturePanel();
        } catch (java.io.IOException e) {
            log("Unable to read from file: " + JSON_STORE);
        }

        userBaseHistory.clear();
        int base = 0;
        userBaseHistory.add(base);

        for (model.Feature f : startup.getFeatures()) {
            if (f.isCompleted()) {
                base += f.getUserImpact();
                userBaseHistory.add(base);
            }
        }

        graphPanel.updateData(userBaseHistory);
        graphPanel.repaint();

    }

    // EFFECTS: Logs a message to the logArea
    public void log(String message) {
        logArea.append(message + "\n");
    }

    // public static void main(String[] args) {
    //     new StartupAppGUI();
    // }

}
