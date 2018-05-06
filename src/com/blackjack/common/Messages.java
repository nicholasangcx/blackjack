package com.blackjack.common;

/**
 * Container for the messages being displayed back to the user
 */
public class Messages {

    private static final String CHOOSE_MOVE = "Choose your move (Enter the corresponding number):\n";

    public static final String MESSAGE_INVALID_OPTION = "Invalid Option.";
    public static final String MESSAGE_BLACKJACK = "BLACKJACK! Payout 1.5 times.";
    public static final String MESSAGE_WON = "You won! Payout 1.0 times your bid(s).";
    public static final String MESSAGE_LOST = "You lost! Lost your current bid(s).";
    public static final String MESSAGE_TIE = "TIE! No nett change in your balance.";
    public static final String MESSAGE_BUST = "BUST! Lose your current bid.";
    public static final String MESSAGE_SURRENDER = "You surrendered! Lost half of your current bid";
    public static final String MESSAGE_FIVE_OPTIONS = CHOOSE_MOVE + "(1)Stand (2)Hit (3)Double (4)Surrender (5)Split\n";
    public static final String MESSAGE_FOUR_OPTIONS = CHOOSE_MOVE + "(1)Stand (2)Hit (3)Double (4)Surrender\n";
    public static final String MESSAGE_TWO_OPTIONS = CHOOSE_MOVE + "(1)Stand (2)Hit\n";
    public static final String MESSAGE_QUERY_NEW_GAME = "Start a new game? (1 for yes, 2 for no): ";
    public static final String MESSAGE_GOODBYE = "Thank you for playing Blackjack! Goodbye!!";
}
