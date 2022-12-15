package starships.Factories;

import starships.GameConfiguration;
import starships.Player;
import starships.collidable.Health;

import java.util.ArrayList;
import java.util.List;

public class PlayerFactory {
    public static List<Player> generate(GameConfiguration config){
        int amount = config.getAmountOfPlayers();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            players.add(new Player("player-" + i, new Health(config.getAmountOfLives()), "starship-" + i + 1));
        }
        return players;
    }
}
