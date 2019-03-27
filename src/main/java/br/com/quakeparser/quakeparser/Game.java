package br.com.quakeparser.quakeparser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {

    private String name;
    private int totalKills;
    private int totalKillsWorld;
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

    /**
     * @return the totalKillsWorld
     */
    public int getTotalKillsWorld() {
        return totalKillsWorld;
    }

    /**
     * @param totalKillsWorld the totalKillsWorld to set
     */
    public void setTotalKillsWorld(int totalKillsWorld) {
        this.totalKillsWorld = totalKillsWorld;
    }

    public void addWorldsKill() {
        this.totalKillsWorld++;
    }

    @Override
    public String toString() {
        return "Game [name=" + name + ", totalKills=" + totalKills + ", players=" + players + ", playersString="
                + playersString + ", kills=" + kills + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kills == null) ? 0 : kills.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + ((playersString == null) ? 0 : playersString.hashCode());
        result = prime * result + totalKills;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Game other = (Game) obj;
        if (kills == null) {
            if (other.kills != null)
                return false;
        } else if (!kills.equals(other.kills))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (playersString == null) {
            if (other.playersString != null)
                return false;
        } else if (!playersString.equals(other.playersString))
            return false;
        if (totalKills != other.totalKills)
            return false;
        return true;
    }

}