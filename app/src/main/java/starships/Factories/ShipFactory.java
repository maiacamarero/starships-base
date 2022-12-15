package starships.Factories;

import starships.GameConfiguration;
import starships.Player;
import starships.collidable.Colisionable;
import starships.collidable.Ship;
import starships.collidable.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {
    public static List<Colisionable> generate(int amountOfShips, List<Player> players){
        List<Colisionable> listToReturn = new ArrayList<>();
        addShips(listToReturn, amountOfShips, players);
        return listToReturn;
    }

    private static void addShips(List<Colisionable> listToReturn, int amountOfShips, List<Player> players) {
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            Vector position = new Vector(((800/amountOfShips) * i) / 2, 300);
            Player player = players.get(i-1);
            Ship ship = new Ship(id, position, 180, 40, 40, player.getPlayerId());
            listToReturn.add(ship);
        }
    }

}
