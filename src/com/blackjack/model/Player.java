package com.blackjack.model;

import java.util.ArrayList;

/**
 * Represents a player in the game of Blackjack, keeping track of his
 * balance cash and hands in the game.
 */
public class Player {

    private static final int STARTING_MONEY = 100;
    private static final String DELIMITER = " ,";

    /** Contains a list of all the hands the player has currently. */
    private ArrayList<Hand> hands;
    private int balance;

    public Player() {
        balance = STARTING_MONEY;
        hands = new ArrayList<Hand>();
    }

    /**
     * This method adds a card to the specified hand of the player (to deal with split hand situations).
     * @param card the card that is dealt.
     * @param currentHand the hand that we are adding this card to.
     */
    public void addCard(Card card, int currentHand) {
        // Dealing the cards out at the start
        if (currentHand == hands.size()) {
            ArrayList<Card> cards = new ArrayList<Card>();
            cards.add(card);
            hands.add(new Hand(cards));
        } else {
            hands.get(currentHand).add(card);
        }
    }

    /**
     * Returns the balance cash the player has left after placing his bids.
     * Do not count his bids as part of his balance cash.
     * @return The balance he has minus his bids.
     */
    public int getBalance() {
        int cashInBids = 0;
        for (Hand hand : hands) {
            cashInBids += hand.getCurrentBid();
        }
        return (balance - cashInBids);
    }

    public ArrayList<Hand> getHands() {
        return hands;
    }

    /**
     * This method concatenates all the face of cards the player has in all of his hands
     * and represents them in a String format.
     * @return A String of all the card faces the player has.
     */
    public String getCardsAsString() {
        String result = "";
        for (Hand hand : hands) {
            for (Card card : hand.getCards()) {
                result += card.getFace() + " ";
            }
            result += DELIMITER;
        }
        // Remove the last delimiter.
        return (result.substring(0, result.length() - 2));
    }

    /**
     * This method concatenates all the bid the player has in all of his hands and represents
     * them in a String format. The order of hands is similar to the {@method getCardsAsString()}.
     * @return A String of all the bids on different hands the player has.
     */
    public String getBidsAsString() {
        String result = "";
        for (Hand hand : hands) {
            result += Integer.toString(hand.getCurrentBid()) + DELIMITER;
        }
        // Remove the last delimiter.
        return (result.substring(0, result.length() - 2));
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    public void decreaseBalance(int amount) {
        balance -= amount;
    }

    /**
     * Resets the hand of the player (for a new game).
     */
    public void clearHands() {
        hands = new ArrayList<Hand>();
    }

    /**
     * Splits the hand into two hands.
     * Remove the first card, add it into a hand, and insert it into the ArrayList of hands.
     * This way, we do not have to worry about OutOfBoundsException and the card order will not be reversed.
     * @param currentHand denotes the hand that we are splitting.
     */
    public void splitHands(int currentHand) {
        ArrayList<Card> additionalList = new ArrayList<Card>();

        Card firstCard = hands.get(currentHand).remove();
        additionalList.add(firstCard);
        hands.add(currentHand, new Hand(additionalList));
    }
}
