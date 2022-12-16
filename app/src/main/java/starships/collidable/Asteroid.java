package starships.collidable;

public class Asteroid extends Colisionable {

    private boolean clockwise;
    private int initialHealth;
    private int currentHealth;

    public Asteroid(String id, Position position, double rotationInDegrees, double height, double width, double direction, boolean clockwise, int initialHealth, int currentHealth) {
        super(id, CollidableType.ASTEROID, position, rotationInDegrees, height, width, CollidableShape.ELLIPTICAL, direction);
        this.clockwise = clockwise;
        this.initialHealth = initialHealth;
        this.currentHealth = currentHealth;
    }

    @Override
    public Colisionable update(){
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
    public Colisionable getNewElementColisionable() {
        return new Asteroid(getId(), getPosition(), getRotationInDegrees(), getHeight(), getWidth(), getDirection(), clockwise, initialHealth, currentHealth);
    }

    private Colisionable move() {
        int newX = (int) (getPosition().getX() + 0.7 * Math.sin(Math.PI * 2 * getDirection() / 360));
        int newY = (int) (getPosition().getY() + 0.7 * Math.cos(Math.PI * 2 * getDirection() / 360));
        double newRotationInDegrees;
        Position newPosition = new Position(newX, newY);
        if (clockwise) {
            newRotationInDegrees = getRotationInDegrees() + 2;
        } else {
            newRotationInDegrees = getRotationInDegrees() - 2;
        }
        return new Asteroid(getId(), newPosition, newRotationInDegrees, getHeight(), getWidth(), getDirection(), clockwise, initialHealth, currentHealth);
    }

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }

    public double calculateHealth(){
        return (this.getWidth() * this.getHeight())/100;
    }

    public void takeDamage(int amount){
        this.currentHealth -= amount;
    }

    public void setInitialHealth(int healthBar) {
        this.initialHealth = healthBar;
        this.currentHealth = healthBar;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public int getInitialHealth() {
        return initialHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getPoints(){
        return initialHealth;
    }
}
