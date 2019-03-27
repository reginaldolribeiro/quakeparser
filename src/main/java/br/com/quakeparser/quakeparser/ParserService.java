package br.com.quakeparser.quakeparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.quakeparser.quakeparser.Game;
import br.com.quakeparser.quakeparser.Player;

public class ParserService {

    private Game game;
    private int counter = 0;
    private int countKillWorld = 0;
    private Set<Player> players = new HashSet<>();
    private List<Game> games = new ArrayList<>();
    private Player player;

    private static final Pattern PATTERN_FOR_KILL = Pattern.compile("\\s*\\d{1,2}:\\d{2}\\s*(Kill:)\\w*");
    private static final Pattern PATTERN_INITIAL_POSITION_PLAYER_KILLED = Pattern
            .compile("Kill:\\s+[0-9]{1,4}\\s+[0-9]\\s+[0-9]{1,}:\\s+");
    private static final Pattern PATTERN_FINAL_POSITION_PLAYER_KILLED = Pattern.compile("\\s+\\bkilled");
    private static final Pattern PATTERN_FINAL_POSITION_DEAD_PLAYER = Pattern.compile("\\bby\\b");

    /**
     * 
     */
    public List<Game> getInfoGames() throws IOException {

        List<String> lines = loadFile("games.log");
        System.out.println("*** Quantidade de linhas do games.log: " + lines.size() + " ***");

        Matcher matcherForKill = PATTERN_FOR_KILL.matcher("");

        // Lendo linha por linha
        lines.forEach(line -> {

            // Criando o jogo
            if (line.contains("InitGame:")) {
                counter++;
                countKillWorld = 0;
                game = new Game("game_" + counter);
                games.add(game);
                players = new HashSet<>();
                game.setPlayers(players);
            }

            // Pegando os jogadores
            // \d{1,2}:\d{2}\s+ClientUserInfoChanged:\s+\d{1,4}\s+
            if (line.contains("ClientUserinfoChanged")) {

                String playerName = getPlayerName(line);

                game.getPlayersString().add(playerName);
                game.getKills().put(playerName, 0);

                player = new Player(playerName);
                players.add(player);

            }

            // sabe que aqui teve morte
            matcherForKill.reset(line);
            if (matcherForKill.find()) {

                game.addKill();

                Matcher matcherInitialPlayerKilled = PATTERN_INITIAL_POSITION_PLAYER_KILLED.matcher(line);
                Matcher matcherFinalPlayerKilled = PATTERN_FINAL_POSITION_PLAYER_KILLED.matcher(line);

                // Pegando o player que matou
                if (matcherInitialPlayerKilled.find() && matcherFinalPlayerKilled.find()) {
                    String killedPlayerName = getPlayerKilledName(line, matcherInitialPlayerKilled,
                            matcherFinalPlayerKilled);
                    Player playerKilled = null;

                    if (!killedPlayerName.equals("<world>")) {
                        playerKilled = players.stream().filter(p -> p.getPlayer().equalsIgnoreCase(killedPlayerName))
                                .findAny().get();
                        playerKilled.addKill();
                    }
                    Player deadPlayer = getDeadPlayer(line, matcherFinalPlayerKilled);
                    // System.out.println(killedPlayerName + " matou o " + deadPlayer.getPlayer());

                    if (killedPlayerName.equals("<world>")) {
                        game.addWorldsKill();
                        deadPlayer.subtractKill();
                    }

                }

            }

        });

        // games.forEach(System.out::println);
        System.out.println("Quantidade de games: " + games.size());

        return games;

    }

    // Pegando o player que morreu
    private Player getDeadPlayer(String line, Matcher matcherFinalPlayerKilled) {

        Matcher matcherFinalPositionDeadPlayer = PATTERN_FINAL_POSITION_DEAD_PLAYER.matcher(line);
        int initialPositionDeadPlayer = matcherFinalPlayerKilled.end();

        if (matcherFinalPositionDeadPlayer.find()) {
            String deadPlayerName = line.substring(initialPositionDeadPlayer, matcherFinalPositionDeadPlayer.start())
                    .trim();
            Player deadPlayer = players.stream().filter(p -> p.getPlayer().equalsIgnoreCase(deadPlayerName)).findAny()
                    .get();
            deadPlayer.addDeath();

            String causeOfDeath = getCauseOfDeath(line, matcherFinalPositionDeadPlayer);

            return deadPlayer;

        }
        return null;
    }

    // Pegando a causa da morte
    private String getCauseOfDeath(String line, Matcher matcherFinalPositionDeadPlayer) {
        int initialPositionCauseOfDeath = matcherFinalPositionDeadPlayer.end();
        return line.substring(initialPositionCauseOfDeath).trim();
        // System.out.println(playerKilled.getPlayer() + " matou " + deadPlayer + " com
        // " + causaDaMorte);
    }

    private String getPlayerKilledName(String line, Matcher matcherInitialPlayerKilled,
            Matcher matcherFinalPlayerKilled) {
        int initialOfPlayer = matcherInitialPlayerKilled.end();
        int finalOfPlayer = matcherFinalPlayerKilled.start();
        return line.substring(initialOfPlayer, finalOfPlayer).trim();
    }

    private String getPlayerName(String line) {
        int initialPosition = line.indexOf("\\");
        int finalPosition = line.indexOf("\\t");
        return line.substring((initialPosition + 1), finalPosition).trim();
    }

    private List<String> loadFile(String path) throws IOException {
        Path pathToFile = Paths.get(path);
        List<String> lines = Files.readAllLines(pathToFile);
        return lines;
    }

}