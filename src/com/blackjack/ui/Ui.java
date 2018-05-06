package com.blackjack.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.blackjack.model.Hand;
import com.blackjack.model.Player;

/**
 * Handles what is displayed and the interaction between the user and the application
 */
public class Ui {

    private final Scanner in;
    private final PrintStream out;

    /** Delimiter to separate bids of different hands */
    private static final String DELIMITER = " , ";

    public Ui() {
        this(System.in, System.out);
    }

    private Ui(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    /** Just to print messages to the user */
    private void showToUser(String message) {
        out.println(message);
    }

    /**
     * Displays the state of the game to the player,
     * showing the cards and hands he and the dealer has
     * as well as his current bids and balance.
     * @param state contains all the details to be printed to screen
     */
    public void displayGameState(GameState state) {
        showToUser(state.toString());
    }

    /**
     * Displays the outcome of the games (i.e. win, lost, draw, blackjack etc)
     * @param result contains the message to be printed.
     */
    public void displayResult(String result) {
        out.println(result);
    }

    /**
     * This method displays just the current bids and balance value of the player
     * without showing the entire game state, which involves displaying the respective cards too.
     * @param player is the player whose stats we want to print.
     */
    public void displayBalance(Player player) {
        String message = "Current Bid: ";
        for (Hand hand : player.getHands()) {
            message += hand.getCurrentBid() + DELIMITER;
        }
        message = message.substring(0, message.length() - 2);
        message += "\nBalance Left: " + player.getBalance();
        out.println(message);
    }

    /** Message to demarcate the start of a new game so the UI looks less cluttered */
    public void displayNewGame() {
        String message = "\n\nNEW GAME!!";
        out.println(message);
    }

    /**
     * This method displays a prompt message to the user and waits for their input
     * @param prompt is the message to be displayed. The message chosen is decided
     * based on the type of hand the player has
     * @return The next integer inputted by the user, which corresponds to the option they chose, is returned.
     */
    public int getUserMove(String prompt) {
        out.print(prompt);
        return in.nextInt();
    }

}
