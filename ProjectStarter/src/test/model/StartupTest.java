package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StartupTest {
    private Startup startup;
    private Feature feature1;
    private Feature feature2;

    @BeforeEach
    public void runBefore() {
        startup = new Startup(5000);
        feature1 = new Feature("Chat System", 1500, 7, 60);
        feature2 = new Feature("Dark Mode", 500, 3, 40);
    }

    @Test
    public void testConstructor() {
        assertEquals(5000, startup.getBudget());
        assertEquals(0, startup.getFeatures().size());
    }

    @Test
    public void testAddFeature() {
        startup.addFeature(feature1);
        assertEquals(1, startup.getFeatures().size());
        assertTrue(startup.getFeatures().contains(feature1));
        assertEquals(3500, startup.getBudget());
    }

    @Test
    public void testRemoveFeature() {
        startup.addFeature(feature1);
        startup.addFeature(feature2);
        startup.removeFeature("Chat System");
        assertEquals(1, startup.getFeatures().size());
        assertFalse(startup.getFeatures().contains(feature1));
    }

    @Test
    public void testNumCompletedFeatures() {
        startup.addFeature(feature1);
        startup.addFeature(feature2);
        assertEquals(0, startup.numCompletedFeatures());
        feature1.markAsCompleted();
        assertEquals(1, startup.numCompletedFeatures());
        feature2.markAsCompleted();
        assertEquals(2, startup.numCompletedFeatures());

    }

    @Test
    public void testRemoveNonExistentFeature() {
        startup.addFeature(feature1);
        startup.removeFeature("DosntExist");
        assertEquals(1, startup.getFeatures().size());
        assertEquals(3500, startup.getBudget());
    }

    @Test
    public void testIncreaseUserBase() {
        assertEquals(0, startup.getUserBase());
        startup.increaseUserBase(100);
        assertEquals(100, startup.getUserBase());
        startup.increaseUserBase(50);
        assertEquals(150, startup.getUserBase());
    }

    @Test
    public void testAddFeatureWithZeroCost() {
        Feature freeFeature = new Feature("Feedback Tool", 0, 2, 10);
        startup.addFeature(freeFeature);
        assertEquals(1, startup.getFeatures().size());
        assertEquals(5000, startup.getBudget());

    }

    @Test
    public void testRemoveUncompletedFeatureRefund() {
        startup.addFeature(feature1);
        startup.removeFeature("Chat System");
        assertEquals(0, startup.getFeatures().size());
        assertEquals(5000, startup.getBudget());
    }

    @Test
    public void testRemoveUncompletedFeatureNoRefund() {
        startup.addFeature(feature1);
        feature1.markAsCompleted();
        startup.removeFeature("Chat System");
        assertEquals(0, startup.getFeatures().size());
        assertEquals(3500, startup.getBudget());
    }

    @Test
    public void testGetFeaturesReturnsActualList() {
        startup.addFeature(feature1);
        List<Feature> feature = startup.getFeatures();
        assertSame(startup.getFeatures(), feature);
        assertTrue(feature.contains(feature1));
    }

    @Test
    public void testMultipleFeaturesSameName() {
        Feature duplicate = new Feature("Chat System", 1000, 5, 30);
        startup.addFeature(feature1);
        startup.addFeature(duplicate);
        startup.removeFeature("Chat System");
        assertEquals(1, startup.getFeatures().size());
        assertTrue(startup.getFeatures().contains(duplicate));
    }

    @Test
    public void testUpdateUserBaseManualCall() {
        startup.addFeature(feature1);
        startup.addFeature(feature2);
        feature1.markAsCompleted();
        feature2.markAsCompleted();
        startup.updateUserBase();
        assertEquals(100, startup.getUserBase());
    }

    @Test
    public void testRemoveFeatureFromEmptyStartup() {
        startup.removeFeature("Chat System");
        assertEquals(0, startup.getFeatures().size());
        assertEquals(5000, startup.getBudget());
    }

    @Test
    public void testAddMultipleFeatures() {
        startup.addFeature(feature1);
        startup.addFeature(feature2);
        assertEquals(2, startup.getFeatures().size());
        assertEquals(3000, startup.getBudget());
    }

    @Test
    public void testUpdateUserBaseWithNoCompletedFeatures() {
        startup.addFeature(feature1);
        startup.addFeature(feature2);
        startup.updateUserBase();
        assertEquals(0, startup.getUserBase());
    }

}
