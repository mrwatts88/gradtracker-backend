package edu.uwm.capstone.util;

import edu.uwm.capstone.model.cards.Card;
import edu.uwm.capstone.model.cards.Deck;
import edu.uwm.capstone.model.cards.Game;
import edu.uwm.capstone.model.cards.Player;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
     * TODO: Test Engineering Code Challenge
     * Compare an unshuffled {@link Deck} of {@link Card}s with a shuffled {@link Deck} of {@link Card}s
     * that is produced by {@link CardDealer#shuffle}.
     */
    @Test
    public void shuffleDeck() {
        Deck deckToShuffle = new Deck();
        Deck unshuffledDeck = new Deck();
        int numOfSameCards = 0;
        boolean isShuffledEnough = false;
        assertEquals(deckToShuffle, unshuffledDeck);
        assertEquals(deckToShuffle.getCards(), unshuffledDeck.getCards());
        CardDealer.shuffle(deckToShuffle);
        for(int i = 0; i < 52; i++)
            if (deckToShuffle.getCards().get(i).equals(unshuffledDeck.getCards().get(i))){
                numOfSameCards++;
        }
        if(numOfSameCards/52 < 0.06)
            isShuffledEnough = true;
        assertEquals(true, isShuffledEnough);
        assertNotEquals(deckToShuffle, unshuffledDeck);
        assertNotEquals(deckToShuffle.getCards(), unshuffledDeck.getCards());
    }

    /**
     * TODO: Test Engineering Code Challenge
     * Compare the unshuffled cards produced by {@link Deck#getCards} with the shuffled cards that is produced by
     * {@link CardDealer#shuffle}.
     */
    @Test
    public void shuffleListOfCards() {
        List<Card> cardsToShuffle = new Deck().getCards();
        List<Card> unshuffledCards = new Deck().getCards();
        assertEquals(cardsToShuffle, unshuffledCards);

        int numOfSameCards = 0;
        boolean isShuffledEnough = false;
        assertEquals(cardsToShuffle, unshuffledCards);
        CardDealer.shuffle(cardsToShuffle);
        for(int i = 0; i < 52; i++)
            if (cardsToShuffle.get(i).equals(unshuffledCards.get(i))){
                numOfSameCards++;
            }
        if(numOfSameCards/52 < 0.06)
            isShuffledEnough = true;
        assertEquals(true, isShuffledEnough);
        CardDealer.shuffle(cardsToShuffle);
        assertNotEquals(cardsToShuffle, unshuffledCards);
    }

    /**
     * TODO: Test Engineering Code Challenge
     * Verify that {@link CardDealer#deal(List, Deck)} correctly deals {@link Card}s to all of the
     * {@link Player}s provided. Don't just do a happy path approach!
     */
    @Test
    public void deal() {
        // get a deck of cards
        Deck deck = new Deck();

        List<Player> players = new ArrayList<>();
        players.add(new Player("Valerie"));
        players.add(new Player("Natasha"));
        players.add(new Player("Sasha"));

        Game game = CardDealer.deal(players, deck);
        for(int i = 0; i < players.size()-1; i++){
            assertNotEquals(players.get(i).getCards(), players.get(i+1).getCards());
            assertEquals(players.get(i).getCards().size(), players.get(i+1).getCards().size());
        }
        assertNotEquals(52, game.getUndealtCards().size());
        assertEquals(17, players.get(0).getCards().size());
        assertEquals(1, game.getUndealtCards().size());
        assertNotNull(game);
    }

    /**
     * TODO: Test Engineering Code Challenge
     * Verify that {@link CardDealer#deal(List, Deck, int)} correctly deals the specified number of {@link Card}s
     * to each of the {@link Player}s provided. Don't just do a happy path approach!
     */
    @Test
    public void dealNumberOfCardsPerPlayer() {
        // get a deck of cards
        Deck deck = new Deck();

        // TODO: generate a list of players
        List<Player> players = new ArrayList<>();
        players.add(new Player("Jay"));
        players.add(new Player("Eric"));
        players.add(new Player("Mark"));
        players.add(new Player("Paul"));
        players.add(new Player("Ed"));
        players.add(new Player("Tom"));
        int numberOfCards = 5;

        // TODO: replace the following once the corresponding CardDealer deal functionality is working
        Game game = CardDealer.deal(players, deck, numberOfCards);
        assertNotNull(game);
        for(int i = 0; i < players.size()-1; i++){
            assertNotEquals(players.get(i).getCards(), players.get(i+1).getCards());
            assertEquals(players.get(i).getCards().size(), players.get(i+1).getCards().size());
            assertNotEquals(players.get(i).getCards(), players.get(i+1).getCards());
        }
        assertEquals(22, game.getUndealtCards().size());
    }

}
