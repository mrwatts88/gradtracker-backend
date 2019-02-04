package edu.uwm.capstone.model.cards;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RankUnitTest {

    /**
     * Verify that each of the {@link Rank#values} has a unique {@link Rank#displayName}.
     */
    @Test
    public void verifyUniqueDisplayNames() {
        Map<String, int[]> rankDisplayNames = new HashMap<>();
        for (Rank rank : Rank.values()) {
            assertNotNull(rank.name() + " has an invalid display name", rank.getDisplayName());
            assertNotNull(rank.name() + " has an invalid value", rank.getValue());
            rankDisplayNames.put(rank.getDisplayName(), rank.getValue());
        }
        assertEquals(Rank.values().length, rankDisplayNames.size());
    }

}
