package persistence;

import model.Feature;
import model.Startup;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterEmptyStartup() {
        try {
            Startup startup = new Startup(10000);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyStartup.json");
            writer.open();
            writer.write(startup);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyStartup.json");
            startup = reader.read();
            assertEquals(10000, startup.getBudget());
            assertEquals(0, startup.getFeatures().size());
            assertEquals(0, startup.getUserBase());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStartup() {
        try {
            Startup startup = new Startup(0);
            Feature f1 = new Feature("Chatbot", 3000, 10, 30);
            Feature f2 = new Feature("Analytics Dashboard", 4000, 15, 50);
            f2.markAsCompleted();
            startup.addFeature(f1);
            startup.addFeature(f2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralStartup.json");
            writer.open();
            writer.write(startup);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralStartup.json");
            Startup loaded = reader.read();

            assertEquals(2, loaded.getFeatures().size());
            checkFeature("Chatbot", 3000, 10, 30, false, loaded.getFeatures().get(0));
            checkFeature("Analytics Dashboard", 4000, 15, 50, true, loaded.getFeatures().get(1));
            assertEquals(-7000, loaded.getBudget());

        } catch (IOException e) {
            fail("Couldn't write/read file");
        }
    }

    @Test
    void testWriterStartupWithZeroValues() {
        try {
            Startup startup = new Startup(0);
            Feature f1 = new Feature("Empty", 0, 0, 0);
            startup.addFeature(f1);

            JsonWriter writer = new JsonWriter("./data/testWriterStartupWithZeroValues.json");
            writer.open();
            writer.write(startup);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterStartupWithZeroValues.json");
            startup = reader.read();
            List<Feature> features = startup.getFeatures();
            assertEquals(1, features.size());
            checkFeature("Empty", 0, 0, 0, false, features.get(0));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

 


}
