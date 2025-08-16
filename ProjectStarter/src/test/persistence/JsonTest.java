package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import model.Feature;

public class JsonTest {
    protected void checkFeature(String name, double cost, int time, int impact, boolean completed, Feature f) {
        assertEquals(name, f.getName());
        assertEquals(cost, f.getCost());
        assertEquals(time, f.getEstimatedTime());
        assertEquals(impact, f.getUserImpact());
        assertEquals(completed, f.isCompleted());
    }
}
