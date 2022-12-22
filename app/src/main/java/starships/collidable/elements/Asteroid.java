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
        double newRotationInDegrees;
        Vector newVector = new Vector(newX, newY);
        if (clockwise) {
            newRotationInDegrees = getRotationInDegrees() + 2;
        } else {
            newRotationInDegrees = getRotationInDegrees() - 2;
        }
        return new Asteroid(getId(), newVector, newRotationInDegrees, getHeight(), getWidth(), getDirection(), getSpeed(), getHealth(), isVisible(), clockwise, initialHealth, currentHealth);

    }

//    private boolean runOut(int shiftx, int shifty) {
//        return isInsideLimit(getPosition().getX()+shiftx, getPosition().getY()+shifty);
//    }

//    private Collidable move() {
//        int newX = (int) (getPosition().getX() - 4 * Math.sin(Math.PI * 2 * getDirection().getX() / 360));
//        int newY = (int) (getPosition().getY() + 4 * Math.cos(Math.PI * 2 * getDirection().getY() / 360));
//        double newRotationInDegrees;
//        Vector newVector = new Vector(newX, newY);
//        if (clockwise) {
//            newRotationInDegrees = getRotationInDegrees() + 2;
//        } else {
//            newRotationInDegrees = getRotationInDegrees() - 2;
//        }
//        return new Asteroid(getId(), newVector, newRotationInDegrees, getHeight(), getWidth(), getDirection(),getSpeed(), getHealth(),isVisible(), clockwise, initialHealth, currentHealth);
//    }

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
