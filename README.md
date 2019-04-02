# Quake Log Parser
  
  Realiza o parser do arquivo de log do jogo Quake 3 Arena, pegando informações como os jogadores participantes, total de mortes por jogo e total de morte por jogadores.
  A coleta dos dados é feita com o uso de expressões regulares, em busca de padrões de textos no arquivo, sendo eles:
   - InitGame
   - ClientConnect/ClientUserinfoChanged
   - Kill:
   - ShutdownGame:
  
  ### Exemplo do arquivo de log
    22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH
  O player Isgalamido matou o player Mocinha.  
    
    23:06 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT
  O player Isgalamido foi morto pois estava ferido.
  
  ### Regras
  1. Quando o player mata ele mesmo, perde -1 kill.  
  2. `<world>` não é um player e não aparece na lista de players e nem na lista de kills.
  3. Quando o `<world>` mata o player, ele perde -1 kill.
  4. `total_kills` são os kills dos games, isso inclui mortes do `<world>`.  
  
  ### API de consulta dos dados
  Os dados coletados no log do Quake são expostos através de uma API RESTFul com dados no formato JSON.  
  
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
  
  #### Endpoints RESTFul
   - http://localhost:8081/quake-api/games/
   - http://localhost:8081/quake-api/games/game_2
   
  ### Setup
  1. Realizar o download do projeto aqui no Github.
  2. Extrair o arquivo quakeparser-master.zip
  3. Entrar na pasta do projeto.
  4. Executar o comando mvn spring-boot:run
  5. Após a aplicação subir, acesse a URL http://localhost:8081/quake-api/games/
  
  ### Tecnologias utilizadas
  1. Java
  2. Spring Boot
  3. Testes unitários com JUnit e Rest Assured
  4. Maven
