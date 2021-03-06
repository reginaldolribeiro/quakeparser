package br.com.quakeparser.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.ResourceSupport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Game extends ResourceSupport {

    private String name;
    private int totalKills;
    // @JsonIgnore
    // private Set<Player> players = new HashSet<>();
    private Set<String> players = new HashSet<>();
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

    /*
     * public Set<Player> getPlayers() { return players; }
     * 
     * public void setPlayers(Set<Player> players) { this.players = players; }
     */

    public Set<String> getPlayers() {
        return players;
    }

    public void addPlayer(String player) {
        this.getPlayers().add(player);
    }

    public Map<String, Integer> getKills() {
        return kills;
    }

    public void addTotalKill() {
        this.totalKills = this.totalKills + 1;
    }

    public void addKill(String player) {
        this.kills.put(player, getKills().get(player).intValue() + 1);
    }

    public void subtractKill(String player) {
        this.kills.put(player, getKills().get(player).intValue() - 1);
    }

    @Override
    public String toString() {
        return "Game [name=" + name + ", totalKills=" + totalKills + ", players=" + players + ", kills=" + kills + "]";
    }

    /*
     * @Override public String toString() { return "Game [name=" + name +
     * ", totalKills=" + totalKills + ", players=" + players + ", playersString=" +
     * playersString + ", kills=" + kills + "]"; }
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kills == null) ? 0 : kills.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        // result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
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
        /*
         * if (players == null) { if (other.players != null) return false; } else if
         * (!players.equals(other.players)) return false;
         */
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (totalKills != other.totalKills)
            return false;
        return true;
    }

}