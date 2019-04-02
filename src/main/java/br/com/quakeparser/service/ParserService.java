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

    private Game game;
    private int counterGame = 0;

    private static final Pattern PATTERN_FOR_KILL = Pattern.compile("\\s*\\d{1,2}:\\d{2}\\s*(Kill:)\\w*");
    private static final Pattern PATTERN_INITIAL_POSITION_KILLER_PLAYER = Pattern
            .compile("Kill:\\s+[0-9]{1,4}\\s+[0-9]\\s+[0-9]{1,}:\\s+");
    private static final Pattern PATTERN_FINAL_POSITION_KILLER_PLAYER = Pattern.compile("\\s+\\bkilled");
    private static final Pattern PATTERN_FINAL_POSITION_DEAD_PLAYER = Pattern.compile("\\bby\\b");

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

                game.addTotalKill();

                Matcher matcherInitialKillerPlayer = PATTERN_INITIAL_POSITION_KILLER_PLAYER.matcher(line);
                Matcher matcherFinalKillerPlayer = PATTERN_FINAL_POSITION_KILLER_PLAYER.matcher(line);

                if (matcherInitialKillerPlayer.find() && matcherFinalKillerPlayer.find()) {

                    String killerPlayer = getKillerPlayer(line, matcherInitialKillerPlayer, matcherFinalKillerPlayer);

                    String deadPlayer = getDeadPlayer(line, matcherFinalKillerPlayer);

                    if (killerPlayer.equals("<world>")) {
                        game.subtractKill(deadPlayer);
                    } else {
                        // suicidio
                        if (killerPlayer.equals(deadPlayer)) {
                            game.subtractKill(killerPlayer);
                        } else {
                            game.addKill(killerPlayer);
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
            game.getPlayers().add(playerName);
            if (!game.getKills().containsKey(playerName)) {
                game.getKills().put(playerName, 0);
            }

        }
        return game;
    }

    // Criando o jogo
    private Game createGame(List<Game> games, String line) {
        if (line.contains("InitGame:")) {
            counterGame++;
            game = new Game("game_" + counterGame);
            games.add(game);
            return game;
        }
        return game;
    }

    // Pegando o player que morreu
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

    private String getKillerPlayer(String line, Matcher matcherInitialKillerPlayer, Matcher matcherFinalPlayerKilled) {
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