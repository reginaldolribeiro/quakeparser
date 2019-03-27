package br.com.quakeparser.quakeparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ParserController {

    @GetMapping
    public @ResponseBody String teste() {
        return "Testeee";
    }

    @GetMapping("/teste")
    public List<String> strings() {
        return Arrays.asList("Reginado", ";aldsf", "dddd");
    }

    @GetMapping("/games")
    public List<Game> games() {
        Game game = new Game("game1");
        Game game2 = new Game("Game2");

        List<Game> games = new ArrayList<>();
        games.add(game);
        games.add(game2);

        return games;
    }

    @GetMapping("/quake")
    public List<Game> getQuake() throws IOException {

        return new ParserService().getInfoGames();

    }

}