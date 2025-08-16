package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FeatureTest {

    private Feature f1;

    @BeforeEach
    public void runBefore() {
        f1 = new Feature("Login System", 1000, 5, 80);
    }

    @Test
    public void testConstructor() {
        assertEquals("Login System", f1.getName());
        assertEquals(1000, f1.getCost());
        assertEquals(5, f1.getEstimatedTime());
        assertEquals(80, f1.getUserImpact());
        assertFalse(f1.isCompleted());

    }

    @Test
    public void testMarkAsCompleted() {
        assertFalse(f1.isCompleted());
        f1.markAsCompleted();
        assertTrue(f1.isCompleted());
    }




    
}
