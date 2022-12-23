package starships.collidable.elements;

import starships.collidable.*;

public class Bullet extends Collidable {

    private final String shipId;
    private final int damage;
    private final BulletType bulletType;

    public Bullet(String id, Vector position, double rotationInDegrees, double height, double width, Vector direction, String shipID, int damage, BulletType bulletType, double speed, boolean isVisible) {
        super(id, CollidableType.BULLET, position, rotationInDegrees, height, width, CollidableShape.RECTANGULAR, direction, speed, new Health(1), isVisible);
        this.shipId = shipID;
        this.damage = damage;
        this.bulletType = bulletType;
    }

    public String getShipId(){
        return this.shipId;
    }

    public Bullet update(){
        //if (isVisible()){
            //if (getSpeed() > 0){
                int newX = (getPosition().getX() + getDirection().getX());
                int newY = (getPosition().getY() - getDirection().getY());
                if (isInBounds()){
                    return setPosition(new Vector(getPosition().getX() +10, getPosition().getY() + 10));
                }//else return setIsVisible(false);
            //}
       // }
        return this;
    }
    public boolean isInBounds(){
        return getPosition().getX() > (0-getWidth()) && getPosition().getX() < (1000+getWidth()) && getPosition().getY() > (0-getHeight()) && getPosition().getY() < (1000+getHeight());
    }

    public Bullet setPosition(Vector position){
        return new Bullet(getId(), getPosition().sum(position), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), shipId, getDamage(), bulletType, getSpeed(), isVisible());
    }
    public Bullet setIsVisible(boolean isVisible){
        return new Bullet(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), shipId, getDamage(), bulletType, getSpeed(), isVisible);
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

    public int getDamage() {
        return damage;
    }

}
