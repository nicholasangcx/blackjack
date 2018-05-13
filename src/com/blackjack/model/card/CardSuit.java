package com.blackjack.model.card;

/**
 * Enumerations for the suits of a card in a regular deck.
 */
public enum CardSuit {
    CLUBS("C"),
    HEARTS("H"),
    DIAMONDS("D"),
    SPADES("S");

    private final String suit;

    CardSuit(String suit) {
        this.suit= suit;
    }

    public String suit() {
        return this.suit;
    }
}
