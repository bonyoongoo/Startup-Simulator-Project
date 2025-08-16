package ui;

import model.Feature;
import model.Startup;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


// Starts the app 

public class StartupApp {
    private Scanner scanner;
    private Startup startup;
    private static final String JSON_STORE = "./data/startup.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: initializes everything
    public StartupApp() {
        scanner = new Scanner(System.in);
        startup = new Startup(5000);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: runs the application
    public void runApp() {
        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine();
            running = handleInput(input);
        }
        System.out.println("Exiting Startup Simulator.");
    }


    // EFFECTS: used as a helper for runApp to help keep under 25 lines
    private boolean handleInput(String input) {
        if (input.equals("1")) {
            addFeature();
        } else if (input.equals("2")) {
            viewFeatures();
        } else if (input.equals("3")) {
            markFeatureComplete();
        } else if (input.equals("4")) {
            removeFeature();
        } else if (input.equals("5")) {
            viewStats();
        } else if (input.equals("6")) {
            saveStartup();
        } else if (input.equals("7")) {
            loadStartup();
        } else if (input.equals("8")) {
            return false;
        } else {
            System.out.println("Invalid option.");
        }
        return true;
    }



    // EFFECTS: displays menu of user commands
    public void printMenu() {
        System.out.println("STARTUP SIMULATOR");
        System.out.println("1. Add Feature");
        System.out.println("2. view Features");
        System.out.println("3. Mark feature as complete");
        System.out.println("4. Remove Feature");
        System.out.println("5. View stats");
        System.out.println("6. save startup to file");
        System.out.println("7. load startup from file");
        System.out.println("8. quit");
        System.out.println("Enter Option: ");
    }

    // REQUIRES: valid user imput from scanner
    // MODIFIES: this
    // EFFECTS: adds a feature to the startup
    public void addFeature() {
        System.out.println("Feature name: ");
        String name = scanner.nextLine();

        System.out.print("Cost: ");
        double cost = Double.parseDouble(scanner.nextLine());

        System.out.print("Completion time: ");
        int time = Integer.parseInt(scanner.nextLine());

        System.out.print("User impact: ");
        int impact = Integer.parseInt(scanner.nextLine());

        Feature f = new Feature(name, cost, time, impact);
        startup.addFeature(f);

        System.out.println("Feature added.");

    }

    // EFFECTS: displays all features in the startup
    private void viewFeatures() {
        if (startup.getFeatures().size() == 0) {
            System.out.println("no features yet");
        } else {
            int i = 0;
            while (i < startup.getFeatures().size()) {
                Feature f = startup.getFeatures().get(i);
                boolean completed = f.isCompleted();
                String status = "";
                if (completed == true) {
                    status = "Completed";
                }
                if (completed == false) {
                    status = "Planned";
                }
                System.out.println("- " + f.getName()
                        + " | Cost: " + f.getCost()
                        +  " | Status: " + status
                        + " | User impact: " + f.getUserImpact());
                i = i + 1;
            }
        }
    }
    

    // REQUIRES: name of existing feature
    // MODIFIES: this
    // EFFECTS: makrs a feature as completed and increases user base
    private void markFeatureComplete() {
        System.out.print("Feature to mark as completed: ");
        String name = scanner.nextLine();
        for (Feature f : startup.getFeatures()) {
            if (f.getName().equals(name)) {
                f.markAsCompleted();
                startup.increaseUserBase(f.getUserImpact());
                System.out.println("Feature marked as complete.");
                return;
            }
        }
        System.out.println("Feature not found.");
    }

    // REQUIRES: name of a feature to remove
    // MODIFIES: this
    // EFFECTS: removes feature from startup and refunds budget if not completed
    private void removeFeature() {
        System.out.print("Feature to remove: ");
        String name = scanner.nextLine();
        startup.removeFeature(name);
        System.out.println("Feature removed (if found).");
    }

    // EFFECTS: displays current budget, user base, and number of completed features
    private void viewStats() {
        System.out.println("Current Budget: " + startup.getBudget());
        System.out.println("Completed Features: " + startup.numCompletedFeatures());
        System.out.println("User Base: " + startup.getUserBase());
    }


    //EFFECTS: saves the startup to file
    private void saveStartup() {
        try {
            jsonWriter.open();
            jsonWriter.write(startup);
            jsonWriter.close();
            System.out.println("Saved startup to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads startup from file
    private void loadStartup() {
        try {
            startup = jsonReader.read();
            System.out.println("Loaded startup from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("unable to read from file " + JSON_STORE);
        }
    }

}
