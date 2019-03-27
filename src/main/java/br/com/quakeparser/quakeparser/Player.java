package br.com.quakeparser.quakeparser;

public class Player {

    private String player;
    private int totalKills;
    private int totalDeaths;

    public Player(String player) {
        this.player = player;
        this.totalKills = 0;
        this.totalDeaths = 0;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public void addDeath() {
        this.totalDeaths++;
    }

    public void addKill() {
        this.totalKills++;
    }

    public void subtractKill() {
        this.totalKills--;
    }

    @Override
    public String toString() {
        return "Player [player=" + player + ", totalKills=" + totalKills + ", totalDeaths=" + totalDeaths + "]";
    }

}