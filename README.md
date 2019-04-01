# Quake Parser
  
  Realizar o parser do log do jogo Quake 3 Arena, pegando informacoes do jogo como: jogadores participantes, total de mortes no jogo e total de morte por jogares.
  ```
  {
    "name": "game_1",
    "totalKills": 11,
    "totalKillsWorld": 8,
    "players": [
      {
        "player": "Mocinha",
        "totalKills": 0,
        "totalDeaths": 1
      }
    ],
    "playersString": [
      "Dono da Bola",
      "Mocinha",
      "Isgalamido"
    ],
    "kills": {
      "Dono da Bola": 0,
      "Mocinha": 0,
      "Isgalamido": 0
    }
  }
  ```
  
  ### API de consulta
  http://google.com
  
  ### Observações

1. Quando o `<world>` mata o player ele perde -1 kill.
2. `<world>` não é um player e não deve aparecer na lista de players e nem no dicionário de kills.
3. `total_kills` são os kills dos games, isso inclui mortes do `<world>`.
4. Quando o player mata ele mesmo, perde -1 kill.
