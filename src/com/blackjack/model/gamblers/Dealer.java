package com.blackjack.model.gamblers;

import java.util.ArrayList;

import com.blackjack.model.Hand;
import com.blackjack.model.card.Card;

/**
 * Represents the dealer in the game of Blackjack
 */
public class Dealer extends Gamblers {

    private static final String CARD_HIDDEN = "X";

    /** Only has a list of cards as dealer cannot have multiple hands.*/
    private ArrayList<Hand> hands;

    public Dealer() {
        super();
    }

    public void addCard(Card card) {
        addCard(card, 0);
    }

    /**
     * This method hides the second card of the dealer by replacing it with an X instead.
     * @return The String format of the dealer's first card and the placeholder.
     */
    public String getHiddenCardsAsString() {
        String result = "";
        result += hands.get(0).getCards().get(0).getFace() + " " + CARD_HIDDEN;
        return result;
    }

    /**
     * This method concatenate all the cards of the dealer and represents them in a String format.
     * @return The String format formed.
     */
    public String getCardsAsString() {
        String result = "";
        for (Card card: cards) {
            result += card.getFace() + " ";
        }
        return result;
    }
}
