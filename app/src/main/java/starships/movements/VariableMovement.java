package starships.movements;

import starships.Collidable;
import starships.Vector;

public class VariableMovement implements MovementStrategy{
    @Override
    public Collidable move(Collidable collidable) {
        Vector newPosition = collidable.getPosition().sum(collidable.getAcelec().sum(collidable.getSpeed()));
        return collidable.setAcelec(newPosition);
    }
}
