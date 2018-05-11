package com.blackjack.logic;

import com.blackjack.model.Hand;
import com.blackjack.model.card.Card;
import com.blackjack.model.card.CardFace;

public class ProcessHand {

    private static final int STARTING_HAND_SIZE = 2;
    private static final int MAXIMUM_POINTS = 21;
    private static final int DEALER_MIN = 17;
    private static final int DIFF_ACE_VALUE = 10;

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

        Card firstCard = hand.getCards().get(0);
        Card secondCard = hand.getCards().get(1);

        return (firstCard.getFace().equals(CardFace.ACE) && GameLogic.cardValue(secondCard) == 10)
                || (secondCard.getFace().equals(CardFace.ACE) && GameLogic.cardValue(firstCard) == 10);
    }

    /**
     * Called only on starting hands.
     * @return true if both cards have the same value
     */
    public static boolean isEqualRank(Hand hand) {
        assert(isStartingHand(hand));

        Card firstCard = hand.getCards().get(0);
        Card secondCard = hand.getCards().get(1);

        return GameLogic.cardValue(firstCard) == GameLogic.cardValue(secondCard);
    }

    /**
     * @return true if the sum of all the card values is equal to or less than 21.
     */
    public static boolean isLessThan21(Hand hand) {
        int totalValue = calculateValue(hand);
        return totalValue < MAXIMUM_POINTS;
    }

    public static boolean isExactly21(Hand hand) {
        int totalValue = calculateValue(hand);
        return totalValue == MAXIMUM_POINTS;
    }

    public static boolean isMoreThan21(Hand hand) {
        int totalValue = calculateValue(hand);
        return totalValue > MAXIMUM_POINTS;
    }

    /**
     * @return true if the sum of all the card values is less than the minimum requirement for the dealer.
     */
    public static boolean isLessThanDealerMin(Hand hand) {
        int totalValue = calculateValue(hand);
        return totalValue < DEALER_MIN;
    }

    public static int calculateValue(Hand hand) {
        int totalValue = 0;
        boolean containsAce = false;
        for (Card card : hand.getCards()) {
            totalValue += GameLogic.cardValue(card);
            if (card.getFace().equals(CardFace.ACE)) {
                containsAce = true;
            }
        }
        // account for the changing ACE value
        if ((totalValue > MAXIMUM_POINTS) && containsAce) {
            totalValue -= DIFF_ACE_VALUE;
        }
        return totalValue;
    }

}
