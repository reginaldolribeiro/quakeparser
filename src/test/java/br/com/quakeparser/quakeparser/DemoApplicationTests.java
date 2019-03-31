package br.com.quakeparser.quakeparser;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import br.com.quakeparser.model.Game;
import br.com.quakeparser.service.ParserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

    private static final String ENDPOINT_QUAKE_LOG = "/quake";
    private ParserService service;
    private List<Game> games;
    private Response responseGames;
    private Response responseGame_2;

    @Before
    public void init() throws IOException {

        RestAssured.port = 8081;
        this.responseGames = RestAssured.given().get(ENDPOINT_QUAKE_LOG);
        this.responseGame_2 = RestAssured.given().get(ENDPOINT_QUAKE_LOG + "/game_2");

        this.service = new ParserService();
        this.games = service.getInfoGames();

    }

    /**
     * Rest Assured
     */
    @Test
    public void checkHeaderResponse() {
        this.responseGames.then().statusCode(200).and().contentType(ContentType.JSON);
        // OU
        // Response response = RestAssured.given().get(QUAKE);
        // assertThat(response.getStatusCode(), Matchers.equalTo(200));
    }

    @Test
    public void checkGamesSizeNotNullAndNotEmpty() {
        List<Game> games = this.responseGames.andReturn().getBody().jsonPath().getList("games", Game.class);
        assertNotNull(games);
        assertTrue(!games.isEmpty());
        assertEquals(games.size(), 21);
    }

    @Test
    public void checkStatusCodeForGameNotFound() {
        RestAssured.given().get("/quake/game_25").then().statusCode(404);
    }

    @Test
    public void checkHeaderResponseForIndividualGame() {
        this.responseGame_2.then().statusCode(200).and().contentType(ContentType.JSON);
    }

    @Test
    public void checkIndividualGame() {
        JsonPath path = this.responseGame_2.andReturn().jsonPath();

        int sizePlayers = path.getList("players").size();
        assertTrue(sizePlayers == 3);

        String mocinha = path.getString("players[0].player");
        assertTrue(mocinha.equals("Mocinha"));

        String donoDaBola = path.getString("players[1].player");
        assertTrue(donoDaBola.equals("Dono da Bola"));

        String isgalamido = path.getString("players[2].player");
        assertTrue(isgalamido.equals("Isgalamido"));
    }

    /**
     * Testes nas classes Java
     */

    @Test(expected = AssertionError.class)
    public void testeGamesLogModified() throws IOException {
        assertEquals(games.size(), 2);
    }

    @Test
    public void gamesSize() throws IOException {
        assertEquals(games.size(), 21);
    }

    @Test
    public void gamePlayers() throws IOException {
        int totalPlayers = games.get(1).getPlayers().size();
        assertEquals(totalPlayers, 3);
    }

}
