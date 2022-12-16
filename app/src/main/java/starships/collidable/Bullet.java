package starships.collidable;

public class Bullet extends Colisionable {

    private String ship;
    private int damage;

    private BulletType bulletType;

    public Bullet(String id, Position position, double rotationInDegrees, double height, double width, double direction, String shipID, int damage, BulletType bulletType) {
        super(id, CollidableType.BULLET, position, rotationInDegrees, height, width, CollidableShape.RECTANGULAR, direction);
        this.ship = shipID;
        this.damage = damage;
        this.bulletType = bulletType;
    }

    public String getShipId(){
        return this.ship;
    }

    @Override
    public Colisionable update(){
        if (!isInBounds()){
            return null;
        }return move();
    }

    @Override
    public Colisionable getNewElementColisionable() {
        return new Bullet(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), getShipId(), damage, bulletType);
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    private Bullet move() {
        int newX = (int) (getPosition().getX() - 4 * Math.sin(Math.PI * 2 * getDirection() / 360));
        int newY = (int) (getPosition().getY() + 4 * Math.cos(Math.PI * 2 * getDirection() / 360));
        Position newPosition = new Position(newX, newY);
        return new Bullet(getId(), newPosition,getRotationInDegrees(),getHeight(),getWidth(),getDirection(),ship, damage, bulletType);
    }

    public int getDamage() {
        return damage;
    }

}
