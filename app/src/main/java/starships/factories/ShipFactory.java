package starships.factories;

import starships.Player;
import starships.collidable.Collidable;
import starships.collidable.elements.Ship;
import starships.collidable.Position;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {
    public static List<Collidable> generate(int amountOfShips, List<Player> players){
        List<Collidable> listToReturn = new ArrayList<>();
        addShips(listToReturn, amountOfShips, players);
        return listToReturn;
    }

    private static void addShips(List<Collidable> listToReturn, int amountOfShips, List<Player> players) {
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            Position position = new Position(((1250/amountOfShips) * i) / 2, 550);
            Player player = players.get(i-1);
            Ship ship = new Ship(id, position, 180, 70, 70, player.getPlayerId());
            listToReturn.add(ship);
        }
    }

}
