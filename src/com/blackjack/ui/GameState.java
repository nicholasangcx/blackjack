package com.blackjack.ui;

import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.gamblers.Player;

/**
 * Represents the state of the game, contains all information about the player and dealer's cards,
 * the player's current bids and balance cash.
 */
public class GameState {

    private static final String CARDS_DEALER = "Dealer's Cards: \n";
    private static final String CARDS_PLAYER = "Your Cards: \n";

    private String playerCards;
    private String dealerCards;
    private String currentBid;
    private int balance;

    public GameState() {

    }

    /**
     * Takes in all the data of this player as strings.
     * @param player the player whose data we are taking in.
     */
    public void updateState(Player player) {
        // Player's attributes
        playerCards = player.getCardsAsString();
        balance = player.getBalance();
        currentBid = player.getBidsAsString();
    }

    /**
     * Takes in the data of the dealer as strings. Except the second card which is hidden.
     * @param dealer the dealer whose data we are taking in.
     */
    public void updateState(Dealer dealer) {
        // Dealer's attributes
        dealerCards = dealer.getHiddenCardsAsString();
    }

    /**
     * Takes in all the data of the dealer. Cards are no longer hidden.
     * @param dealer the dealer whose data we are taking in.
     * @return the state of the game.
     */
    public GameState revealDealer(Dealer dealer) {
        dealerCards = dealer.getCardsAsString();
        return this;
    }

    /**
     * Concatenates all the details of the current GameState together.
     * Used to display a summary of the current state of the game to the player.
     * @return a String with all the details and with "," as a delimiter
     * between different hands and their respective bids.
     */
    @Override
    public String toString() {
        String result = "\n" + CARDS_DEALER + dealerCards
                    + "\n\n" + CARDS_PLAYER + playerCards
                    + "\n\n" + "Current Bid: " + currentBid
                    + "\n" + "Balance Left: " + balance + "\n";
        return result;
    }
}
