package edu.uwm.capstone.model.cards;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static edu.uwm.capstone.helper.TestHelper.randomAlphabetic;
import static org.junit.Assert.*;

public class PlayerUnitTest {

    /**
     * Verify that we can create {@link Player}s for every {@link Rank} and {@link Suit} that is defined.
     */
    @Test
    public void verifyConstructor() {
        Player player = new Player(randomAlphabetic(100));
        assertNotNull(player);
        assertNotNull(player.getName());
        assertNotNull(player.getCards());
    }

    /**
     * Verify that a request to create a {@link Player} with an null {@link Player#name} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyConstructorNullName() {
        new Player(null);
    }

    /**
     * Verify {@link Player#equals} is working correctly.
     */
    @Test
    public void verifyEquals() {
        String playerName = randomAlphabetic(100);
        Player player1 = new Player(playerName);
        Player player2 = new Player(playerName);
        assertEquals(player1, player2);
    }

    /**
     * Verify {@link Player#equals} is correctly determining inequality.
     */
    @Test
    public void verifyNotEquals() {
        Player player1 = new Player(randomAlphabetic(100));
        Player player2 = new Player(randomAlphabetic(120));
        assertNotEquals(player1, player2);

        String playerName = randomAlphabetic(100);
        Player player3 = new Player(playerName);
        Player player4 = new Player(playerName);

        List<Card> diamonds = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            diamonds.add(new Card(rank, Suit.DIAMONDS));
        }
        player3.setCards(diamonds);

        List<Card> clubs = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            clubs.add(new Card(rank, Suit.CLUBS));
        }
        player4.setCards(clubs);

        assertNotEquals(player3, player4);

        assertNotEquals(player4, null);
    }

    /**
     * Verify {@link Player#hashCode} is working correctly.
     */
    @Test
    public void verifyHashCode() {
        assertNotNull(new Player(randomAlphabetic(100)).hashCode());
    }

    /**
     * Verify {@link Player#toString} is working correctly.
     */
    @Test
    public void verifyToString() {
        assertNotNull(new Player(randomAlphabetic(100)).toString());
    }

}
