package starships.collidable;

import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Ship;

public class Collidable {

    private final String id;
    private final Vector position;
    private final double rotationInDegrees;
    private final Vector direction;
    private final double height;
    private final double speed;
    private final double width;
    private final CollidableType collidableType;
    private final CollidableShape collidableShape;
    private final Health health;
    private final boolean isVisible;

    public Collidable(String id, CollidableType collidableType, Vector position, double rotationInDegrees, double height, double width, CollidableShape collidableShape, Vector direction, double speed, Health health, boolean isVisible) {
        this.id = id;
        this.position = position;
        this.rotationInDegrees = rotationInDegrees;
        this.collidableType = collidableType;
        this.direction = direction;
        this.height = height;
        this.width = width;
        this.collidableShape = collidableShape;
        this.speed = speed;
        this.health = health;
        this.isVisible = isVisible;
    }


    //    public abstract Collidable update();
//    public abstract Collidable getNewElementCollidable();

    public Collidable setId(String id){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setCollidableType(CollidableType collidableType){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setPosition(Vector position){
        return new Collidable(id, collidableType, this.position.sum(position), rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setRotationInDegrees(double rotationInDegrees){
        return new Collidable(id, collidableType, position, this.rotationInDegrees + rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setHeight(double height){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setWidth(double width){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setCollidableShape(CollidableShape collidableShape){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }
    public Collidable setDirection(Vector direction){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, this.direction.sum(direction), speed, health, isVisible);
    }
    public Collidable setSpeed(double speed){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, this.speed + speed, health, isVisible);
    }
    public Collidable setDirectionSpeed(Vector direction, double speed){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, this.direction.sum(direction), this.speed + speed, health, isVisible);

    }
    public Collidable setHealth(Health health){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, new Health(this.health.getValue() + health.getValue()), isVisible);
    }
    public Collidable setIsVisible(boolean isVisible){
        return new Collidable(id, collidableType, position, rotationInDegrees, height, width, collidableShape, direction, speed, health, isVisible);
    }

    public String getId() {
        return id;
    }
    public Vector getPosition() {
        return position;
    }
    public double getRotationInDegrees() {
        return rotationInDegrees;
    }
    public CollidableType getCollidableType() {
        return collidableType;
    }
    public CollidableShape getCollidableShape() {
        return collidableShape;
    }
    public Vector getDirection() {
        return direction;
    }
    public double getHeight() {
        return height;
    }
    public double getWidth() {
        return width;
    }
    public double getSpeed() {
        return speed;
    }
    public Health getHealth() {
        return health;
    }
    public boolean isVisible() {
        return isVisible;
    }

    public boolean isInBounds(Vector position){
        if (position.getX() > 0 && position.getX() < 800 && position.getY() > 0 && position.getY() < 800){
            return true;
        }else return false;
    }

//    public boolean isInBounds(){
//        if (getPosition().getX() > 0 && getPosition().getX() < 800 && getPosition().getY() > 0 && getPosition().getY() < 800){
//            return true;
//        }else return false;
//    }

    public boolean isInBounds(){
        if ((getPosition().getX() > (0-getHeight())) && (getPosition().getX()<(800+getHeight())) && (getPosition().getY() > (0-getHeight())) && (getPosition().getX()<(800+getHeight()))){
            return true;
        }else return false;
    }

    public Collidable getNewElementCollidable() {
        return new Collidable(getId(), getCollidableType(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getCollidableShape(), getDirection(), getSpeed(), getHealth(), isVisible());
    }

    public Collidable update(){
        int newX = (int) (getPosition().getX() - 3.5 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
        int newY = (int) (getPosition().getY() + 3.5 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
        if (collidableType == CollidableType.SHIP){
            if (speed > 0 && !isInsideLimit(newX, newY)){
                    return setSpeed(0.0);
            }else return setSpeed(-5);

        }else if (collidableType == CollidableType.ASTEROID){
            int newX1 = (int) (getPosition().getX() - 4 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
            int newY1 = (int) (getPosition().getY() + 4 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
            double newRotationInDegrees;
            Vector newPosition = new Vector(newX1, newY1);
            newRotationInDegrees = getRotationInDegrees() - 2;
            this.setRotationInDegrees(newRotationInDegrees);
            this.setPosition(newPosition);
            return this;
        }else {
            if (isVisible()){
                if (speed > 0){
                    if (isInBounds()){
                        return setPosition(new Vector(newX, newY));
                    }else return setIsVisible(false);
                }
            }
        }
        return this;
    }

    public boolean isInsideLimit(int x, int y){
        return x > 0 && x < 725 && y > 0 && y < 700;
    }
    public boolean isInsideLimit(){
        return getPosition().getX() > 0 && getPosition().getX() < 800 && getPosition().getY() > 0 && getPosition().getY() < 800;
    }

}
