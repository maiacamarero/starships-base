package starships.Factories;

import starships.collidable.Bullet;
import starships.collidable.Ship;
import starships.collidable.Vector;

import java.util.Random;

public class BulletFactory {

    private static int counter = 0;

    public static Bullet generate(Ship ship){
        String id = "bullet-" + ++counter;
        Random random = new Random();
        int r = random.nextInt(2, 5);
        return new Bullet(id, new Vector(ship.getPosition().getX()+16, ship.getPosition().getY()), ship.getRotationInDegrees()-20, r*12, r*4, ship.getRotationInDegrees(), ship.getId(), (r*13), ship.getBulletType());

    }
}
