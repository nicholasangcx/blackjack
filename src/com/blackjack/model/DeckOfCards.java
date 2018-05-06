package com.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a normal Blackjack deck, with 52 cards and no Joker.
 */
public class DeckOfCards {

    private static final int NUM_REPETITIONS = 4;
    private static final int NUM_NUMBER_CARDS = 9;
    private static final int VALUE_FIRST_NUM_CARD = 2;
    private static final int VALUE_ACE = 11;
    private static final int VALUE_PICTURE_CARD = 10;

    private ArrayList<Card> deck;

    /**
     * Creates a new empty arraylist of cards as the deck.
     */
    public DeckOfCards() {
        deck = new ArrayList<Card>();
        populateDeck();
        shuffleDeck();
    }

    /**
     * Populates the arraylist with the required 52 cards.
     */
    private void populateDeck() {
        for (int i = 0; i < NUM_REPETITIONS; i++) {

            // Add the ACE card first
            deck.add(new Card("A", VALUE_ACE));

            // Add the number cards
            for (int j = VALUE_FIRST_NUM_CARD; j < NUM_NUMBER_CARDS + VALUE_FIRST_NUM_CARD; j++) {
                deck.add(new Card(String.valueOf(j), j));
            }

            // Add the JACK card
            deck.add(new Card("J", VALUE_PICTURE_CARD));

            // Add the QUEEN card
            deck.add(new Card("Q", VALUE_PICTURE_CARD));

            // Add the KING card
            deck.add(new Card("K", VALUE_PICTURE_CARD));
        }
    }

    /**
     * Shuffles the deck so that the cards will no longer be in order
     */
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Deals the card at the top of the deck to the relevant player.
     * Removes the last element in the shuffled ArrayList.
     * We chose the last element since it is faster to remove it and we will not have to shift all the elements within
     * It still works since we can just consider the arraylist to contain the cards in reverse order
     */
    public Card dealCard() {
        return deck.remove(deck.size() - 1);
    }

}
