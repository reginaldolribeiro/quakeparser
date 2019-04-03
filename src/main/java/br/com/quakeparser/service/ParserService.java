package br.com.quakeparser.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.quakeparser.model.Game;

public class ParserService {

    private static final String GAMES_LOG = "games.log";

    private static final Pattern PATTERN_FOR_KILL = Pattern.compile("\\s*\\d{1,2}:\\d{2}\\s*(Kill:)\\w*");
    private static final Pattern PATTERN_INITIAL_POSITION_KILLER_PLAYER = Pattern
            .compile("Kill:\\s+[0-9]{1,4}\\s+[0-9]\\s+[0-9]{1,}:\\s+");
    private static final Pattern PATTERN_FINAL_POSITION_KILLER_PLAYER = Pattern.compile("\\s+\\bkilled");
    private static final Pattern PATTERN_FINAL_POSITION_DEAD_PLAYER = Pattern.compile("\\bby\\b");

    private Game game;
    private int counterGame = 0;

    public List<Game> getInfoGames() throws IOException {

        List<Game> games = new ArrayList<>();

        List<String> lines = loadFile(GAMES_LOG);
        System.out.println("*** Quantidade de linhas do games.log: " + lines.size() + " ***");

        lines.forEach(line -> {

            createGame(games, line);

            addPlayers(line);

            handleKills(line);

        });

        // games.forEach(System.out::println);
        System.out.println("Quantidade de games: " + games.size());

        return games;

    }

    private List<String> loadFile(String path) throws IOException {
        Path pathToFile = Paths.get(path);
        List<String> lines = Files.readAllLines(pathToFile);
        return lines;
    }

    private void createGame(List<Game> games, String line) {
        if (line.contains("InitGame:")) {
            counterGame++;
            game = new Game("game_" + counterGame);
            games.add(game);
        }
    }

    private void addPlayers(String line) {

        // \d{1,2}:\d{2}\s+ClientUserInfoChanged:\s+\d{1,4}\s+

        if (line.contains("ClientUserinfoChanged")) {
            String playerName = getPlayerName(line);
            game.getPlayers().add(playerName);
            if (!game.getKills().containsKey(playerName)) {
                game.getKills().put(playerName, 0);
            }
        }

    }

    private String getPlayerName(String line) {
        int initialPosition = line.indexOf("\\");
        int finalPosition = line.indexOf("\\t");
        return line.substring((initialPosition + 1), finalPosition).trim();
    }

    private String getKillerPlayer(String line, Matcher matcherInitialKillerPlayer, Matcher matcherFinalPlayerKilled) {
        int initialOfPlayer = matcherInitialKillerPlayer.end();
        int finalOfPlayer = matcherFinalPlayerKilled.start();
        return line.substring(initialOfPlayer, finalOfPlayer).trim();
    }

    private void handleKills(String line) {

        Matcher matcherForKill = PATTERN_FOR_KILL.matcher("");
        matcherForKill.reset(line);

        if (matcherForKill.find()) {

            game.addTotalKill();

            Matcher matcherInitialKillerPlayer = PATTERN_INITIAL_POSITION_KILLER_PLAYER.matcher(line);
            Matcher matcherFinalKillerPlayer = PATTERN_FINAL_POSITION_KILLER_PLAYER.matcher(line);

            if (matcherInitialKillerPlayer.find() && matcherFinalKillerPlayer.find()) {

                String killerPlayer = getKillerPlayer(line, matcherInitialKillerPlayer, matcherFinalKillerPlayer);

                String deadPlayer = getDeadPlayer(line, matcherFinalKillerPlayer);

                if (killerPlayer.equals("<world>")) {
                    game.subtractKill(deadPlayer);
                } else {
                    if (isSuicide(killerPlayer, deadPlayer)) {
                        game.subtractKill(killerPlayer);
                    } else {
                        game.addKill(killerPlayer);
                    }

                }
            }
        }
    }

    private String getDeadPlayer(String line, Matcher matcherFinalKillerPlayer) {

        Matcher matcherFinalPositionDeadPlayer = PATTERN_FINAL_POSITION_DEAD_PLAYER.matcher(line);
        int initialPositionDeadPlayer = matcherFinalKillerPlayer.end();

        if (matcherFinalPositionDeadPlayer.find()) {
            String deadPlayerName = line.substring(initialPositionDeadPlayer, matcherFinalPositionDeadPlayer.start())
                    .trim();
            return deadPlayerName;

        }
        return null;
    }

    private boolean isSuicide(String killerPlayer, String deadPlayer) {
        return killerPlayer.equals(deadPlayer);
    }

}