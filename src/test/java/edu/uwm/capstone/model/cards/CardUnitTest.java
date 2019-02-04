package edu.uwm.capstone.model.cards;

import org.junit.Test;

import static edu.uwm.capstone.helper.TestHelper.randomRank;
import static edu.uwm.capstone.helper.TestHelper.randomSuit;
import static org.junit.Assert.*;

public class CardUnitTest {

    /**
     * Verify that we can create {@link Card}s for every {@link Rank} and {@link Suit} that is defined.
     */
    @Test
    public void verifyConstructor() {
        Card card;
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                card = new Card(rank, suit);
                assertNotNull(card);
            }
        }
    }

    /**
     * Verify that a request to create a {@link Card} with an null {@link Rank} value produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyConstructorNullRank() {
        Suit suit = randomSuit();
        new Card(null, suit);
    }

    /**
     * Verify that a request to create a {@link Card} with an null {@link Suit} value produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyConstructorNullSuit() {
        Rank rank = randomRank();
        new Card(rank, null);
    }

    /**
     * Verify {@link Card#equals} is working correctly.
     */
    @Test
    public void verifyEquals() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                Card card1 = new Card(rank, suit);
                Card card2 = new Card(rank, suit);
                assertEquals(card1, card2);
            }
        }
    }

    /**
     * Verify {@link Card#equals} is correctly determining inequality.
     */
    @Test
    public void verifyNotEquals() {
        Card previousCard = null;
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                if (previousCard != null) {
                    Card currentCard = new Card(rank, suit);
                    assertNotEquals(currentCard, previousCard);
                }
                previousCard = new Card(rank, suit);
            }
        }
        assertNotEquals(previousCard, null);
    }

    /**
     * Verify {@link Card#hashCode} is working correctly.
     */
    @Test
    public void verifyHashCode() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                Card card = new Card(rank, suit);
                assertNotNull(card.hashCode());
            }
        }
    }

    /**
     * Verify {@link Card#toString} is working correctly.
     */
    @Test
    public void verifyToString() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                Card card = new Card(rank, suit);
                assertNotNull(card.toString());
            }
        }
    }

}
