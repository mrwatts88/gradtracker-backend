package edu.uwm.capstone.model.cards;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static edu.uwm.capstone.helper.TestHelper.randomAlphabetic;
import static org.junit.Assert.*;

public class GameUnitTest {

    /**
     * Verify that we can create {@link Game}s for every {@link Rank} and {@link Suit} that is defined.
     */
    @Test
    public void verifyConstructor() {
        List<Player> players = new ArrayList<>();
        List<Card> undealtCards = new ArrayList<>();
        Game game = new Game(players, undealtCards);
        assertNotNull(game);
        assertEquals(players, game.getPlayers());
        assertEquals(undealtCards, game.getUndealtCards());
    }

    /**
     * Verify that a request to create a {@link Game} with an null List of {@link Player}s produces
     * an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyConstructorNullRank() {
        List<Card> undealtCards = new ArrayList<>();
        new Game(null, undealtCards);
    }

    /**
     * Verify that a request to create a {@link Game} with an null List of undealt {@link Card}s produces
     * an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyConstructorNullSuit() {
        List<Player> players = new ArrayList<>();
        new Game(players, null);
    }

    /**
     * Verify {@link Game#equals} is working correctly.
     */
    @Test
    public void verifyEquals() {
        List<Player> players = new ArrayList<>();
        List<Card> undealtCards = new ArrayList<>();
        Game game1 = new Game(players, undealtCards);
        Game game2 = new Game(players, undealtCards);
        assertEquals(game1, game2);
    }

    /**
     * Verify {@link Game#equals} is correctly determining inequality.
     */
    @Test
    public void verifyNotEquals() {
        List<Player> players = new ArrayList<>();
        List<Card> undealtCards = new ArrayList<>();
        Game game1 = new Game(players, undealtCards);

        List<Player> differentPlayers = new ArrayList<>();
        differentPlayers.add(new Player(randomAlphabetic(20)));
        Game game2 = new Game(differentPlayers, undealtCards);
        assertNotEquals(game1, game2);

        List<Card> differentUndealtCards = new Deck().getCards();
        Game game3 = new Game(players, differentUndealtCards);
        assertNotEquals(game1, game3);

        assertNotEquals(game1, null);
    }

    /**
     * Verify {@link Game#hashCode} is working correctly.
     */
    @Test
    public void verifyHashCode() {
        List<Player> players = new ArrayList<>();
        List<Card> undealtCards = new ArrayList<>();
        Game game = new Game(players, undealtCards);
        assertNotNull(game);
    }

    /**
     * Verify {@link Game#toString} is working correctly.
     */
    @Test
    public void verifyToString() {
        List<Player> players = new ArrayList<>();
        List<Card> undealtCards = new ArrayList<>();
        Game game = new Game(players, undealtCards);
        assertNotNull(game.toString());
    }

}
