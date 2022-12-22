package starships.collidable.elements;

import starships.collidable.*;

public class Ship extends Collidable {

    private long lastBulletShot;
    private final String playerId;
    //private double accelerate;
    private BulletType bulletType;
    private int amountOfShots;

    public Ship(String id, Vector position, double rotationInDegrees, double height, double width, String playerId, Vector direction, double speed, Health health, boolean isVisible, int amountOfShots) {
        super(id, CollidableType.SHIP, position, rotationInDegrees, height, width, CollidableShape.TRIANGULAR, direction, speed, health, isVisible);
        this.playerId = playerId;
        this.amountOfShots = amountOfShots;
    }

    public Ship(String id, Vector position, double rotationInDegrees, double height, double width, String playerId, long lastBulletShot, Vector direction, double speed, BulletType bulletType, boolean isVisible, Health health) {
        super(id, CollidableType.SHIP, position, rotationInDegrees, height, width, CollidableShape.TRIANGULAR, direction, speed, health, isVisible);
        this.playerId = playerId;
        this.lastBulletShot = lastBulletShot;
        this.bulletType = bulletType;
    }

    public Bullet shoot(){
        return new Bullet(getId() + amountOfShots, getPosition(), getRotationInDegrees(), 10, 5, getDirection(), getId(), 5, BulletType.NORMAL, 10, isVisible());
    }

    public Ship update(){
        if (getSpeed() > 0){
            int newX = (int) (getPosition().getX() + getSpeed() * getDirection().getX());
            int newY = (int) (getPosition().getY() + getSpeed() * getDirection().getY());
//            int newX = (int) (getPosition().getX() - 3.5 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
//            int newY = (int) (getPosition().getY() + 3.5 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
            if (isInsideLimit(newX, newY)){
                return setPosition(new Vector(newX, newY));
            }
        }
        return this;
    }

    public Ship getNewElementCollidable() {
        return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), getSpeed(), bulletType, isVisible(), getHealth());
    }

    public Ship move(Vector direction) {
        Ship ship = setDirection(direction);
        int newX = (int) (direction.getX() + 3.5 * Math.sin(Math.PI * 2 * direction.getX() / 360));
        int newY = (int) (direction.getY() + 3.5 * Math.sin(Math.PI * 2 * direction.getY() / 360));
//        if (getSpeed() < 1000){
        if (direction.getX() == 0 && direction.getY() == 0) {
            //ship = setDirectionSpeedPosition(direction, 70, new Vector(1, 1));
            ship = ship.setDirectionPosition(direction, new Vector(0, 0));
            //  }else {
            // }
        }else {
           ship = ship.setDirectionPosition(direction,  new Vector(newX, newY));

//        }//else ship = getNewElementCollidable();

        }

        return ship;

    }

    public Ship rotate(double rotation){
        return setRotationInDegrees(rotation);
    }

    public Ship setPosition(Vector position){
        return new Ship(getId(), getPosition().sum(position), getRotationInDegrees(), getHeight(), getWidth(),getPlayerId(), lastBulletShot, getDirection(), getSpeed(),bulletType, isVisible(), getHealth());
    }
    public Ship setDirection(Vector direction){
        return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(),getPlayerId(), lastBulletShot, getDirection().sum(direction), getSpeed(),bulletType, isVisible(), getHealth());
    }
    public Ship setDirectionPosition(Vector direction, Vector position){
        return new Ship(getId(), getPosition().sum(position), getRotationInDegrees(), getHeight(), getWidth(),getPlayerId(), lastBulletShot, getDirection().sum(direction), getSpeed(),bulletType, isVisible(), getHealth());
    }

    public Ship setSpeed(double speed){
        return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(),getPlayerId(), lastBulletShot, getDirection(), getSpeed() + speed, bulletType, isVisible(), getHealth());
    }

    public Ship setRotationInDegrees(double rotationInDegrees){
        return new Ship(getId(), getPosition(), getRotationInDegrees() + rotationInDegrees, getHeight(), getWidth(),getPlayerId(), lastBulletShot, getDirection(), getSpeed(),bulletType, isVisible(), getHealth());
    }

    public Ship setDirectionSpeedPosition(Vector direction, double speed, Vector position){
        return new Ship(getId(), getPosition().sum(position), getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection().sum(direction), getSpeed() + speed,bulletType,isVisible(), getHealth());
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

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getAmountOfShots() {
        return amountOfShots;
    }

    public boolean isInsideLimit(int x, int y){
        return x > 0 && x < 720 && y > 0 && y < 700;
    }
    public boolean isInsideLimit(){
        return getPosition().getX() > 0 && getPosition().getX() < 800 && getPosition().getY() > 0 && getPosition().getY() < 800;
    }
}
