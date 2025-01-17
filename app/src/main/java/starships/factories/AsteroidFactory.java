package starships.factories;

import starships.collidable.*;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidFactory {
    Random random = new Random();

    public List<Collidable> generate(List<Collidable> elements){
        List<Collidable> newElements = new ArrayList<>();
        int amountOfAsteroids = getAmountOfAsteroids(elements);
        if (amountOfAsteroids < 7){
                List<Ship> ships = getCurrentsShips(elements);
                int x, y;
                Ship target = getRandomShip(ships);
                int sideOfScreen = random.nextInt(4);
                int number = random.nextInt(80);
                assert target != null;

                switch (sideOfScreen){
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
                        y = 790;
                    }
                    default -> {
                        x = 790;
                        y = number;
                    }
                }
                Vector direction = getDirection(x, y, target);
                Vector position = new Vector(x, y);
                String id = "asteroid-" + target.getId().substring(9).concat(String.valueOf(random.nextInt(0, 100)));
                int height = random.nextInt(50, 150);
                int width = random.nextInt(50, 150);
                Health health = new Health(calculateHealth(height, width));
                newElements.add(new Asteroid(id, position, 180, height, width, direction, 10, health, true, true, health, health));
        }
        return newElements;
    }

    private Vector getDirection(int x, int y, Ship target) {
        return new Vector(target.getDirection().getX() - x + random.nextInt(20), target.getDirection().getY() - y + random.nextInt(20));
    }

    private List<Ship> getCurrentsShips(List<Collidable> elements) {
        List<Ship> ships = new ArrayList<>();
        for (Collidable element : elements){
            if (element.getCollidableType() == CollidableType.SHIP){
                ships.add((Ship) element);
            }
        }
        return ships;
    }

    private int getAmountOfAsteroids(List<Collidable> elements){
        int amount = 0;
        for (Collidable element : elements) {
            if (element.getCollidableType() == CollidableType.ASTEROID && element.isVisible()){
                amount++;
            }
        }
        return amount;
    }

    private Ship getRandomShip(List<Ship> ships) {
        return ships.get(random.nextInt(ships.size()));
    }

    private int calculateHealth(int height, int width) {
        return ((height * width) / 100);
    }


}
