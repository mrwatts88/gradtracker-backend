package edu.uwm.capstone.model.cards;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

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
public class Card {

    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        Assert.notNull(rank, "Rank cannot be null");
        Assert.notNull(suit, "Suit cannot be null");
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Card && (this == object || EqualsBuilder.reflectionEquals(this, object));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
