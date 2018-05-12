package com.blackjack.ui;

import java.util.ArrayList;

import com.blackjack.model.Hand;
import com.blackjack.model.Table;
import com.blackjack.model.card.Card;
import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.gamblers.Gambler;
import com.blackjack.model.gamblers.Player;

/**
 * Represents the state of the game, contains all information about the player and dealer's cards,
 * the player's current bids and balance cash.
 */
public class GameState {

    private static final String CARDS_DEALER = "Dealer's Cards: \n";
    private static final String CARDS_PLAYER = "Your Cards: \n";
    private static final String CARD_HIDDEN = "X";
    private static final String DELIMITER = ", ";

    private String dealerCards;
    private ArrayList<String> playerCards;
    private ArrayList<String> playerBids;
    private ArrayList<Integer> playerBalance;

    public GameState() {
        dealerCards = "";
        playerCards = new ArrayList<String>();
        playerBids = new ArrayList<String>();
        playerBalance = new ArrayList<Integer>();
    }
    /**
     * Takes in all the data of the table and converts them to string
     * @param table the table whose data we are taking in.
     */
    public void updateState(Table table) {
        ArrayList<Gambler> gamblers = table.getGamblers();
        updateDealer((Dealer)gamblers.get(0));

        for (int i = 1; i < gamblers.size(); i++) {
            Player player = (Player)gamblers.get(i);
            updatePlayer(player, i);
        }
    }

    /**
     * Takes in the data of the dealer as strings. Except the second card which is hidden.
     * @param dealer the dealer whose data we are taking in.
     */
    private void updateDealer(Dealer dealer) {
        ArrayList<Hand> dealerHands = dealer.getHands();
        dealerCards = "";
        dealerCards += dealerHands.get(0).getCards().get(0).getFace() + " " + CARD_HIDDEN;
    }

    private void updatePlayer(Player player, int gamblerNum) {
        ArrayList<Hand> hands = player.getHands();
        String cards = "";
        for (Hand hand : hands) {
            for (Card card : hand.getCards()) {
                cards += card.getFace() + " ";
            }
            cards += DELIMITER;
        }
        // Remove the last delimiter.
        playerCards.add(cards.substring(0, cards.length() - 2));

        String bids = "";
        for (Hand hand : hands) {
            bids += Integer.toString(hand.getCurrentBid()) + " " + DELIMITER;
        }
        // Remove the last delimiter.
        playerBids.add(bids.substring(0, bids.length() - 2));

        playerBalance.add(player.getBalance());

    }

    /**
     * Takes in all the data of the dealer. Cards are no longer hidden.
     * @param dealer the dealer whose data we are taking in.
     * @return the state of the game.
     */
    public GameState revealDealer(Dealer dealer) {
        dealerCards = "";
        Hand dealerHand = dealer.getHands().get(0);
        for (Card card: dealerHand.getCards()) {
            dealerCards += card.getFace() + " ";
        }
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
                    + "\n\n" + "Current Bid: " + playerBids.toString()
                    + "\n" + "Balance Left: " + playerBalance + "\n";
        return result;
    }
}
