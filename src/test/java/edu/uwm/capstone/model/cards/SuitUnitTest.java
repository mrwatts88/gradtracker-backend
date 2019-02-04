package edu.uwm.capstone.model.cards;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SuitUnitTest {

    /**
     * Verify that each of the {@link Suit#values} has a unique {@link Suit#displayName}.
     */
    @Test
    public void suit() {
        Map<String, String> suitDisplayNames = new HashMap<>();
        for (Suit suit : Suit.values()) {
            assertNotNull(suit.name() + " has an invalid display name", suit.getDisplayName());
            suitDisplayNames.put(suit.getDisplayName(), suit.name());
        }
        assertEquals(Suit.values().length, suitDisplayNames.size());
    }

}
