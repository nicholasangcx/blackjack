package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.gamblers.Dealer;
import com.blackjack.model.gamblers.Gamblers;
import com.blackjack.model.gamblers.Player;

public class Table {

    private ArrayList<Gamblers> gamblers;

    public Table() {
        gamblers = new ArrayList<Gamblers>();
        gamblers.add(new Dealer());
    }

    public void addPlayers(Player player) {
        gamblers.add(player);
    }
}
