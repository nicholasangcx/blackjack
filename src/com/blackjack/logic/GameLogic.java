package com.blackjack.logic;

import com.blackjack.model.card.Card;

public class GameLogic {

    public static int cardValue(Card card) {
        switch (card.getFace()) {

            case TWO:
                return 2;

            case THREE:
                return 3;

            case FOUR:
                return 4;

            case FIVE:
                return 5;

            case SIX:
                return 6;

            case SEVEN:
                return 7;

            case EIGHT:
                return 8;

            case NINE:
                return 9;

            case TEN:
            case JACK:
            case QUEEN:
            case KING:
                return 10;

            case ACE:
                return 11;

            default:
                return -100;
        }
    }
}
