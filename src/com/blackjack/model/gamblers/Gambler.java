package com.blackjack.model.gamblers;

import java.util.ArrayList;

import com.blackjack.model.Hand;
import com.blackjack.model.card.Card;

/**
 * Abstract class that implements some common methods that everyone seated
 * at a Blackjack table would require.
 */
public abstract class Gambler {

    private ArrayList<Hand> hands;

    protected Gambler() {
        hands = new ArrayList<Hand>();
    }

    /**
     * This method adds a card to the specified hand of the player (to deal with split hand situations).
     * @param card the card that is dealt.
     * @param currentHand the hand that we are adding this card to.
     */
    public void addCard(Card card, int currentHand) {
        // Dealing the cards out at the start
        if (currentHand == hands.size()) {
            ArrayList<Card> cards = new ArrayList<Card>();
            cards.add(card);
            hands.add(new Hand(cards));
        } else {
            hands.get(currentHand).add(card);
        }
    }

    public ArrayList<Hand> getHands() {
        return hands;
    }

    /**
     * Resets the hand of the player (for a new game).
     */
    public void clearHands() {
        hands = new ArrayList<Hand>();
    }

}
