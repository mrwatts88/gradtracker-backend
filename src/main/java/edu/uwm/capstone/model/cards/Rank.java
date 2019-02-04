package edu.uwm.capstone.model.cards;

import edu.uwm.capstone.util.CardDealer;

/**
 * The deck of cards object model is as follows:
 * <ul>
 *     <li>A {@link Game} consists of a list of {@link Player}s and a list of undealt {@link Card}s</li>
 *     <li>A {@link Player} consists of a {@link Player#name} and the {@link Card}s that have been dealt to them</li>
 *     <li>A {@link Deck} consists of a list of {@link Card}s</li>
 *     <li>A {@link Card} is comprised of a {@link Rank} and a {@link Suit}</li>
 * </ul>
 *
 * A deck of cards is shuffled via {@link CardDealer#shuffle}
 * and dealt from via {@link CardDealer#deal}.
 */
public enum Rank {

    ACE("Ace", new int[]{1,11}),
    TWO("Two", new int[]{2}),
    THREE("Three", new int[]{3}),
    FOUR("Four", new int[]{4}),
    FIVE("Five", new int[]{5}),
    SIX("Six", new int[]{6}),
    SEVEN("Seven", new int[]{7}),
    EIGHT("Eight", new int[]{8}),
    NINE("Nine", new int[]{9}),
    TEN("Ten", new int[]{10}),
    JACK("Jack", new int[]{10}),
    QUEEN("Queen", new int[]{10}),
    KING("King", new int[]{10}),
    ;

    Rank(String displayName, int[] value) {
        this.displayName = displayName;
        this.value = value;
    }

    private String displayName;
    private int[] value;

    public String getDisplayName() {
        return displayName;
    }

    public int[] getValue() {
        return value;
    }

}
