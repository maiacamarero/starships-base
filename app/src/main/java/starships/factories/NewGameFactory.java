package starships.factories;

import starships.GameConfiguration;
import starships.Juego;
import starships.Player;
import starships.State;
import starships.collidable.Collidable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewGameFactory {
    private final PlayerFactory playerFactory = new PlayerFactory();
    private final ShipFactory shipFactory = new ShipFactory();
    public Juego ganerate(GameConfiguration configuration) {
        List<Player> players = playerFactory.generate(configuration);
        List<Collidable> ships = shipFactory.generate(players);
        State state = new State(ships, players);
        return new Juego(new ArrayList<>(), new HashMap<>(), configuration, state, false, false);
    }
}
