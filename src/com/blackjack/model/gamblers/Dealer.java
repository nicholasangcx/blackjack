package com.blackjack.model.gamblers;

import java.util.ArrayList;

import com.blackjack.model.Hand;

/**
 * Represents the dealer in the game of Blackjack
 */
public class Dealer extends Gambler {

    /** Only has a list of cards as dealer cannot have multiple hands.*/
    private ArrayList<Hand> hands;

    public Dealer() {
        super();
    }

}
