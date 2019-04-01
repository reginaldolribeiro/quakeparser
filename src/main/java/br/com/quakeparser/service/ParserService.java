package br.com.quakeparser.service;

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
import br.com.quakeparser.model.Game;
import br.com.quakeparser.model.Player;

public class ParserService {

    private Game game;
    private int counterGame = 0;
    private int countKillWorld = 0;
    private Set<Player> players;
    // private List<Game> games = new ArrayList<>();
    private Player player;

    private static final Pattern PATTERN_FOR_KILL = Pattern.compile("\\s*\\d{1,2}:\\d{2}\\s*(Kill:)\\w*");
    private static final Pattern PATTERN_INITIAL_POSITION_KILLER_PLAYER = Pattern
            .compile("Kill:\\s+[0-9]{1,4}\\s+[0-9]\\s+[0-9]{1,}:\\s+");
    private static final Pattern PATTERN_FINAL_POSITION_KILLER_PLAYER = Pattern.compile("\\s+\\bkilled");
    private static final Pattern PATTERN_FINAL_POSITION_DEAD_PLAYER = Pattern.compile("\\bby\\b");

    /**
     * 
     */
    public List<Game> getInfoGames() throws IOException {

        List<Game> games = new ArrayList<>();

        List<String> lines = loadFile("games.1.log");
        System.out.println("*** Quantidade de linhas do games.log: " + lines.size() + " ***");

        Matcher matcherForKill = PATTERN_FOR_KILL.matcher("");

        lines.forEach(line -> {

            game = createGame(games, line);

            game = addPlayers(line);

            matcherForKill.reset(line);
            if (matcherForKill.find()) {

                game.addKill();

                Matcher matcherInitialKillerPlayer = PATTERN_INITIAL_POSITION_KILLER_PLAYER.matcher(line);
                Matcher matcherFinalKillerPlayer = PATTERN_FINAL_POSITION_KILLER_PLAYER.matcher(line);

                // score = totalKills - (totalDeathsByWorld+totalSuicides)

                if (matcherInitialKillerPlayer.find() && matcherFinalKillerPlayer.find()) {

                    String killerPlayerName = getKillerPlayerName(line, matcherInitialKillerPlayer,
                            matcherFinalKillerPlayer);
                    // Player killerPlayer = null;

                    Player deadPlayer = getDeadPlayer(line, matcherFinalKillerPlayer);

                    if (killerPlayerName.equals("<world>")) {

                        // game.addWorldsKill();
                        // deadPlayer.subtractKill();
                        deadPlayer.subtractScore();
                        game.getKills().put(deadPlayer.getPlayer(),
                                game.getKills().get(deadPlayer.getPlayer()).intValue() - 1);
                        deadPlayer.addDeathByWorld();

                    } else {

                        Player killerPlayer = players.stream()
                                .filter(p -> p.getPlayer().equalsIgnoreCase(killerPlayerName)).findAny().get();

                        killerPlayer.addKill();

                        if (killerPlayer == deadPlayer) {
                            killerPlayer.addSuicide();
                            game.getKills().put(killerPlayer.getPlayer(),
                                    game.getKills().get(killerPlayer.getPlayer()).intValue() - 1);
                        } else {
                            // killerPlayer.addKill();
                            killerPlayer.addScore();
                            game.getKills().put(killerPlayer.getPlayer(),
                                    game.getKills().get(killerPlayer.getPlayer()).intValue() + 1);
                        }

                    }

                }

            }

        });

        // games.forEach(System.out::println);
        System.out.println("Quantidade de games: " + games.size());

        return games;

    }

    // Pegando os jogadores
    private Game addPlayers(String line) {
        // \d{1,2}:\d{2}\s+ClientUserInfoChanged:\s+\d{1,4}\s+
        if (line.contains("ClientUserinfoChanged")) {

            String playerName = getPlayerName(line);

            game.getPlayersString().add(playerName);
            game.getKills().put(playerName, 0);

            player = new Player(playerName);
            players.add(player);

        }
        return game;
    }

    // Criando o jogo
    private Game createGame(List<Game> games, String line) {
        if (line.contains("InitGame:")) {
            counterGame++;
            countKillWorld = 0;
            game = new Game("game_" + counterGame);
            games.add(game);
            players = new HashSet<>();
            game.setPlayers(players);
            return game;
        }
        return game;
    }

    // Pegando o player que morreu
    private Player getDeadPlayer(String line, Matcher matcherFinalKillerPlayer) {

        Matcher matcherFinalPositionDeadPlayer = PATTERN_FINAL_POSITION_DEAD_PLAYER.matcher(line);
        int initialPositionDeadPlayer = matcherFinalKillerPlayer.end();

        if (matcherFinalPositionDeadPlayer.find()) {
            String deadPlayerName = line.substring(initialPositionDeadPlayer, matcherFinalPositionDeadPlayer.start())
                    .trim();
            Player deadPlayer = players.stream().filter(p -> p.getPlayer().equalsIgnoreCase(deadPlayerName)).findAny()
                    .get();
            deadPlayer.addDeath();

            // String causeOfDeath = getCauseOfDeath(line, matcherFinalPositionDeadPlayer);

            return deadPlayer;

        }
        return null;
    }

    // Pegando a causa da morte
    /*
     * private String getCauseOfDeath(String line, Matcher
     * matcherFinalPositionDeadPlayer) { int initialPositionCauseOfDeath =
     * matcherFinalPositionDeadPlayer.end(); return
     * line.substring(initialPositionCauseOfDeath).trim(); //
     * System.out.println(playerKilled.getPlayer() + " matou " + deadPlayer + " com
     * // " + causaDaMorte); }
     */

    private String getKillerPlayerName(String line, Matcher matcherInitialKillerPlayer,
            Matcher matcherFinalPlayerKilled) {
        int initialOfPlayer = matcherInitialKillerPlayer.end();
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