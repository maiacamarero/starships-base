package starships.factories;

import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;
import starships.collidable.Position;

import java.util.Random;

public class BulletFactory {

    private static int counter = 0;

    public static Bullet generate(Ship ship){
        String id = "bullet-" + counter++;
        Random random = new Random();
        int r = random.nextInt(2, 5);
        return new Bullet(id, new Position(ship.getPosition().getX()+16, ship.getPosition().getY()), ship.getRotationInDegrees()-20, r*12, r*4, ship.getRotationInDegrees(), ship.getId(), 1, ship.getBulletType());
    }
}
