package starships.collidable.elements;

import starships.collidable.*;

public class Ship extends Collidable {

    private long lastBulletShot;
    private final String playerId;
    private double accelerate;
    private BulletType bulletType;

    public Ship(String id, Position position, int rotationInDegrees, int height, int width, String playerId) {
        super(id, CollidableType.SHIP, position, rotationInDegrees, height, width, CollidableShape.TRIANGULAR, rotationInDegrees);
        this.playerId = playerId;
    }

    public Ship(String id, Position position, double rotationInDegrees, double height, double width, String playerId, long lastBulletShot, double direction, double boost, BulletType bulletType) {
        super(id, CollidableType.SHIP, position, rotationInDegrees, height, width, CollidableShape.TRIANGULAR, direction);
        this.playerId = playerId;
        this.lastBulletShot = lastBulletShot;
        this.accelerate = boost;
        this.bulletType = bulletType;

    }

    public Ship shoot(){
        return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), playerId, System.currentTimeMillis(), getDirection(), accelerate, bulletType);
    }

    @Override
    public Ship update(){
        if (accelerate > 0){
            int newX = (int) (getPosition().getX() -  3.5 * Math.sin(Math.PI * 2 * getDirection() / 360));
            int newY = (int) (getPosition().getY() +  3.5 * Math.cos(Math.PI * 2 * getDirection() / 360));
            Position position = new Position(newX, newY);
            if (!isInBounds(position)){
                return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(),getWidth(),playerId,lastBulletShot, getDirection(), 0, bulletType);
            }
            else{
                return new Ship(getId(), position, getRotationInDegrees(), getHeight(),getWidth(),playerId,lastBulletShot, getDirection(), accelerate - 5, bulletType);
            }
        }
        return (Ship) getNewElementColisionable();
    }

    @Override
    public Collidable getNewElementColisionable() {
        return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), accelerate, bulletType);
    }

    public Ship moveX(boolean accept) {
        if (accept){
            return leftMovement();
        }else return rightMovement();
    }

    private Ship rightMovement() {
        if (accelerate < 100){
            Position position = new Position(getPosition().getX() + 10, getPosition().getY() );
            return new Ship(getId(), position, getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), accelerate, bulletType);
        }
        return (Ship) getNewElementColisionable();
    }


    private Ship leftMovement() {
        if (accelerate < 100){
            Position position = new Position(getPosition().getX() - 10, getPosition().getY());
            return new Ship(getId(), position, getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), accelerate, bulletType);
        }
        return (Ship) getNewElementColisionable();
    }

    public Ship moveY(boolean accept){
        if (accept){
            return accelerate();
        }else return slowDown();
    }

    private Ship slowDown() {
        if (accelerate < 100){
            Position position = new Position(getPosition().getX(), getPosition().getY() + 70);
            return new Ship(getId(), position, getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), accelerate -= 170, bulletType);
        }
        return (Ship) getNewElementColisionable();
    }

    private Ship accelerate() {
        if (accelerate < 100){
            return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), accelerate += 70, bulletType);
        }
        return (Ship) getNewElementColisionable();
    }

    public Ship rotate(int rotation){
        return new Ship(getId(), getPosition(), getRotationInDegrees() + rotation, getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), accelerate, bulletType);
    }

    public boolean canShoot(){
        return System.currentTimeMillis() - lastBulletShot > 500;
    }

    public long getLastBulletShot() {
        return lastBulletShot;
    }

    public String getPlayerId() {
        return playerId;
    }

    public double getAccelerate() {
        return accelerate;
    }

    public BulletType getBulletType() {
        return bulletType;
    }


}
