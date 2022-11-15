package starships.movements;

import starships.Collidable;

public interface MovementStrategy {
    Collidable move(Collidable collidable);
}
