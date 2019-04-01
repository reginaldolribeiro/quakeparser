package br.com.quakeparser.quakeparser;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.quakeparser.model.Game;
import br.com.quakeparser.service.ParserService;

@RestController
public class ParserController {

    @GetMapping("/quake-api/games")
    public ResponseEntity<List<Game>> getQuakeGames() throws IOException {
        List<Game> games = new ParserService().getInfoGames();
        if (games != null) {
            games.forEach(g -> {
                try {
                    g.add(linkTo(methodOn(ParserController.class).getQuakeGamesByName(g.getName())).withSelfRel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return new ResponseEntity<>(games, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/quake-api/games/{gameName}")
    public ResponseEntity<Game> getQuakeGamesByName(@PathVariable String gameName) throws IOException {
        List<Game> games = new ParserService().getInfoGames();
        Game game = games.stream().filter(g -> g.getName().equalsIgnoreCase(gameName)).findAny().orElse(null);
        if (game != null) {
            game.add(linkTo(methodOn(ParserController.class).getQuakeGames()).withRel("Games list"));
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}