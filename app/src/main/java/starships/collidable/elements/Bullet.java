package starships.collidable.elements;

import starships.collidable.*;

public class Bullet extends Collidable {

    private final String ship;
    private final int damage;
    private final BulletType bulletType;

    public Bullet(String id, Vector position, double rotationInDegrees, double height, double width, Vector direction, String shipID, int damage, BulletType bulletType, double speed, boolean isVisible) {
        super(id, CollidableType.BULLET, position, rotationInDegrees, height, width, CollidableShape.RECTANGULAR, direction, speed, new Health(1), isVisible);
        this.ship = shipID;
        this.damage = damage;
        this.bulletType = bulletType;
    }

    public String getShipId(){
        return this.ship;
    }

    public Bullet update(){
        if (isVisible()){
            if (getSpeed() > 0){
                int newX = (int) (getPosition().getX() + getSpeed() * getDirection().getX());
                int newY = (int) (getPosition().getY() + getSpeed() * getDirection().getY());
                if (isInBounds()){
                    return (Bullet) setPosition(new Vector(newX, newY));
                }else return (Bullet) setIsVisible(false);
            }
        }
        return this;
    }

//    public Collidable getNewElementCollidable() {
//        return new Bullet(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), getShipId(), damage, bulletType);
//    }

//    public boolean isInBounds(){
//        if ((getPosition().getX() > (0-getHeight())) && (getPosition().getX()<(800+getHeight())) && (getPosition().getY() > (0-getHeight())) && (getPosition().getX()<(800+getHeight()))){
//            return true;
//        }else return false;
//    }

    public BulletType getBulletType() {
        return bulletType;
    }

    private Bullet move() {
        //Bullet bullet = null;
        int newX = (int) (getPosition().getX() - 4 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
        int newY = (int) (getPosition().getY() + 4 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
        Vector newVector = new Vector(newX, newY);
        return (Bullet) setPosition(newVector);
    }

    public int getDamage() {
        return damage;
    }

}
