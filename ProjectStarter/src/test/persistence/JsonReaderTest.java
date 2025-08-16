package persistence;

import model.Feature;
import model.Startup;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void testReaderFeatureMissingCompleted() {
        JsonReader reader = new JsonReader("./data/testReaderFeatureMissingCompleted.json");
        try {
            Startup startup = reader.read();
            assertEquals(5000, startup.getBudget());
            assertEquals(100, startup.getUserBase());
            assertEquals(1, startup.getFeatures().size());

            Feature feature = startup.getFeatures().get(0);
            assertEquals("New UI", feature.getName());
            assertEquals(1000, feature.getCost());
            assertEquals(7, feature.getEstimatedTime());
            assertEquals(200, feature.getUserImpact());
            assertFalse(feature.isCompleted());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderFeatureWithCompletedTrue() {
        JsonReader reader = new JsonReader("./data/testReaderFeatureWithCompletedTrue.json");
        try {
            Startup startup = reader.read();
            Feature f = startup.getFeatures().get(0);
            assertTrue(f.isCompleted());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    void testReaderMissingUserBase() {
        JsonReader reader = new JsonReader("./data/testReaderMissingUserBase.json");
        try {
            Startup startup = reader.read();
            assertEquals(1000, startup.getBudget());
            assertEquals(0, startup.getUserBase()); 
            assertEquals(0, startup.getFeatures().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralStartup() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralStartup.json");
        try {
            Startup startup = reader.read();
            assertEquals(2, startup.getFeatures().size());

            assertEquals(2500, startup.getBudget());

            List<Feature> features = startup.getFeatures();

            Feature f1 = features.get(0);
            assertEquals("Chatbot", f1.getName());
            assertEquals(3000, f1.getCost());
            assertEquals(10, f1.getEstimatedTime());
            assertEquals(30, f1.getUserImpact());
            assertFalse(f1.isCompleted());

            Feature f2 = features.get(1);
            assertEquals("Analytics Dashboard", f2.getName());
            assertEquals(4000, f2.getCost());
            assertEquals(15, f2.getEstimatedTime());
            assertEquals(50, f2.getUserImpact());
            assertTrue(f2.isCompleted());

        } catch (IOException e) {
            fail("Couldnt read from file");
        }

    }

    @Test
    void testReaderZeroValuesFeature() {
        JsonReader reader = new JsonReader("./data/testReaderZeroValuesFeature.json");
        try {
            Startup startup = reader.read();
            List<Feature> features = startup.getFeatures();
            assertEquals(1, features.size());

            Feature f = features.get(0);
            assertEquals("Zero Feature", f.getName());
            assertEquals(0, f.getCost());
            assertEquals(0, f.getEstimatedTime());
            assertEquals(0, f.getUserImpact());
            assertFalse(f.isCompleted());

            assertEquals(1000, startup.getBudget());

        } catch (IOException e) {
            fail("Couldn't read file with zero-values feature");
        }
    }

}
