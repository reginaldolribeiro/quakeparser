package br.com.quakeparser.quakeparser;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.quakeparser.quakeparser.Game;
import br.com.quakeparser.quakeparser.ParserService;

@RestController
public class ParserController {

    @GetMapping("/quake")
    public List<Game> getQuake() throws IOException {

        return new ParserService().getInfoGames();

    }

}