package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.card.Card;

/**
 * Represents a hand, which contains multiple cards, in the game of Blackjack.
 */
public class Hand {

    public static final int DEFAULT_BID = 10;

    private ArrayList<Card> hand;
    private int currentBid;

    /**
     * Every hand is given a default bid when it is first initialized.
     * @param playersHand is the list of cards the hand has.
     */
    public Hand(ArrayList<Card> playersHand) {
        this.hand = playersHand;
        currentBid = DEFAULT_BID;
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

    public int getSize() {
        return hand.size();
    }

    public ArrayList<Card> getCards() {
        return hand;
    }

    public int getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(int currentBid) {
        this.currentBid = currentBid;
    }

    /**
     * Resets the bid to zero.
     */
    public void clearBid() {
        currentBid = 0;
    }
}
