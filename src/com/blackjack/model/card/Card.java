package com.blackjack.model.card;

/**
 * Represents the a card from a Blackjack deck.
 * Contains the face of the card (from 2-Ace) and the value associated with it.
 */
public class Card {

    private CardFace face;
    private CardSuit suit;

    public Card(CardFace face, CardSuit suit) {
        this.face = face;
        this.suit = suit;
    }

    public CardFace getFace() {
        return face;
    }

    public CardSuit getSuit() {
        return suit;
    }
}
