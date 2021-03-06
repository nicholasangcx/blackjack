package com.blackjack.model.gamblers;

import java.util.ArrayList;

import com.blackjack.model.Hand;
import com.blackjack.model.card.Card;

/**
 * Represents a player in the game of Blackjack, keeping track of his
 * balance cash and hands in the game.
 */
public class Player extends Gambler {

    private static final int STARTING_MONEY = 100;
    public static final int DEFAULT_BID = 10;

    /** Contains a list of all the hands the player has currently. */
    private ArrayList<Hand> hands;
    private ArrayList<Integer> bids;
    private int balance;

    public Player() {
        balance = STARTING_MONEY;
    }

    /**
     * Returns the balance cash the player has left after placing his bids.
     * Do not count his bids as part of his balance cash.
     * @return The balance he has minus his bids.
     */
    public int getBalance() {
        int cashInBids = 0;
        for (int bid : bids) {
            cashInBids += bid;
        }
        return (balance - cashInBids);
    }

    public void startingBid() {
        bids = new ArrayList<Integer>();
        bids.add(DEFAULT_BID);
    }

    public ArrayList<Integer> getBids() {
        return bids;
    }

    public void setBid(int handNum, int bid) {
        bids.remove(handNum);
        bids.add(handNum, bid);
    }

    public void addBid() {
        bids.add(DEFAULT_BID);
    }

    /**
     * Resets the bid to zero.
     */
    public void clearBid(int handNum) {
        bids.remove(handNum);
        bids.add(handNum, 0);
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    public void decreaseBalance(int amount) {
        balance -= amount;
    }

    /**
     * Splits the hand into two hands.
     * Remove the first card, add it into a hand, and insert it into the ArrayList of hands.
     * This way, we do not have to worry about OutOfBoundsException and the card order will not be reversed.
     * @param currentHand denotes the hand that we are splitting.
     */
    public void splitHands(int currentHand) {
        hands = super.getHands();
        ArrayList<Card> additionalList = new ArrayList<Card>();

        Card firstCard = hands.get(currentHand).remove();
        additionalList.add(firstCard);
        hands.add(currentHand, new Hand(additionalList));
    }
}
