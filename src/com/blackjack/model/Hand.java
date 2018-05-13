package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.card.Card;

/**
 * Represents a hand, which contains multiple cards, in the game of Blackjack.
 */
public class Hand {

    private ArrayList<Card> hand;

    /**
     * Every hand is given a default bid when it is first initialized.
     * @param playersHand is the list of cards the hand has.
     */
    public Hand(ArrayList<Card> playersHand) {
        this.hand = playersHand;
    }

    /**
     * Adds a card to the hand.
     * @param card to be added.
     */
    public void add(Card card) {
        hand.add(card);
    }

    /**
     * Removes and
     * @return the first card in the hand.
     */
    public Card remove() {
        return hand.remove(0);
    }

    /**
     * Gets the size of the hand.
     * @return the number of cards in the hand.
     */
    public int getSize() {
        return hand.size();
    }

    public ArrayList<Card> getCards() {
        return hand;
    }

}
