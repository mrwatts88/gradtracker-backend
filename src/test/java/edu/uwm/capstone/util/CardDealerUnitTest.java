package edu.uwm.capstone.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.uwm.capstone.model.cards.Card;
import edu.uwm.capstone.model.cards.Deck;
import edu.uwm.capstone.model.cards.Game;
import edu.uwm.capstone.model.cards.Player;
import static org.junit.Assert.*;

public class CardDealerUnitTest {

    /**
     * {@link CardDealer} is a utility class and as such it should only contains static methods.
     * This test verifies that the {@link CardDealer} object only contains a private constructor
     * so that all of its methods will be provided statically.
     */
    @Test
    public void verifyConstructorIsPrivate() throws Exception {
        Constructor<CardDealer> constructor = CardDealer.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * {@link CardDealer} is a utility class and as such it should only contains static methods.
     * This test verifies that the {@link CardDealer} object does not contain any public constructors
     * so that all of its methods will be provided statically.
     */
    @Test
    public void verifyOnlyPrivateConstructors() {
        assertEquals(0, CardDealer.class.getConstructors().length);
    }

    /**
     * Compare an unshuffled {@link Deck} of {@link Card}s with a shuffled {@link Deck} of {@link Card}s
     * that is produced by {@link CardDealer#shuffle}.
     */
    @Test
    public void shuffleDeck() {
        Deck unshuffledDeck = new Deck();
        Deck verifyUnshuffledDeck = new Deck();
        assertEquals(unshuffledDeck, verifyUnshuffledDeck);
        assertEquals(unshuffledDeck.getCards(), verifyUnshuffledDeck.getCards());

        CardDealer.shuffle(unshuffledDeck);
        assertNotEquals(unshuffledDeck, verifyUnshuffledDeck);
        assertNotEquals(unshuffledDeck.getCards(), verifyUnshuffledDeck.getCards());
    }

    /**
     * Compare the unshuffled cards produced by {@link Deck#getCards} with the shuffled cards that is produced by
     * {@link CardDealer#shuffle}.
     */
    @Test
    public void shuffleListOfCards() {
        List<Card> unshuffledCards = new Deck().getCards();
        List<Card> verifyUnshuffledCards = new Deck().getCards();
        assertEquals(unshuffledCards, verifyUnshuffledCards);

        CardDealer.shuffle(verifyUnshuffledCards);
        assertNotEquals(unshuffledCards, verifyUnshuffledCards);
    }

    /**
     * TODO: Test Engineering Open Code Challenge
     * Verify that {@link CardDealer#deal(List, Deck)} correctly deals {@link Card}s to all of the
     * {@link Player}s provided. Don't just do a happy path approach!
     */
    @Test
    public void deal() {
        // get a deck of cards
        Deck deck = new Deck();

        // TODO: generate a list of players
        List<Player> players = new ArrayList<>();
        players.add(new Player("XxX"));
        players.add(new Player("yYy"));
        players.add(new Player("ZZZ"));

        // TODO: replace the following once the corresponding CardDealer deal functionality is working
        Game game = CardDealer.deal(players, deck);
        assertNotNull(game);

        // TODO: and complete all of the appropriate assertions
    }

    /**
     * TODO: Test Engineering Open Code Challenge
     * Verify that {@link CardDealer#deal(List, Deck, int)} correctly deals the specified number of {@link Card}s
     * to each of the {@link Player}s provided. Don't just do a happy path approach!
     */
    @Test
    public void dealNumberOfCardsPerPlayer() {
        // get a deck of cards
        Deck deck = new Deck();

        // TODO: generate a list of players
        List<Player> players = new ArrayList<>();
        players.add(new Player("XxXxxxxx"));
        players.add(new Player("yYyYYYYY"));
        players.add(new Player("ZZZzzzzz"));

        int numberOfCards = 5;

        // TODO: replace the following once the corresponding CardDealer deal functionality is working
        Game game = CardDealer.deal(players, deck, numberOfCards);
        assertNotNull(game);

        // TODO: and complete all of the appropriate assertions
    }

}
