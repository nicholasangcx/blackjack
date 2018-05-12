package com.blackjack.logic;

import java.util.ArrayList;

import com.blackjack.model.Table;
import com.blackjack.model.DeckOfCards;
import com.blackjack.model.gamblers.Gambler;

/**
 * Logic used to deal the cards out to players for each iteration of the game
 */
public class Deal {

    /** The number of cards in the starting hand of Blackjack */
    private static int STARTING_HAND = 2;

    private DeckOfCards deck;

    public Deal() {
        deck = new DeckOfCards();
    }

    /**
     * Starts the game by dealing the required 2 cards to everyone on the table
     * @param table The table at which we are dealing to
     */
    public void initialHand(Table table) {
        ArrayList<Gambler> gamblers = table.getGamblers();
        for (int i = 0; i < STARTING_HAND; i++) {
            for (Gambler gambler : gamblers) {
                dealCard(gambler, 0);
            }
        }
    }

    /**
     * Deals a single card to the the gambler
     * @param gambler the gambler the cards are dealt to
     * @param currentHand the specific hand of the gambler that the cards are dealt to
     */
    public void dealCard(Gambler gambler, int currentHand) {
        gambler.addCard(deck.remove(), currentHand);
    }

}
