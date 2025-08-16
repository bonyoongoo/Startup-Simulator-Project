package persistence;

import model.Startup;

import org.json.JSONObject;

import java.io.*;

// Represents a write that writes JSON representation of Startup to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    //EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writerl throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }


    // MODIFIES: this
    // EFFECTS: wrotes JSON representation of Startup file
    public void write(Startup startup) {
        JSONObject json = startup.toJson();
        saveToFile(json.toString(TAB));
        
    }


    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }


    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }



}
