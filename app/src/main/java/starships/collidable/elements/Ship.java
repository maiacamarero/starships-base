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
//            int newX = (int) (getPosition().getX() + getSpeed() * getDirection().getX());
//            int newY = (int) (getPosition().getY() - getSpeed() * getDirection().getY());
            int newX = (int) (getPosition().getX() - 3.5 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
            int newY = (int) (getPosition().getY() + 3.5 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));

            if (!isInsideLimit(newX, newY)){
                return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), playerId, lastBulletShot, getDirection(), 0.0, bulletType, isVisible(), getHealth());
            }else return new Ship(getId(), new Vector(newX, newY), getRotationInDegrees(), getHeight(), getWidth(), playerId, lastBulletShot, getDirection(), getSpeed() - 5, bulletType, isVisible(), getHealth());


//            Vector position = new Vector(newX, newY);
//            if (newX < 720 && newX > 0 && newY < 700 && newY > 0){
//                return setPosition(position);
//            }
        }
        return getNewElementCollidable();
    }

    public Ship getNewElementCollidable() {
        return new Ship(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getPlayerId(), lastBulletShot, getDirection(), getSpeed(), bulletType, isVisible(), getHealth());
    }

    public Ship move(Vector direction){
        Ship ship;
        int newX = (int) (getPosition().getX() - 3.5 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
        int newY = (int) (getPosition().getY() - 3.5 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
        if (getSpeed() < 1000){
            //if (direction.getX() == 0 && direction.getY() == 0){
                ship = setDirectionSpeedPosition(direction, 70.0, new Vector(newX, newY));
          //  }else {
           // }
        }else if (getSpeed()>0){
            ship = setDirectionSpeedPosition(direction, -69.0, new Vector(newX, newY));

        }else ship = getNewElementCollidable();

        return ship;
    }

    public Ship rotate(double rotation){
        return setRotationInDegrees(rotation);
    }

    public Ship setPosition(Vector position){
        return new Ship(getId(), getPosition().sum(position), getRotationInDegrees(), getHeight(), getWidth(),getPlayerId(), lastBulletShot, getDirection(), getSpeed(),bulletType, isVisible(), getHealth());
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
        return x > 0 && x < 725 && y > 0 && y < 700;
    }
    public boolean isInsideLimit(){
        return getPosition().getX() > 0 && getPosition().getX() < 800 && getPosition().getY() > 0 && getPosition().getY() < 800;
    }
}
