package com.blackjack.ui;

import static com.blackjack.common.Messages.MESSAGE_LOST;
import static com.blackjack.common.Messages.MESSAGE_TIE;
import static com.blackjack.common.Messages.MESSAGE_WON;

import java.util.ArrayList;

import com.blackjack.logic.ProcessHand;
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
    private static final String CARD_HIDDEN = "X";
    private static final String DELIMITER = ", ";

    private String dealerCards;
    private ArrayList<String> playerCards;
    private ArrayList<String> playerBids;
    private ArrayList<Integer> playerBalance;

    /** Used to determine if there is previously undisplayed information */
    public boolean hasNewInfo;

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
            updatePlayer(player);
        }
    }

    /**
     * Takes in the data of the dealer as strings. Except the second card which is hidden.
     * @param dealer the dealer whose data we are taking in.
     */
    public void updateDealer(Dealer dealer) {
        ArrayList<Hand> dealerHands = dealer.getHands();
        Card card = dealerHands.get(0).getCards().get(0);
        dealerCards = "";
        dealerCards += card.getFace().face() + card.getSuit().suit() + " " + CARD_HIDDEN;
    }

    private void updatePlayer(Player player) {
        ArrayList<Hand> hands = player.getHands();
        ArrayList<Integer> bids = player.getBids();

        String cards = "";
        for (Hand hand : hands) {
            for (Card card : hand.getCards()) {
                cards += card.getFace().face() + card.getSuit().suit() + " ";
            }
            cards += DELIMITER;
        }
        // Remove the last delimiter.
        playerCards.add(cards.substring(0, cards.length() - 2));

        String totalBids = "";
        for (int bid : bids) {
            totalBids += Integer.toString(bid) + " " + DELIMITER;
        }
        // Remove the last delimiter.
        playerBids.add(totalBids.substring(0, totalBids.length() - 2));

        playerBalance.add(player.getBalance());

    }

    public void updatePlayer(Player player, int playerNum) {
        ArrayList<Hand> hands = player.getHands();
        ArrayList<Integer> bids = player.getBids();

        String cards = "";
        for (Hand hand : hands) {
            for (Card card : hand.getCards()) {
                cards += card.getFace().face() + card.getSuit().suit() + " ";
            }
            cards += DELIMITER;
        }
        playerCards.remove(playerNum);
        // Remove the last delimiter.
        playerCards.add(playerNum, cards.substring(0, cards.length() - 2));

        String totalBids = "";
        for (int bid : bids) {
            totalBids += Integer.toString(bid) + " " + DELIMITER;
        }
        playerBids.remove(playerNum);
        // Remove the last delimiter.
        playerBids.add(playerNum, totalBids.substring(0, totalBids.length() - 2));

        playerBalance.remove(playerNum);
        playerBalance.add(playerNum, player.getBalance());
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
            dealerCards += card.getFace().face() + card.getSuit().suit() + " ";
        }
        return this;
    }

    /**
     * Determines the winner of this iteration of the game
     * @return the outcome with respect to the player
     * It also updates if there is any new information to be provided to the player.
     */
    public String determineWinner(Player player, int playerNum, Dealer dealer) {
        hasNewInfo = false;
        ArrayList<Card> dealerCards = dealer.getHands().get(0).getCards();
        ArrayList<Hand> playerHands = player.getHands();
        Hand dealerHand = new Hand(dealerCards);

        String result = "";
        /** Situations where we have to compare the values */
        int dealerValue = ProcessHand.calculateValue(dealerHand);

        /** Calculate and determine win/loss for each hand of the player */
        int handNum = 1;
        for (int i = 0; i <playerHands.size(); i++) {
            if (player.getBids().get(i) != 0) {
                if (i > 0) {
                    result += "\n";
                }
                int playerValue = ProcessHand.calculateValue(playerHands.get(i));
                result += "Player " + playerNum + " Hand " + handNum + ": ";
                // Determining win/loss of current hand
                if (playerValue > dealerValue || ProcessHand.isMoreThan21(dealerHand)) {
                    result += MESSAGE_WON;
                    player.increaseBalance(player.getBids().get(i));
                } else if (dealerValue > playerValue) {
                    result += MESSAGE_LOST;
                    player.decreaseBalance(player.getBids().get(i));
                } else {
                    result += MESSAGE_TIE;
                }

                player.clearBid(i);
                hasNewInfo = true;
            }
            handNum++;
        }
        return result;
    }

    /**
     * Concatenates all the details of the current GameState together.
     * Used to display a summary of the current state of the game to the player.
     * @return a String with all the details and with "," as a delimiter
     * between different hands and their respective bids.
     */
    @Override
    public String toString() {
        String result = "\n" + CARDS_DEALER + dealerCards + "\n\n";
        for (int playerNum = 1; playerNum <= playerCards.size(); playerNum++) {
            result += "Player " + playerNum + " Cards: \n" + playerCards.get(playerNum-1)
                    + "\n" + "Current Bid: " + playerBids.get(playerNum-1)
                    + "\n" + "Balance Left: " + playerBalance.get(playerNum-1) + "\n\n";
        }
        return result;
    }
}
