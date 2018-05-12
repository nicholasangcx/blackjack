package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.gamblers.Gambler;
import com.blackjack.model.gamblers.Player;

public class Table {

    private ArrayList<Gambler> gamblers;

    public Table() {
        gamblers = new ArrayList<Gambler>();
    }

    public void addPlayers(Gambler gambler) {
        gamblers.add(gambler);
    }

    public ArrayList<Gambler> getGamblers() {
        return gamblers;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i = 1; i < gamblers.size(); i++) {
            players.add((Player)gamblers.get(i));
        }
        return players;
    }

    public void clearHands() {
        for (Gambler gambler : gamblers) {
            gambler.clearHands();
        }
    }
}
