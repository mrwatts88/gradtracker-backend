package edu.uwm.capstone.model.cards;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeckUnitTest {

    /**
     * Instantiate a {@link Deck} and verify that {@link Deck#getCards} correctly returns the
     * {@link Card}s expected based on the {@link Suit#values} and {@link Rank#values}
     * declared within the class.
     */
    @Test
    public void getCards() {
        // instantiate object
        Deck deck = new Deck();
        assertNotNull(deck);

        // verify getCards produces a collection of card objects
        List<Card> cards = deck.getCards();
        assertNotNull(cards);

        // determine how many cards there should be and verify that is how many are produced
        int rankCount = Rank.values().length;
        int suitCount = Suit.values().length;
        int expectedCardCount = rankCount * suitCount;
        assertEquals(expectedCardCount, deck.getCards().size());

        // verify that the cards produced are unique by using a hashmap for keys to ensure no duplicates
        Map<String, String> cardNameMap = new HashMap<>();
        String cardName;
        for (Card card : cards) {
            cardName = card.getRank().getDisplayName() + " of " + card.getSuit().getDisplayName();
            cardNameMap.put(cardName, cardName);
        }
        assertEquals(expectedCardCount, cardNameMap.size());
    }

    /**
     * Verify {@link Deck#equals} is working correctly.
     */
    @Test
    public void verifyEquals() {
        Deck unshuffledDeck1 = new Deck();
        Deck unshuffledDeck2 = new Deck();
        assertEquals(unshuffledDeck1, unshuffledDeck2);
    }

    /**
     * Verify {@link Deck#equals} is correctly determining inequality.
     */
    @Test
    public void verifyNotEquals() {
        Deck deck1 = new Deck();

        Deck deck2 = new Deck();
        Collections.shuffle(deck2.getCards());

        assertNotEquals(deck1, deck2);
        assertNotEquals(deck1, null);
    }

    /**
     * Verify {@link Deck#hashCode} is working correctly.
     */
    @Test
    public void verifyHashCode() {
        assertNotNull(new Deck().hashCode());
    }

    /**
     * Verify {@link Deck#toString} is working correctly.
     */
    @Test
    public void verifyToString() {
        assertNotNull(new Deck().toString());
    }

}
