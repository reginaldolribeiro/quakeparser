# Quake Log Parser
  
  Realiza o parser do log do jogo Quake 3 Arena, pegando informações do jogo como: jogadores participantes, total de mortes no jogo e total de morte por jogadores.
  
  ### Regras
  1. Quando o player mata ele mesmo, perde -1 kill.  
  2. `<world>` não é um player e não aparece na lista de players e nem na lista de kills.
  3. Quando o `<world>` mata o player, ele perde -1 kill.
  4. `total_kills` são os kills dos games, isso inclui mortes do `<world>`.  
  
  ### Consulta dos dados
  Os dados coletados no log do Quake são expostos através de uma API RESTFul com dados no formato JSON.
  ### Endpoints RESTFul
   - http://localhost:8081/quake-api/games/
   - http://localhost:8081/quake-api/games/game_2
  
  #### JSON de retorno (Exemplo)
  ```
   {
        "name": "game_4",
        "totalKills": 105,
        "players": [
            "Dono da Bola",
            "Assasinu Credi",
            "Isgalamido",
            "Zeh"
        ],
        "kills": {
            "Dono da Bola": 5,
            "Assasinu Credi": 11,
            "Isgalamido": 19,
            "Zeh": 20
        },
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8081/quake-api/games/game_4"
            }
        ]
    }
  ```
  
  ### Setup
  
  ### Tecnologias utilizadas
  1. Java
  2. Spring Boot
  3. Testes unitários com JUnit e Rest Assured
