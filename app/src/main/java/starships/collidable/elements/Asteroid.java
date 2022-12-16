package starships.collidable.elements;

import starships.collidable.*;

public class Asteroid extends Collidable {

    private final boolean clockwise;
    private final Health initialHealth;
    private final Health currentHealth;

    public Asteroid(String id, Position position, double rotationInDegrees, double height, double width, double direction, boolean clockwise, Health initialHealth, Health currentHealth) {
        super(id, CollidableType.ASTEROID, position, rotationInDegrees, height, width, CollidableShape.ELLIPTICAL, direction);
        this.clockwise = clockwise;
        this.initialHealth = initialHealth;
        this.currentHealth = currentHealth;
    }

    @Override
    public Collidable update(){
        if (isOutOfBounds(3, 3)){
            return null;
        }return move();
    }

    private boolean isOutOfBounds(int shift1, int shift2){
        if (!isInBounds(new Position(getPosition().getX() + shift1, getPosition().getY() + shift2))){
            return true;
        }else return false;
    }

    @Override
    public Collidable getNewElementColisionable() {
        return new Asteroid(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), clockwise, initialHealth, currentHealth);
    }

    private Collidable move() {
        int newX = (int) (getPosition().getX() - 4 * Math.sin(Math.PI * 2 * getDirection() / 360));
        int newY = (int) (getPosition().getY() + 4 * Math.cos(Math.PI * 2 * getDirection() / 360));
        double newRotationInDegrees;
        Position newPosition = new Position(newX, newY);
        if (clockwise) {
            newRotationInDegrees = getRotationInDegrees() + 2;
        } else {
            newRotationInDegrees = getRotationInDegrees() - 2;
        }
        return new Asteroid(getId(), newPosition, newRotationInDegrees, getHeight(), getWidth(), getDirection(), clockwise, initialHealth, currentHealth);
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
