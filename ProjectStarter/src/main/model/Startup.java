package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

// represents a startup with a list of product features and a development budget
public class Startup {
    private List<Feature> features;
    private double budget;
    private int userBase;

    // REQUIRES: budget >= 0
    // MODIFIES: this
    // EFFECTS: constructs a startup with an empty list of features and sets initial
    // budget
    public Startup(double budget) {
        this.budget = budget;
        this.features = new ArrayList<>();
        this.userBase = 0;
    }

    // REQUIRES: feature that is not null
    // MODIFIES: this
    // EFFECTS: adds the feature to the startup's list if budget is enough
    public void addFeature(Feature feature) {
        features.add(feature);
        budget = budget - feature.getCost();
        EventLog.getInstance().logEvent(new Event(
                "Added feature: " + feature.getName() + " (Cost: $" + feature.getCost() + "). Remaining budget: $"
                        + budget));
    }

    // REQUIRES: name is not null
    // MPDIFIES: this
    // EFFECTS: removes the feature with the given name from list
    public void removeFeature(String removed) {
        for (Feature f : features) {
            if (f.getName().equals(removed)) {
                if (!f.isCompleted()) {
                    budget = budget + f.getCost();
                }
                features.remove(f);
                EventLog.getInstance().logEvent(new Event(
                        "Removed feature: " + f.getName()
                        + (f.isCompleted() ? " (Completed, no refund)." : " (Refunded $" + f.getCost() + ")")));
                break;
            }
        }
    }

    // EFFECTS: returns estimated user base size
    public int getUserBase() {
        // updateUserBase();
        return userBase;
    }

    // MODIFIES: this
    // EFFECTS: recalculates user base from complete features
    public void updateUserBase() {
        int total = 0;
        for (Feature f : features) {
            if (f.isCompleted()) {
                total = total + f.getUserImpact();
            }
        }
        userBase = total;

    }

    // EFFECTS: returns list of all features
    public List<Feature> getFeatures() {
        return features;
    }

    // EFFECTS: returns number of features that are completed
    public int numCompletedFeatures() {
        int count = 0;
        for (Feature f : features) {
            if (f.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    // EFFECTS: returns current budget
    public double getBudget() {
        return budget;
    }

    // MODIFIES: this
    // EFFECTS: increases user base by given amount
    public void increaseUserBase(int amount) {
        userBase = userBase + amount;
        EventLog.getInstance().logEvent(new Event(
                "User base increased by " + amount + " to " + userBase));
    }

    // EFFECTS: returns startup as JSON object

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("budget", budget);
        json.put("userBase", userBase);
        json.put("features", featuresToJson());
        return json;
    }

    // EFEFECTS: returns features in this startup as a JSON array

    private JSONArray featuresToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Feature f : features) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: adds the feature to the startup's list WITHOUT modifying the budget
    public void loadFeature(Feature feature) {
        features.add(feature);
    }

}
