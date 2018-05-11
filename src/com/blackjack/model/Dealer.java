package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.card.Card;

/**
 * Represents the dealer in the game of Blackjack
 */
public class Dealer {

    private static final String CARD_HIDDEN = "X";

    /** Only has a list of cards as dealer cannot have multiple hands.*/
    private ArrayList<Card> cards;

    public Dealer() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * This method hides the second card of the dealer by replacing it with an X instead.
     * @return The String format of the dealer's first card and the placeholder.
     */
    public String getHiddenCardsAsString() {
        String result = "";
        result += cards.get(0).getFace() + " " + CARD_HIDDEN;
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
