package br.com.quakeparser.model;

public class Player {

    private String player;
    private int score;
    private int totalKills;
    private int totalDeaths;
    private int totalDeathsByWorld;
    private int totalSuicides;

    public Player(String player) {
        this.player = player;
        this.score = 0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.totalDeathsByWorld = 0;
        this.totalSuicides = 0;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalSuicides() {
        return totalSuicides;
    }

    public int getTotalDeathsByWorld() {
        return totalDeathsByWorld;
    }

    public void addScore() {
        this.score++;
    }

    public void subtractScore() {
        this.score--;
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

    public void addSuicide() {
        this.totalSuicides++;
    }

    public void addDeathByWorld() {
        this.totalDeathsByWorld++;
    }

    @Override
    public String toString() {
        return "Player [player=" + player + ", totalKills=" + totalKills + ", totalDeaths=" + totalDeaths + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((player == null) ? 0 : player.hashCode());
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
        Player other = (Player) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}