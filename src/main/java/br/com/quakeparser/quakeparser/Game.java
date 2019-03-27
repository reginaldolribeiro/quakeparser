package br.com.quakeparser.quakeparser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {

    private String name;
    private int totalKills;
    private Set<Player> players = new HashSet<>();
    private Set<String> playersString = new HashSet<>();
    private Map<String, Integer> kills = new HashMap<String, Integer>();

    public Game(String name) {
        this.name = name;
        this.totalKills = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<String> getPlayersString() {
        return playersString;
    }

    public void setPlayersString(Set<String> playersString) {
        this.playersString = playersString;
    }

    public Map<String, Integer> getKills() {
        return kills;
    }

    public void setKills(Map<String, Integer> kills) {
        this.kills = kills;
    }

    public void addKill() {
        this.totalKills = this.totalKills + 1;
    }

    @Override
    public String toString() {
        return "Game [name=" + name + ", totalKills=" + totalKills + ", players=" + players + ", playersString="
                + playersString + ", kills=" + kills + "]";
    }

}