package starships.factories;

import starships.Player;
import starships.collidable.*;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidFactory {

    private static int counter = 0;
    static Random random = new Random();

    public List<Collidable> generate(List<Collidable> elements){
        List<Collidable> newElements = new ArrayList<>();
        if (getAmountOfAsteroids(elements) < 10){
            List<Ship> ships = getCurrentsShips(elements);
            int x, y;
            Ship target = getRandomShip(ships);
            int sideOfScreen = random.nextInt(4);
            int number = random.nextInt(800);
            Vector direction;
            assert target != null;
            switch (sideOfScreen){
                case 0 -> {
                    x = number;
                    y = 0;
                    if (target.getPosition().getX() > number){
                        direction = new Vector(target.getPosition().getX() + number, target.getPosition().getY());
                    }else {
                        direction = new Vector(target.getPosition().getX() - number, target.getPosition().getY());
                    }
                }
                case 1 -> {
                    x = 0;
                    y = number;
                    if (target.getPosition().getY() > number){
                        direction = new Vector(target.getPosition().getX(), target.getPosition().getY() + number);
                    }else {
                        direction = new Vector(target.getPosition().getX(), target.getPosition().getY() - number);
                    }
                }
                case 2 -> {
                    x = number;
                    y = 800;
                    if (target.getPosition().getX() > number){
                        direction = new Vector(target.getPosition().getX() + number, -target.getPosition().getY());
                    }else {
                        direction = new Vector(target.getPosition().getX() - number, -target.getPosition().getY());
                    }
                }
                default -> {
                    x = 800;
                    y = number;
                    if (target.getPosition().getY() > number){
                        direction = new Vector(-target.getPosition().getX(), target.getPosition().getY() + number);
                    }else {
                        direction = new Vector(-target.getPosition().getX(), target.getPosition().getY() - number);
                    }
                }
            }
            Vector position = new Vector(x, y);
            String id = "asteroid-" + counter++;
            int height = random.nextInt(10, 150);
            int width = random.nextInt(80, 150);
            Health health = new Health(calculateHealth(height, width));
            newElements.add(new Asteroid(id, position, 100, height, width, direction, 0.0015, health, random.nextBoolean(), random.nextBoolean(), health, health));
        }
        return newElements;
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

    private static int getAmountOfAsteroids(List<Collidable> elements){
        int amount = 0;
        for (Collidable element : elements) {
            if (element.getCollidableType() == CollidableType.ASTEROID){
                amount++;
            }
        }
        return amount;
    }

    private static Ship getRandomShip(List<Ship> ships) {
        return ships.get(random.nextInt(ships.size()));
    }

    private static int calculateHealth(int height, int width) {
        return ((height * width) / 100);
    }


}
