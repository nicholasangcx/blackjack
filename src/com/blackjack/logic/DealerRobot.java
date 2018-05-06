package com.blackjack.logic;

import com.blackjack.model.Dealer;
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
        Hand hand = new Hand(dealer.getCards());
        while (hand.isLessThanDealerMin()) {
            deal.dealCard(dealer);
            hand = new Hand(dealer.getCards());
        }
    }

}
