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

        List<String> lines = loadFile("games.1.log");
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
            if (line.contains("ClientUserinfoChanged")) {

                String playerName = getPlayer(line);

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

            // Pegando a causa da morte
            int inicioCausaDaMorte = matcherFinalPositionDeadPlayer.end();
            String causaDaMorte = line.substring(inicioCausaDaMorte).trim();
            // System.out.println(playerKilled.getPlayer() + " matou " + deadPlayer + " com
            // " + causaDaMorte);

            return deadPlayer;

        }
        return null;
    }

    private String getPlayerKilledName(String line, Matcher matcherInitialPlayerKilled,
            Matcher matcherFinalPlayerKilled) {
        int startOfPlayer = matcherInitialPlayerKilled.end();
        int endOfPlayer = matcherFinalPlayerKilled.start();
        String playerName = line.substring(startOfPlayer, endOfPlayer).trim();
        return playerName;
    }

    private String getPlayer(String line) {
        int start = line.indexOf("\\");
        int end = line.indexOf("\\t");
        String nameOfPlayer = line.substring((start + 1), end);
        return nameOfPlayer;
    }

    private List<String> loadFile(String path) throws IOException {
        Path pathToFile = Paths.get(path);
        List<String> lines = Files.readAllLines(pathToFile);
        return lines;
    }

}