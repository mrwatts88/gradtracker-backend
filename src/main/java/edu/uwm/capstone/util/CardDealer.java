package edu.uwm.capstone.util;

import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

import edu.uwm.capstone.model.cards.*;

/**
 * This class provides the {#shuffle} and {@link #deal} functionality that supports the deck of cards object model:
 * <ul>
 *     <li>A {@link Game} consists of a list of {@link Player}s and a list of undealt {@link Card}s</li>
 *     <li>A {@link Player} consists of a {@link Player#name} and the {@link Card}s that have been dealt to them</li>
 *     <li>A {@link Deck} consists of a list of {@link Card}s </li>
 *     <li>A {@link Card} is comprised of a {@link Rank} and a {@link Suit}</li>
 * </ul>
 */
public class CardDealer {

    private CardDealer() {
    }

    /**
     * TODO: Test Engineering Open Code Challenge
     * Deal as many cards as possible to the players provided.
     * @param players List of {@link Player}s to be dealt cards
     * @param deck The {@link Deck} of {@link Card}s to be used
     * @return {@link Game} comprised of the {@link Player}s and their individual {@link Card}s
     * along with any undealt {@link Card}s that would remain
     */
    public static Game deal(List<Player> players, Deck deck) {
        Assert.notNull(players, "Players cannot be null");
        Assert.notNull(deck, "Deck cannot be null");

        // TODO: this is where the work goes
        // TODO: this is NOT the correct solution, it is simply returning the input values
        return new Game(players, deck.getCards());
    }

    /**
     * TODO: Test Engineering Open Code Challenge
     * Deal the specified number of cards to the players provided.
     * @param players List of {@link Player}s to be dealt cards
     * @param deck The {@link Deck} of {@link Card}s to be used
     * @param numberOfCardsPerPlayer The number of cards to deal to each player.
     * @return {@link Game} comprised of the {@link Player}s and their individual {@link Card}s
     * along with any undealt {@link Card}s that would remain
     */
    public static Game deal(List<Player> players, Deck deck, int numberOfCardsPerPlayer) {
        Assert.notNull(players, "Players cannot be null");
        Assert.notNull(deck, "Deck cannot be null");

        // TODO: this is where the work goes
        // TODO: this is NOT the correct solution, it is simply returning the input values
        return new Game(players, deck.getCards());
    }

    /**
     * Shuffle/randomize the provided {@link Deck}.
     */
    protected static Deck shuffle(Deck deck) {
        Assert.notNull(deck, "Deck cannot be null");
        Collections.shuffle(deck.getCards());
        return deck;
    }

    /**
     * Shuffle/randomize the provided list of {@link Card}s.
     */
    protected static List<Card> shuffle(List<Card> cards) {
        Assert.notNull(cards, "List<Card> cannot be null");
        Collections.shuffle(cards);
        return cards;
    }

}
