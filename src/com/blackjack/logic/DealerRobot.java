package com.blackjack.logic;

import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.Hand;

/**
 * Contains the logic of the dealer.
 */
public class DealerRobot {

    private Deal deal;
    private Dealer dealer;

    public DealerRobot(Deal deal, Dealer dealer) {
        this.deal = deal;
        this.dealer = dealer;
    }

    /**
     * The dealer will continue taking cards till he hits the minimum points required as a dealer.
     */
    public void execute() {
        Hand hand = dealer.getHands().get(0);
        while (ProcessHand.isLessThanDealerMin(hand)) {
            deal.dealCard(dealer);
            hand = dealer.getHands().get(0);
        }
    }

}
