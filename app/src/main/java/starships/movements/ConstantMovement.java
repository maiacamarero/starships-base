package starships.movements;

import starships.Collidable;
import starships.Vector;

public class ConstantMovement implements MovementStrategy{
    @Override
    public Collidable move(Collidable collidable) {
        Vector newPosition = collidable.getPosition().sum(collidable.getSpeed());
        return collidable.setPosition(newPosition);
    }
}
