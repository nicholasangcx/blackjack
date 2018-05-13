package com.blackjack.model.card;

public enum CardFace {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    private final String face;

    CardFace(String face) {
        this.face = face;
    }

    public String face() {
        return this.face;
    }

}
