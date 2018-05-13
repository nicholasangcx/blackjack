package com.blackjack.logic.util;

import com.blackjack.logic.Deal;
import com.blackjack.logic.DealerRobot;
import com.blackjack.model.Table;
import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.gamblers.Player;
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

    private Table table;
    private Player player;
    private Dealer dealer;
    private Deal deal;
    private GameState state;

    private DealerRobot robot;

    public LogicUtil(Deal deal, Table table, GameState state) {
        this.table = table;
        this.deal = deal;
        this.state = state;
        dealer = (Dealer)table.getGamblers().get(0);

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
    public void hit(int currentHandNum, int playerNum) {
        player = (Player)table.getGamblers().get(playerNum+1);
        deal.dealCard(player, currentHandNum);
        state.updatePlayer(player, playerNum);
    }

    /**
     * Handles the execution when player chooses to double down on their initial bid
     */
    public void doubleDown(int currentHandNum, int playerNum) {
        player = (Player)table.getGamblers().get(playerNum+1);
        player.setBid(currentHandNum, player.getBids().get(currentHandNum) * 2);
        deal.dealCard(player, currentHandNum);
        state.updatePlayer(player, playerNum);
    }

    /**
     * Handles the execution when player chooses to surrender and save half their bid
     */
    public void surrender(int currentHandNum, int playerNum) {
        player = (Player)table.getGamblers().get(playerNum+1);
        player.decreaseBalance(player.getBids().get(currentHandNum) / 2);
        player.clearBid(currentHandNum);
    }

    /**
     * Handles the execution when player chooses to split his hand.
     * Split your 2 equal value cards into 2 hands. One additional card is dealt
     * to each hand immediately since the player has no choice but to take it anyway.
     */
    public void split(int currentHandNum, int playerNum) {
        player = (Player)table.getGamblers().get(playerNum+1);
        player.splitHands(currentHandNum);

        deal.dealCard(player, currentHandNum);
        int nextHand = currentHandNum + 1;
        deal.dealCard(player, nextHand);

        player.addBid();
        state.updatePlayer(player, playerNum);
    }

    /**
     * Calls the dealer robot to execute dealer logic (Dealer's turn)
     * Checks who the winner is after since everyone has already completed their turns.
     * @return the results of the game with respect to the player.
     */
    public void executeRobot() {
        robot.execute();
        state.updateDealer(dealer);
    }

}
