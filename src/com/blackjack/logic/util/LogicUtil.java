package com.blackjack.logic.util;

import static com.blackjack.common.Messages.MESSAGE_LOST;
import static com.blackjack.common.Messages.MESSAGE_TIE;
import static com.blackjack.common.Messages.MESSAGE_WON;

import java.util.ArrayList;

import com.blackjack.logic.Deal;
import com.blackjack.logic.DealerRobot;
import com.blackjack.logic.GameLogic;
import com.blackjack.logic.ProcessHand;
import com.blackjack.model.card.Card;
import com.blackjack.model.Dealer;
import com.blackjack.model.Hand;
import com.blackjack.model.Player;
import com.blackjack.ui.GameState;

/**
 * Some utility methods for the Logic class used to execute user options,
 * call the dealer robot, and to determine the winner of the game.
 */
public class LogicUtil {

    public static final int STAND = 1;
    public static final int HIT = 2;
    public static final int DOUBLE = 3;
    public static final int SURRENDER = 4;
    public static final int SPLIT = 5;

    /** When taking the value of ACE to be 11 causes the hand to just exceed 21 */
    private static final int VALUE_WITH_ACE_11 = 22;

    /** Used to determine if there is previously undisplayed information */
    public boolean hasNewInfo;

    private Player player;
    private Dealer dealer;
    private Deal deal;
    private GameState state;

    private DealerRobot robot;

    public LogicUtil(Deal deal, Dealer dealer, Player player, GameState state) {
        this.player = player;
        this.dealer = dealer;
        this.deal = deal;
        this.state = state;

        robot = new DealerRobot(deal, dealer);
    }

    /**
     * Handles the execution when player chooses not to be dealt anymore cards
     */
    public void stand() {
        /**
         * Do not require any more cards, so do nothing.
         * Let the execution proceed to the next hand, or the dealer.
         */
    }

    /**
     * Handles the execution when player chooses to be dealt a card
     */
    public void hit(int currentHandNum) {
        deal.dealCard(player, currentHandNum);
        state.updateState(player);
    }

    /**
     * Handles the execution when player chooses to double down on their initial bid
     */
    public void doubleDown(Hand currentHand, int currentHandNum) {
        currentHand.setCurrentBid(currentHand.getCurrentBid() * 2);
        deal.dealCard(player, currentHandNum);
        state.updateState(player);
    }

    /**
     * Handles the execution when player chooses to surrender and save half their bid
     */
    public void surrender(Hand currentHand) {
        player.decreaseBalance(currentHand.getCurrentBid()/2);
        currentHand.clearBid();
    }

    /**
     * Handles the execution when player chooses to split his hand.
     * Split your 2 equal value cards into 2 hands. One additional card is dealt
     * to each hand immediately since the player has no choice but to take it anyway.
     */
    public void split(int currentHandNum) {
        player.splitHands(currentHandNum);

        deal.dealCard(player, currentHandNum);
        int nextHand = currentHandNum + 1;
        deal.dealCard(player, nextHand);

        state.updateState(player);
    }

    /**
     * Calls the dealer robot to execute dealer logic (Dealer's turn)
     * Checks who the winner is after since everyone has already completed their turns.
     * @return the results of the game with respect to the player.
     */
    public String executeRobot() {
        robot.execute();

        String result = determineWinner(dealer, player);
        state.updateState(player);

        return result;
    }

    /**
     * Determines the winner of this iteration of the game
     * @param dealer to check and calculate the dealer's card value
     * @param player to check and calculate the player's card value
     * @return the outcome with respect to the player
     * It also updates if there is any new information to be provided to the player.
     */
    public String determineWinner(Dealer dealer, Player player) {
        hasNewInfo = false;
        ArrayList<Card> dealerCards = dealer.getCards();
        ArrayList<Hand> playerHands = player.getHands();

        String result = "";

        /** First consider the situations where we do not have to compare the value */
        Hand dealerHand = new Hand(dealerCards);
        if (ProcessHand.isBlackjack(dealerHand)) {
            result = MESSAGE_LOST;
            hasNewInfo = true;
            return result;
        }

        /** Situations where we have to compare the values */
        int dealerValue = ProcessHand.calculateValue(dealerHand);

        /** Calculate and determine win/loss for each hand of the player */
        int handNum = 1;
        for (Hand hand : playerHands) {
            if (hand.getCurrentBid() != 0) {
                int playerValue = ProcessHand.calculateValue(hand);

                result += "Hand " + handNum + ": ";
                // Determining win/loss of current hand
                if (playerValue > dealerValue || ProcessHand.isMoreThan21(dealerHand)) {
                    result += MESSAGE_WON;
                    player.increaseBalance(hand.getCurrentBid());
                } else if (dealerValue > playerValue) {
                    result += MESSAGE_LOST;
                    player.decreaseBalance(hand.getCurrentBid());
                } else {
                    result += MESSAGE_TIE;
                }
                result += "\n";
                hand.clearBid();
                hasNewInfo = true;
            }
            handNum++;
        }
        return result;
    }
}
