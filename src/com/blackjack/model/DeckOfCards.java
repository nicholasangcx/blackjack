package com.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;

import com.blackjack.model.card.Card;
import com.blackjack.model.card.CardFace;
import com.blackjack.model.card.CardSuit;

/**
 * Represents a normal Blackjack deck, with 52 cards and no Joker.
 */
public class DeckOfCards {

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

        for (CardFace face : CardFace.values()) {
            for (CardSuit suit : CardSuit.values()) {
                deck.add(new Card(face, suit));
            }
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
    public Card remove() {
        return deck.remove(deck.size() - 1);
    }

}
