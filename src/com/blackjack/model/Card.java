package com.blackjack.model;

/**
 * Represents the a card from a Blackjack deck.
 * Contains the face of the card (from 2-Ace) and the value associated with it.
 */
public class Card {

    private String face;
    private int value;

    public Card(String face, int value) {
        this.face = face;
        this.value = value;
    }

    public String getFace() {
        return face;
    }

    public int getValue() {
        return value;
    }
}
