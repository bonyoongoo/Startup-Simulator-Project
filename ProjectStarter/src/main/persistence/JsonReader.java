package persistence;

import model.Feature;
import model.Startup;

import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// represents a reader that reads Startup data from JSON file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Startup from file and returns it
    // throws exception if error occurs reading data from file
    public Startup read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseStartup(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    protected String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses Startup from JSON object and returns it
    private Startup parseStartup(JSONObject jsonObject) {
        double budget = jsonObject.getDouble("budget");
        Startup startup = new Startup(budget);
        if (jsonObject.has("userBase")) {
            int userBase = jsonObject.getInt("userBase");
            startup.increaseUserBase(userBase);
        }

        if (jsonObject.has("features")) {
            addFeatures(startup, jsonObject.getJSONArray("features"));
        }

        return startup;
    }

    // MODIFIES: startup
    // EFFECTS: parses features from JSON array and adds them to startup
    private void addFeatures(Startup startup, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextFeature = (JSONObject) json;
            addFeature(startup, nextFeature);
        }
    }

    // MODIFIES: startup
    // EFFECTS: parses feature from JSON object and adds it to startup
    private void addFeature(Startup startup, JSONObject jsonFeature) {
        String name = jsonFeature.getString("name");
        double cost = jsonFeature.getDouble("cost");
        int userImpact = jsonFeature.getInt("userImpact");
        int estimatedTime = jsonFeature.getInt("estimatedTime");

        boolean completed = false;
        if (jsonFeature.has("completed")) {
            completed = jsonFeature.getBoolean("completed");
        }

        Feature feature = new Feature(name, cost, estimatedTime, userImpact);
        if (completed) {
            feature.markAsCompleted();
        }
        startup.loadFeature(feature);
    }

}
