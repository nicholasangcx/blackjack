package com.blackjack.model;

import java.util.ArrayList;

import com.blackjack.model.gamblers.Gambler;
import com.blackjack.model.gamblers.Player;

/**
 * Represents a Blackjack table
 */
public class Table {

    private ArrayList<Gambler> gamblers;

    public Table() {
        gamblers = new ArrayList<Gambler>();
    }

    /**
     * Adds a player to the table.
     * @param gambler the player to add.
     */
    public void addPlayers(Gambler gambler) {
        gamblers.add(gambler);
    }

    public ArrayList<Gambler> getGamblers() {
        return gamblers;
    }

    /**
     * Get only the players at the table, excluding the dealer.
     * @return an ArrayList of players.
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i = 1; i < gamblers.size(); i++) {
            players.add((Player)gamblers.get(i));
        }
        return players;
    }

    /**
     * Places the starting bid for every player at the table
     */
    public void startingBids() {
        ArrayList<Player> players = getPlayers();
        for (Player player : players) {
            player.startingBid();
        }
    }

    /**
     * Clears the hands of every gambler at the table.
     * To start a new game.
     */
    public void clearHands() {
        for (Gambler gambler : gamblers) {
            gambler.clearHands();
        }
    }
}
