package starships.factories;

import starships.Player;
import starships.collidable.Collidable;
import starships.collidable.Health;
import starships.collidable.elements.Ship;
import starships.collidable.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {
    public List<Collidable> generate(List<Player> players){
        int amountOfShips = players.size();
        List<Collidable> listToReturn = new ArrayList<>();
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            Vector position = new Vector(((1250/amountOfShips) * i) / 2, 500);
            Player player = players.get(i-1);
            Ship ship = new Ship(id, position, 180, 70, 70, player.getPlayerId(), new Vector(300, 300), 0.0, new Health(3), true, 10);
            listToReturn.add(ship);
        }
        return listToReturn;
    }

}
