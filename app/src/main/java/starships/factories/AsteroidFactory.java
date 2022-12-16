package starships.factories;

import starships.collidable.*;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidFactory {

    private static int counter = 0;
    static Random random = new Random();

    public static void generate(List<Asteroid> asteroids, List<Collidable> elements){
        if (asteroids.size() < 10){
            List<Ship> ships = getCurrentsShips(elements);
            int x, y;
            Ship target = getRandomShip(ships);
            if (target == null) return;
            int side = random.nextInt(20);
            int number = random.nextInt(2000);
            switch (side){
                case 0 -> {
                    x = number;
                    y = 0;
                }
                case 1 -> {
                    x = 0;
                    y = number;
                }
                case 2 -> {
                    x = number;
                    y = 994;
                }
                default -> {
                    x = 994;
                    y = number;
                }
            }
            Position position = new Position(x, y);
            int direction = getDirection(position, target);
            String id = "asteroid-" + counter++;
            int height = random.nextInt(10, 150);
            int width = random.nextInt(80, 150);
            Health health = new Health(calculateHealth(height, width));
            elements.add(new Asteroid(id, position, 100, height, width, direction, random.nextBoolean(), health, health));
        }
    }

    private static List<Ship> getCurrentsShips(List<Collidable> elements) {
        List<Ship> ships = new ArrayList<>();
        for (Collidable element : elements){
            if (element.getCollidableType() == CollidableType.SHIP){
                ships.add((Ship) element);
            }
        }
        return ships;
    }

    private static Ship getRandomShip(List<Ship> ships) {
        if (ships.isEmpty()){
            return null;
        }else return ships.get(random.nextInt(ships.size()));
    }

    private static int getDirection(Position position, Ship target) {
        return (int) (Math.toDegrees(Math.atan2(target.getPosition().getX() - position.getX(), target.getPosition().getY() - position.getY())) + random.nextInt(35));
    }

    private static int calculateHealth(int height, int width) {
        return ((height * width) / 100);
    }


}
