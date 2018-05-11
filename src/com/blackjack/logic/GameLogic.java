package com.blackjack.logic;

import com.blackjack.model.Hand;
import com.blackjack.model.card.Card;

public class GameLogic {

    private static final int STARTING_HAND_SIZE = 2;
    private static final int MAXIMUM_POINTS = 21;
    private static final int DEALER_MIN = 17;

    /**
     * @return true if this hand is a starting hand
     */
    public static boolean isStartingHand(Hand hand) {
        return hand.getSize() == STARTING_HAND_SIZE;
    }

    /**
     * Called only on starting hands.
     * @return true if this is a Blackjack hand
     */
    public static boolean isBlackjack(Hand hand) {
        assert(isStartingHand(hand));
        return (hand.getCards().get(0).getFace().equals("A") && hand.getCards().get(1).getValue() == 10)
                || (hand.getCards().get(0).getValue() == 10 && hand.getCards().get(1).getFace().equals("A"));
    }

    /**
     * Called only on starting hands.
     * @return true if both cards have the same value
     */
    public static boolean isEqualRank(Hand hand) {
        assert(isStartingHand(hand));
        return hand.getCards().get(0).getValue() == hand.getCards().get(1).getValue();
    }

    /**
     * @return true if the sum of all the card values is equal to or less than 21.
     */
    public static boolean isLessThan21(Hand hand) {
        int totalValue = 0;
        boolean containsAce = false;
        for (Card card : hand.getCards()) {
            totalValue += card.getValue();
            if (card.getFace().equals("A")) {
                containsAce = true;
            }
        }
        // account for the changing ACE value
        if ((totalValue > MAXIMUM_POINTS) && containsAce) {
            totalValue--;
        }
        return totalValue <= MAXIMUM_POINTS;
    }

    public static boolean isExactly21(Hand hand) {

    }

    /**
     * @return true if the sum of all the card values is less than the minimum requirement for the dealer.
     */
    public static boolean isLessThanDealerMin(Hand hand) {
        int totalValue = 0;
        for (Card card : hand.getCards()) {
            totalValue += card.getValue();
        }
        return totalValue < DEALER_MIN;
    }
}
