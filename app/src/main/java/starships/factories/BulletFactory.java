package starships.factories;

import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;
import starships.collidable.Vector;

import java.util.Random;

public class BulletFactory {

    private static int counter = 0;

    public Bullet generate(Ship ship){
        String id = "bullet-" + counter++;
        Random random = new Random();
        int r = random.nextInt(2, 5);

        return new Bullet(id,
                new Vector(ship.getPosition().getX()+16, 140),
                ship.getRotationInDegrees()-2,
                r*12,
                r*4,
                ship.getDirection(),
                ship.getId(),
                r,
                ship.getBulletType(),
                ship.getSpeed(),
                ship.isVisible());
    }
}
