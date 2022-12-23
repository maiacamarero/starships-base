package starships.collidable.elements;

import starships.collidable.*;

public class Asteroid extends Collidable {

    private final boolean clockwise;
    private final Health initialHealth;
    private final Health currentHealth;

    public Asteroid(String id, Vector position, double rotationInDegrees, double height, double width, Vector direction, double speed, Health health, boolean isVisible, boolean clockwise, Health initialHealth, Health currentHealth) {
        super(id, CollidableType.ASTEROID, position, rotationInDegrees, height, width, CollidableShape.ELLIPTICAL, direction, speed, health, isVisible);
        this.clockwise = clockwise;
        this.initialHealth = initialHealth;
        this.currentHealth = currentHealth;
    }

    public Asteroid update(){
        int newX = (int) (getPosition().getX() - 4 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
        int newY = (int) (getPosition().getY() + 4 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
//        int newX = (int) (getPosition().getX() + getSpeed() * getDirection().getX());
//        int newY = (int) (getPosition().getY() + getSpeed() *  getDirection().getY());
        double newRotationInDegrees = 0;
        Vector newPosition = new Vector(newX, newY);

        if (isInBounds()){
            if (isClockwise()) {
                newRotationInDegrees = getRotationInDegrees() + 2;
            } else {
                newRotationInDegrees = getRotationInDegrees() - 2;
            }
        }else {
             return this.setPosition(new Vector(-100, -100));
        }
        return new Asteroid(getId(), newPosition, newRotationInDegrees, getHeight(), getWidth(), getDirection(), getSpeed(), getHealth(), isVisible(), clockwise, initialHealth, currentHealth);

    }

    public Asteroid setIsVisible(boolean isVisible){
        return new Asteroid(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), getSpeed(), getHealth(), isVisible, clockwise, getInitialHealth(), getCurrentHealth());
    }

    public Asteroid setRotation( double rotation){
        return new Asteroid(getId(), getPosition(), getRotationInDegrees() + rotation, getHeight(), getWidth(), getDirection(), getSpeed(), getHealth(), getNewElementCollidable().isVisible(), clockwise, getInitialHealth(), getCurrentHealth());
    }
    public Asteroid setPosition( Vector position){
        return new Asteroid(getId(), position, getRotationInDegrees(), getHeight(), getWidth(), getDirection(), getSpeed(), getHealth(), getNewElementCollidable().isVisible(), clockwise, getInitialHealth(), getCurrentHealth());
    }

    private boolean runOut(int shiftx, int shifty) {
        return !isInsideLimit(getPosition().getX()+shiftx, getPosition().getY()+shifty);
    }

    public boolean isInBounds(){
        return getPosition().getX() > (0-getWidth()) && getPosition().getX() < (1000+getWidth()) && getPosition().getY() > (0-getHeight()) && getPosition().getY() < (1000+getHeight());
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public Health getInitialHealth() {
        return initialHealth;
    }

    public Health getCurrentHealth() {
        return currentHealth;
    }

    public int getPoints(){
        return initialHealth.getValue();
    }
}
