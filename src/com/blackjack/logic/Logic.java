package com.blackjack.logic;

import static com.blackjack.common.Messages.MESSAGE_BLACKJACK;
import static com.blackjack.common.Messages.MESSAGE_BUST;
import static com.blackjack.common.Messages.MESSAGE_FIVE_OPTIONS;
import static com.blackjack.common.Messages.MESSAGE_FOUR_OPTIONS;
import static com.blackjack.common.Messages.MESSAGE_INVALID_OPTION;
import static com.blackjack.common.Messages.MESSAGE_LOST;
import static com.blackjack.common.Messages.MESSAGE_QUERY_NEW_GAME;
import static com.blackjack.common.Messages.MESSAGE_REMOVED;
import static com.blackjack.common.Messages.MESSAGE_SURRENDER;
import static com.blackjack.common.Messages.MESSAGE_TIE;
import static com.blackjack.common.Messages.MESSAGE_TWO_OPTIONS;
import static com.blackjack.logic.util.LogicUtil.DOUBLE;
import static com.blackjack.logic.util.LogicUtil.HIT;
import static com.blackjack.logic.util.LogicUtil.SPLIT;
import static com.blackjack.logic.util.LogicUtil.STAND;
import static com.blackjack.logic.util.LogicUtil.SURRENDER;
import static com.blackjack.logic.util.TypeOfHand.BLACKJACK;
import static com.blackjack.logic.util.TypeOfHand.EQUALVALUE;
import static com.blackjack.logic.util.TypeOfHand.EXACTLY21;
import static com.blackjack.logic.util.TypeOfHand.LESSTHAN21;
import static com.blackjack.logic.util.TypeOfHand.MORETHAN21;
import static com.blackjack.logic.util.TypeOfHand.UNEQUALVALUE;
import static com.blackjack.model.gamblers.Player.DEFAULT_BID;

import java.util.ArrayList;

import com.blackjack.logic.util.LogicUtil;
import com.blackjack.logic.util.TypeOfHand;
import com.blackjack.model.Table;
import com.blackjack.ui.GameState;
import com.blackjack.ui.Ui;
import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.Hand;
import com.blackjack.model.gamblers.Player;

/**
 * Contains the main logic of the game
 * Maintains the gamestate, dealer and player state,
 * as well as make calls to the UI.
 */
public class Logic {

    /** Static variables that are used to map user input */
    private static final int YES = 1;
    private static final int NO = 2;

    private Table table;
    private Ui ui;

    private Deal deal;
    private GameState state;
    private LogicUtil logicUtil;
    private Dealer dealer;
    private Player player;

    private int playerNum;
    private int playerNumOneIndex;
    private int handNum;
    private int handNumOneIndex;
    private int currentBid;
    private Hand hand;
    
    public Logic(Table table, Ui ui) {
        this.table = table;
        this.ui = ui;
        dealer = (Dealer)table.getGamblers().get(0);
    }

    /**
     * Re-initializes all the required classes and
     * Starts the game by dealing 2 random cards to each player
     */
    public void startGame() {
        // Re-initializes all the required classes
        state = new GameState();
        deal = new Deal();
        table.clearHands();

        deal.initialHand(table);
        table.startingBids();
        state.updateState(table);
        ui.displayNewGame();
        ui.displayGameState(state);
    }

    /**
     * Check the dealer's initial hand for a Blackjack since the game ends right away if that is the case.
     * @return true if user wants to continue playing another game.
     */
    public boolean processGame() {
        Hand dealerHand = dealer.getHands().get(0);
        ArrayList<Player> players = table.getPlayers();

        /** Determine if dealer has a blackjack to decide if we have to complete the game */
        if (ProcessHand.isBlackjack(dealerHand)) {
            ui.displayGameState(state.revealDealer(dealer));
            for (Player player : players) {
                playerNumOneIndex = 1;
                if (ProcessHand.isBlackjack(player.getHands().get(0))) {
                    ui.displayResult("Player " + playerNumOneIndex + ": " + MESSAGE_TIE);
                } else {
                    ui.displayResult("Player " + playerNumOneIndex + ": " + MESSAGE_LOST);
                }
                playerNumOneIndex++;
            }
        } else {
            for (playerNum = 0; playerNum < players.size(); playerNum++) {
                player = players.get(playerNum);
                playerNumOneIndex = playerNum + 1;
                finishGame();
            }
            logicUtil.executeRobot();
            ui.displayGameState(state.revealDealer(dealer));

            for (int i = 0; i < table.getPlayers().size(); i++) {
                Player player = table.getPlayers().get(i);
                String result = state.determineWinner(player, i+1, dealer);
                state.updatePlayer(player, i);
                /** If there is additional information about the outcome of the game not already provided */
                if (state.hasNewInfo) {
                    ui.displayResult(result);
                    ui.displayBalance(player);
                }
            }
        }

        boolean isValidInput = true;
        int numOfPlayers = 0;
        for (playerNumOneIndex = 1; playerNumOneIndex <= players.size(); playerNumOneIndex++) {
            playerNum = playerNumOneIndex - 1;
            if (players.get(playerNum).getBalance() < DEFAULT_BID) {
                players.remove(playerNum);
                playerNumOneIndex--;
                ui.displayResult("Player " + playerNumOneIndex + MESSAGE_REMOVED);
            } else {
                do {
                    int decision = ui.queryPlayer(playerNumOneIndex, MESSAGE_QUERY_NEW_GAME);
                    if (decision == YES) {
                        numOfPlayers++;
                    } else if (decision == NO) {
                        players.remove(playerNum);
                        playerNumOneIndex--;
                        ui.displayResult("Player " + playerNumOneIndex + MESSAGE_REMOVED);
                    } else {
                        ui.displayResult(MESSAGE_INVALID_OPTION);
                        isValidInput = false;
                    }
                } while (!isValidInput);
            }
        }

        return numOfPlayers > 0;
    }

    /**
     * Execution of the rest of the game, after the initial dealing and processing of cards
     */
    private void finishGame() {
        logicUtil = new LogicUtil(deal, table, state);
        handNum = 0;
        handNumOneIndex = handNum + 1;

        /** Execution of the game, for each hand */
        while (handNum <  player.getHands().size()) {
            hand = player.getHands().get(handNum);
            currentBid = player.getBids().get(handNum);
            TypeOfHand typeOfHand = processHand(hand);
            executeMove(typeOfHand);
            handNum++;
            handNumOneIndex = handNum + 1;
        }
    }

    /**
     * Runs the relevant logic for the type of hand the player has received
     * @param typeOfHand represents the various hands available
     */
    private void executeMove(TypeOfHand typeOfHand) {

        switch (typeOfHand) {

            case BLACKJACK:
                executeBlackjackHand();
                break;

            case EQUALVALUE:
                executeSplittableHand();
                break;

            case UNEQUALVALUE:
                executeNotSplittableHand();
                break;

            case MORETHAN21:
                executeBustHand();
                break;

            case EXACTLY21:
                // do nothing
                break;

            default: // less than 21 case
                executeNormalHand();
        }
    }

    /**
     * Overloaded method to process the hand again since it would have changed from the last time
     * after being dealt a card.
     * @param handNum corresponds to which hand is being played right now.
     * @return an integer that represents the type of hand this is
     */
    private TypeOfHand processHand(int handNum) {
        hand = player.getHands().get(handNum);
        return processHand(hand);
    }

    /**
     * Processes the hand the player has to decide what options are available to him
     * @return an integer that represents the type of hand this is.
     */
    private TypeOfHand processHand(Hand hand) {
        if (ProcessHand.isStartingHand(hand)) {
            if (ProcessHand.isBlackjack(hand)) {
                return BLACKJACK;
            } else if (ProcessHand.isEqualRank(hand)) {
                return EQUALVALUE;
            }
            else {
                return UNEQUALVALUE;
            }
        } else if (ProcessHand.isLessThan21(hand)) { // not starting hand
            return LESSTHAN21;
        } else if (ProcessHand.isExactly21(hand)) {
            return EXACTLY21;
        }
        else { // more than 21
            return MORETHAN21;
        }
    }

    /** ========================== Logic for executing different types of hands ============================ */

    /**
     * The case where player has a Blackjack, and dealer has no Blackjack.
     * Since we already handled the case for a dealer Blackjack previously.
     */
    private void executeBlackjackHand() {
        player.increaseBalance(currentBid * 15/10);
        player.clearBid(handNum);
        state.updatePlayer(player, playerNum);
        ui.displayResult("Player " + playerNumOneIndex + ": " + MESSAGE_BLACKJACK);
        ui.displayBalance(player);
    }

    /**
     * The case where player has 2 cards of the same value in his starting hand.
     * Assume you can still surrender on your first 2 cards of each split hand.
     */
    private void executeSplittableHand() {
        int move = ui.getUserMove("Player " + playerNumOneIndex + " Hand " + handNumOneIndex
                + "\n" + MESSAGE_FIVE_OPTIONS);

        boolean isNotValid = false;

        // do-while loop to ensure that execution can proceed with a valid input.
        do {
            switch (move) {

                case STAND:
                    logicUtil.stand();
                    break;

                case HIT:
                    logicUtil.hit(handNum, playerNum);
                    ui.displayGameState(state);
                    executeMove(processHand(handNum));
                    break;

                case DOUBLE:
                    logicUtil.doubleDown(handNum, playerNum);
                    if (ProcessHand.isMoreThan21(hand)) {
                        executeBustHand();
                    }
                    break;

                case SURRENDER:
                    logicUtil.surrender(handNum, playerNum);
                    ui.displayResult(MESSAGE_SURRENDER);
                    ui.displayBalance(player);
                    break;

                case SPLIT:
                    logicUtil.split(handNum, playerNum);
                    ui.displayGameState(state);
                    handNum--; // to reprocess the current hand again
                    break;

                default:
                    isNotValid = true;
                    ui.displayResult(MESSAGE_INVALID_OPTION);
                    move = ui.getUserMove("Player " + playerNumOneIndex + " Hand " + handNumOneIndex
                            + "\n" + MESSAGE_FIVE_OPTIONS);
            }
        } while (isNotValid);
    }

    /**
     * The case where player does not have 2 cards of the same value in his starting hand.
     */
    private void executeNotSplittableHand() {
        int move = ui.getUserMove("Player " + playerNumOneIndex + " Hand " + handNumOneIndex
                + "\n" + MESSAGE_FOUR_OPTIONS);

        boolean isNotValid = false;

        // do-while loop to ensure that execution can proceed with a valid input.
        do {
            switch (move) {

                case STAND:
                    logicUtil.stand();
                    break;

                case HIT:
                    logicUtil.hit(handNum, playerNum);
                    ui.displayGameState(state);
                    executeMove(processHand(handNum));
                    break;

                case DOUBLE:
                    logicUtil.doubleDown(handNum, playerNum);
                    ui.displayGameState(state);
                    if (ProcessHand.isMoreThan21(hand)) {
                        executeBustHand();
                    }
                    break;

                case SURRENDER:
                    logicUtil.surrender(handNum, playerNum);
                    ui.displayResult(MESSAGE_SURRENDER);
                    ui.displayBalance(player);
                    break;

                default:
                    isNotValid = true;
                    ui.displayResult(MESSAGE_INVALID_OPTION);
                    move = ui.getUserMove("Player " + playerNumOneIndex + " Hand " + handNumOneIndex
                            + "\n" + MESSAGE_FOUR_OPTIONS);
            }
        } while (isNotValid);
    }

    /**
     * The case where the player's cards add up to a value greater than 21.
     */
    private void executeBustHand() {
        player.decreaseBalance(currentBid);
        player.clearBid(handNum);
        state.updatePlayer(player, playerNum);
        ui.displayResult("Player " + playerNumOneIndex + ": " + MESSAGE_BUST);
        ui.displayBalance(player);
    }

    /**
     * The case where player has less than 21 points but has no other options
     * or "special" hands.
     */
    private void executeNormalHand() {
        int move = ui.getUserMove("Player " + playerNumOneIndex + " Hand " + handNumOneIndex
                + "\n" + MESSAGE_TWO_OPTIONS);

        boolean isNotValid = false;

        // do-while loop to ensure that execution can proceed with a valid input.
        do {
            switch (move) {

                case STAND:
                    logicUtil.stand();
                    break;

                case HIT:
                    logicUtil.hit(handNum, playerNum);
                    ui.displayGameState(state);
                    executeMove(processHand(handNum));
                    break;

                default:
                    isNotValid = true;
                    ui.displayResult(MESSAGE_INVALID_OPTION);
                    move = ui.getUserMove("Player " + playerNumOneIndex + " Hand " + handNumOneIndex
                            + "\n" + MESSAGE_TWO_OPTIONS);
            }
        } while (isNotValid);
    }

}
