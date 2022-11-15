package starships;

public class Collision {

    private Collidable collidable;
    private Collidable otherCollidable;

    public Collision(Collidable collidable, Collidable otherCollidable) {
        this.collidable = collidable;
        this.otherCollidable = otherCollidable;
    }

    public Collision handleCollision(Collidable collidable, Collidable otherCollidable){
        if (collidable.getCollidableType().equals(CollidableType.SHIP) && otherCollidable.getCollidableType().equals(CollidableType.ASTEROID)){
            return new Collision(collidable.setHealth(collide(collidable, otherCollidable)), otherCollidable);
        } else if (collidable.getCollidableType().equals(CollidableType.SHIP) && otherCollidable.getCollidableType().equals(CollidableType.BULLET)) {
            return new Collision(collidable.setHealth(collide(collidable, otherCollidable)), otherCollidable);
        }
        throw new RuntimeException();
    }

    private Health collide(Collidable collidable, Collidable otherCollidable){
        return collidable.getHealth().reduce(otherCollidable.getDamage().getValue());
    }
}
