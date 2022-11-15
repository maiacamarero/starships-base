package starships.movements;

import starships.Collidable;

public class LimitMovement implements MovementStrategy{

    private final int speedLimit;
    private final ConstantMovement constantMovement;
    private final VariableMovement variableMovement;

    public LimitMovement(int speedLimit, ConstantMovement constantMovement, VariableMovement variableMovement) {
        this.speedLimit = speedLimit;
        this.constantMovement = constantMovement;
        this.variableMovement = variableMovement;
    }

    @Override
    public Collidable move(Collidable collidable) {
        if (speedModule(collidable) < speedLimit){
            return variableMovement.move(collidable);
        }else return constantMovement.move(collidable);
    }

    public double speedModule(Collidable collidable){
        return Math.pow(collidable.getSpeed().getX(), 2) + Math.pow(collidable.getSpeed().getY(), 2);
    }
}
