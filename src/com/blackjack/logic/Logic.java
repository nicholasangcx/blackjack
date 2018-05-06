package com.blackjack.logic;

import static com.blackjack.common.Messages.MESSAGE_BLACKJACK;
import static com.blackjack.common.Messages.MESSAGE_BUST;
import static com.blackjack.common.Messages.MESSAGE_FIVE_OPTIONS;
import static com.blackjack.common.Messages.MESSAGE_FOUR_OPTIONS;
import static com.blackjack.common.Messages.MESSAGE_GOODBYE;
import static com.blackjack.common.Messages.MESSAGE_INVALID_OPTION;
import static com.blackjack.common.Messages.MESSAGE_QUERY_NEW_GAME;
import static com.blackjack.common.Messages.MESSAGE_SURRENDER;
import static com.blackjack.common.Messages.MESSAGE_TIE;
import static com.blackjack.common.Messages.MESSAGE_TWO_OPTIONS;
import static com.blackjack.logic.util.LogicUtil.DOUBLE;
import static com.blackjack.logic.util.LogicUtil.HIT;
import static com.blackjack.logic.util.LogicUtil.SPLIT;
import static com.blackjack.logic.util.LogicUtil.STAND;
import static com.blackjack.logic.util.LogicUtil.SURRENDER;
import static com.blackjack.model.Hand.DEFAULT_BID;

import com.blackjack.logic.util.LogicUtil;
import com.blackjack.ui.GameState;
import com.blackjack.ui.Ui;
import com.blackjack.model.Dealer;
import com.blackjack.model.Hand;
import com.blackjack.model.Player;

/**
 * Contains the main logic of the game
 * Maintains the gamestate, dealer and player state,
 * as well as make calls to the UI.
 */
public class Logic {

    /** Static variables that are used to map the type of hand of a player */
    private static final int BLACKJACK = 1;
    private static final int SPLITTABLE = 2;
    private static final int NOT_SPLITTABLE = 3;
    private static final int LESSTHAN_OR_EQUAL_21 = 4;
    private static final int BUST = 5;

    /** Static variables that are used to map user input */
    private static final int YES = 1;
    private static final int NO = 2;

    private Deal deal;
    private GameState state;
    private Dealer dealer;
    private Player player;
    private Ui ui;
    private LogicUtil logicUtil;

    private int handNum;
    private int handNumOneIndex;
    private int currentBid;
    private Hand hand;
    
    public Logic(Player player, Ui ui) {
        this.player = player;
        this.ui = ui;
    }

    /**
     * Re-initializes all the required classes and
     * Starts the game by dealing 2 random cards to each player
     */
    public void startGame() {
        // Re-initializes all the required classes
        state = new GameState();
        deal = new Deal(state);
        dealer = new Dealer();
        player.clearHands();

        deal.initialHand(dealer, player);
        ui.displayNewGame();
        ui.displayGameState(state);
    }

    /**
     * Execution of the rest of the game, after the initial dealing of cards
     * @return true if user wants to continue playing another game.
     */
    public boolean finishGame() {
        logicUtil = new LogicUtil(deal, dealer, player, state);
        handNum = 0;
        handNumOneIndex = handNum + 1;

        /** Execution of the game, for each hand */
        while (handNum <  player.getHands().size()) {
            hand = player.getHands().get(handNum);
            currentBid = hand.getCurrentBid();
            int typeOfHand = processHand(hand);
            executeMove(typeOfHand);
            handNum++;
            handNumOneIndex = handNum + 1;
        }
        String result = logicUtil.executeRobot();
        /** If there is additional information about the outcome of the game not already provided */
        if (logicUtil.hasNewInfo) {
            ui.displayGameState(state.revealDealer(dealer));
            ui.displayResult(result);
        }

        /** Processing whether to start a new game */
        if (player.getBalance() < DEFAULT_BID) {
            ui.displayResult(MESSAGE_GOODBYE);
            return false;
        }

        boolean isValidInput;
        do {
            int decision = ui.getUserMove(MESSAGE_QUERY_NEW_GAME);
            if (decision == YES) {
                return true;
            } else if (decision == NO) {
                ui.displayResult(MESSAGE_GOODBYE);
                return false;
            } else {
                ui.displayResult(MESSAGE_INVALID_OPTION);
                isValidInput = false;
            }
        } while (!isValidInput);

        return false;
    }

    /**
     * Runs the relevant logic for the type of hand the player has received
     * @param typeOfHand represents the various hands available
     */
    private void executeMove(int typeOfHand) {

        switch (typeOfHand) {

            case BLACKJACK:
                executeBlackjackHand();
                break;

            case SPLITTABLE:
                executeSplittableHand();
                break;

            case NOT_SPLITTABLE:
                executeNotSplittableHand();
                break;

            case BUST:
                executeBustHand();
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
    private int processHand(int handNum) {
        hand = player.getHands().get(handNum);
        return processHand(hand);
    }

    /**
     * Processes the hand the player has to decide what options are available to him
     * @return an integer that represents the type of hand this is.
     */
    private int processHand(Hand hand) {
        if (hand.isStartingHand()) {
            if (hand.isBlackjack()) {
                return BLACKJACK;
            } else if (hand.isEqualRank()) {
                return SPLITTABLE;
            }
            else {
                return NOT_SPLITTABLE;
            }
        } else if (hand.is21OrLess()) { // not starting hand
            return LESSTHAN_OR_EQUAL_21;
        }
        else { // more than 21
            return BUST;
        }
    }

    /**
     * The case where player has a Blackjack.
     */
    private void executeBlackjackHand() {
        Hand dealerHand = new Hand(dealer.getCards());
        if (!dealerHand.isBlackjack()) {
            player.increaseBalance(currentBid * 25/10);
            player.getHands().get(handNum).clearBid();
            ui.displayResult(MESSAGE_BLACKJACK);
            ui.displayBalance(player);
        }
        else {
            player.increaseBalance(currentBid);
            player.getHands().get(handNum).clearBid();
            state.updateState(player);
            ui.displayGameState(state.revealDealer(dealer));
            ui.displayResult(MESSAGE_TIE);
        }
    }

    /**
     * The case where player has 2 cards of the same value in his starting hand.
     * Assume you can still surrender on your first 2 cards of each split hand.
     */
    private void executeSplittableHand() {
        int move = ui.getUserMove("Hand No.: " + handNumOneIndex + "\n" + MESSAGE_FIVE_OPTIONS);

        boolean isNotValid = false;
        String result;

        // do-while loop to ensure that execution can proceed with a valid input.
        do {
            switch (move) {

                case STAND:
                    logicUtil.stand();
                    break;

                case HIT:
                    logicUtil.hit(handNum);
                    ui.displayGameState(state);
                    executeMove(processHand(handNum));
                    break;

                case DOUBLE:
                    logicUtil.doubleDown(hand, handNum);
                    if (!hand.is21OrLess()) {
                        executeBustHand();
                    }
                    break;

                case SURRENDER:
                    logicUtil.surrender(hand);
                    ui.displayResult(MESSAGE_SURRENDER);
                    ui.displayBalance(player);
                    break;

                case SPLIT:
                    logicUtil.split(handNum);
                    ui.displayGameState(state);
                    handNum--; // to reprocess the current hand again
                    break;

                default:
                    isNotValid = true;
                    ui.displayResult(MESSAGE_INVALID_OPTION);
                    move = ui.getUserMove("Hand No.: " + handNumOneIndex + "\n" + MESSAGE_FIVE_OPTIONS);
            }
        } while (isNotValid);
    }

    /**
     * The case where player does not have 2 cards of the same value in his starting hand.
     */
    private void executeNotSplittableHand() {
        int move = ui.getUserMove("Hand No.: " + handNumOneIndex + "\n" + MESSAGE_FOUR_OPTIONS);

        boolean isNotValid = false;
        String result;

        // do-while loop to ensure that execution can proceed with a valid input.
        do {
            switch (move) {

                case STAND:
                    logicUtil.stand();
                    break;

                case HIT:
                    logicUtil.hit(handNum);
                    ui.displayGameState(state);
                    executeMove(processHand(handNum));
                    break;

                case DOUBLE:
                    logicUtil.doubleDown(hand, handNum);
                    ui.displayGameState(state);
                    if (!hand.is21OrLess()) {
                        executeBustHand();
                    }
                    break;

                case SURRENDER:
                    logicUtil.surrender(hand);
                    ui.displayResult(MESSAGE_SURRENDER);
                    ui.displayBalance(player);
                    break;

                default:
                    isNotValid = true;
                    ui.displayResult(MESSAGE_INVALID_OPTION);
                    move = ui.getUserMove("Hand No.: " + handNumOneIndex + "\n" + MESSAGE_FOUR_OPTIONS);
            }
        } while (isNotValid);
    }

    /**
     * The case where the player's cards add up to a value greater than 21.
     */
    private void executeBustHand() {
        player.decreaseBalance(currentBid);
        hand.clearBid();
        ui.displayResult(MESSAGE_BUST);
        ui.displayBalance(player);
    }

    /**
     * The case where player has less than 21 points but has no other options
     * or "special" hands.
     */
    private void executeNormalHand() {
        int move = ui.getUserMove("Hand No.: " + handNumOneIndex + "\n" + MESSAGE_TWO_OPTIONS);

        boolean isNotValid = false;

        // do-while loop to ensure that execution can proceed with a valid input.
        do {
            switch (move) {

                case STAND:
                    logicUtil.stand();
                    break;

                case HIT:
                    logicUtil.hit(handNum);
                    ui.displayGameState(state);
                    executeMove(processHand(handNum));
                    break;

                default:
                    isNotValid = true;
                    ui.displayResult(MESSAGE_INVALID_OPTION);
                    move = ui.getUserMove("Hand No.: " + handNumOneIndex + "\n" + MESSAGE_TWO_OPTIONS);
            }
        } while (isNotValid);
    }

}
