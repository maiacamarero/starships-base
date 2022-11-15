package starships.movements;

import starships.Collidable;

public class RotationMovement implements MovementStrategy{

    private final int rotationValueInDegrees;

    public RotationMovement(int rotationValueInDegrees) {
        this.rotationValueInDegrees = rotationValueInDegrees;
    }

    @Override
    public Collidable move(Collidable collidable) {
        return collidable.setRotationInDegrees(collidable.getRotationInDegrees() + rotationValueInDegrees);
    }
}
