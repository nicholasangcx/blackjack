package com.blackjack.logic;

import com.blackjack.model.Dealer;
import com.blackjack.model.DeckOfCards;
import com.blackjack.model.Player;
import com.blackjack.ui.GameState;

/**
 * Logic used to deal the cards out to players for each iteration of the game
 */
public class Deal {

    /** The number of cards in the starting hand of Blackjack */
    private static int STARTING_HAND = 2;

    private DeckOfCards deck;
    private GameState state;

    public Deal(GameState state) {
        deck = new DeckOfCards();
        this.state = state;
    }

    /**
     * Starts the game by dealing the required 2 cards to the player and dealer
     * @param dealer the dealer the cards are dealt to
     * @param player the player the cards are dealt to
     */
    public void initialHand(Dealer dealer, Player player) {
        for (int i = 0 ; i < STARTING_HAND; i++) {
            dealCard(player, state, 0);
            dealCard(dealer, state);
        }
    }

    /**
     * Deals a single card to the the player
     * @param player the player the cards are dealt to
     * @param currentHand the specific hand of the player that the cards are dealt to.
     *                    This is for the cases where a player has multiple hands.
     */
    public void dealCard(Player player, int currentHand) {
        dealCard(player, state, currentHand);
    }

    /**
     * Overloaded method to help update the GameState after the player receives a new card.
     * @param player the current player involved
     * @param state the state of the game (to be updated)
     * @param currentHand the current hand of the player we are processing
     */
    private void dealCard(Player player, GameState state, int currentHand) {
        player.addCard(deck.dealCard(), currentHand);
        state.updateState(player);
    }

    /**
     * Deals a single card to the dealer.
     * @param dealer the dealer the cards are dealt to
     */
    public void dealCard(Dealer dealer) {
        dealer.addCard(deck.dealCard());
    }

    /**
     * Overloaded method to help update the GameState after the dealer receives a new card.
     * @param dealer the current dealer
     * @param state the state of the game (to be updated)
     */
    private void dealCard(Dealer dealer, GameState state) {
        dealer.addCard(deck.dealCard());
        state.updateState(dealer);
    }

}
