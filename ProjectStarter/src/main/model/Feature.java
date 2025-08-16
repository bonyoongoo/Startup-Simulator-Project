package model;

import org.json.JSONObject;

// Represents a product feature in startup with a cost, developement status, 
// user impact, estimated time to build, name, and completion status 

public class Feature {
    private String name;
    private double cost;
    private int estimatedTime;
    private boolean isCompleted;
    private int userImpact;

    // REQUIRES: non-empty name, cost >= 0, estimatedTime > 0, user impact >= 0
    // EFFECTS: constructs a new feature with given parameters
    public Feature(String name, double cost, double time, int userImpact) {
        this.name = name;
        this.cost = cost;
        this.estimatedTime = (int) time;
        this.userImpact = userImpact;
        this.isCompleted = false;


    }

    // EFFECTS: returns whether the feature is completed
    public boolean isCompleted() {
        return isCompleted;
    }

    // MODIFIES: THIS
    // EFFECTS: marks the feature as completed
    public void markAsCompleted() {
        isCompleted = true;
    }

    // EFFECTS: returns the name of feature
    public String getName() {
        return name;
    }

    // EFECTS: returns the development cost
    public double getCost() {
        return cost;
    }

    // EFFECTS: returns the estimated time to complete feature
    public int getEstimatedTime() {
        return estimatedTime;
    }

    //EFECTS: returns the user impact score
    public int getUserImpact() {
        return userImpact;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("cost", cost);
        json.put("userImpact", userImpact);
        json.put("estimatedTime", estimatedTime);
        json.put("completed", isCompleted);
        return json;

    }

}
