package com.blackjack;

import com.blackjack.logic.Logic;
import com.blackjack.model.Table;
import com.blackjack.model.gamblers.Player;
import com.blackjack.ui.Ui;

/**
 * Entry point of the Blackjack game.
 * Initializes the application and starts the interaction with the user.
 */
public class Main {

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
        table = new Table();
        ui = new Ui();
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

}
