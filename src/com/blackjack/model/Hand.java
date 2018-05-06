package com.blackjack.model;

import java.util.ArrayList;

/**
 * Represents a hand, which contains multiple cards, in the game of Blackjack.
 */
public class Hand {

    private static final int STARTING_HAND_SIZE = 2;
    private static final int MAXIMUM_POINTS = 21;
    private static final int DEALER_MIN = 17;
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
     * @return true if this hand is a starting hand
     */
    public boolean isStartingHand() {
        return hand.size() == STARTING_HAND_SIZE;
    }

    /**
     * Called only on starting hands.
     * @return true if this is a Blackjack hand
     */
    public boolean isBlackjack() {
        assert(isStartingHand());
        return (hand.get(0).getFace().equals("A") && hand.get(1).getValue() == 10)
                    || (hand.get(0).getValue() == 10 && hand.get(1).getFace().equals("A"));
    }

    /**
     * Called only on starting hands.
     * @return true if both cards have the same value
     */
    public boolean isEqualRank() {
        assert(isStartingHand());
        return hand.get(0).getValue() == hand.get(1).getValue();
    }

    /**
     * @return true if the sum of all the card values is equal to or less than 21.
     */
    public boolean is21OrLess() {
        int totalValue = 0;
        boolean containsAce = false;
        for (Card card : hand) {
            totalValue += card.getValue();
            if (card.getFace().equals("A")) {
                containsAce = true;
            }
        }
        // account for the changing ACE value
        if ((totalValue > MAXIMUM_POINTS) && containsAce) {
            totalValue--;
        }
        return totalValue <= MAXIMUM_POINTS;
    }

    /**
     * @return true if the sum of all the card values is less than the minimum requirement for the dealer.
     */
    public boolean isLessThanDealerMin() {
        int totalValue = 0;
        for (Card card : hand) {
            totalValue += card.getValue();
        }
        return totalValue < DEALER_MIN;
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
