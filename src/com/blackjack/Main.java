package com.blackjack;

import static com.blackjack.common.Messages.MESSAGE_INVALID_OPTION;
import static com.blackjack.common.Messages.MESSAGE_QUERY_NUM_PLAYERS;

import com.blackjack.logic.Logic;
import com.blackjack.model.Table;
import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.gamblers.Player;
import com.blackjack.ui.Ui;

/**
 * Entry point of the Blackjack game.
 * Initializes the application and starts the interaction with the user.
 */
public class Main {

    private final int MAXIMUM_PLAYERS = 4;

    private Dealer dealer;
    private Table table;
    private Ui ui;
    private Logic logic;

    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * Runs the program until the a decision to exit is made
     */
    private void run() {
        init();
        boolean wantNewGame = true;
        while (wantNewGame) {
            wantNewGame = executeGame();
        }
    }

    /**
     * Initializes the main components of the program that will process logic,
     * or will interact with the user, as well as adding a new player.
     */
    private void init() {
        dealer = new Dealer();
        table = new Table();
        table.addPlayers(dealer);
        ui = new Ui();

        addPlayers();
        logic = new Logic(table, ui);
    }

    /**
     * Starts the game of Blackjack.
     * @return Returns the decision on whether to play another game after this one.
     */
    private boolean executeGame() {
        logic.startGame();
        return logic.processGame();
    }

    private void addPlayers() {
        boolean isNotValid;
        do {
            int numPlayers = ui.getUserMove(MESSAGE_QUERY_NUM_PLAYERS);
            if (numPlayers < MAXIMUM_PLAYERS) {
                for (int i = 0; i < numPlayers; i++) {
                    table.addPlayers(new Player());
                }
                isNotValid = false;
            } else {
                ui.displayResult(MESSAGE_INVALID_OPTION);
                isNotValid = true;
            }
        } while (isNotValid);
    }
}
