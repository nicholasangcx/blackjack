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

    /** Contains a list of all the hands the player has currently. */
    private ArrayList<Hand> hands;
    private ArrayList<Integer> bids;
    private int balance;

    public Player() {
        super();
        balance = STARTING_MONEY;
        bids = new ArrayList<Integer>();
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
        ArrayList<Card> additionalList = new ArrayList<Card>();

        Card firstCard = hands.get(currentHand).remove();
        additionalList.add(firstCard);
        hands.add(currentHand, new Hand(additionalList));
    }
}
