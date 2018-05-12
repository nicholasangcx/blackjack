package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.gamblers.Gambler;
import com.blackjack.model.gamblers.Player;

public class Table {

    private ArrayList<Gambler> gamblers;

    public Table() {
        gamblers = new ArrayList<Gambler>();
        gamblers.add(new Dealer());
    }

    public void addPlayers(Player player) {
        gamblers.add(player);
    }

    public ArrayList<Gambler> getGamblers() {
        return gamblers;
    }

    public void clearHands() {
        for (Gambler gambler : gamblers) {
            gambler.clearHands();
        }
    }
}
