package starships;

public class Collidable {
    private final Vector position;
    private final Vector speed;
    private final Vector acelec;
    private final int rotationInDegrees;
    private final Health health;
    private final Damage damage;
    private final CollidableType collidableType; //ship asteroid bullet tambien sirve como id

    public Collidable(Vector position, Vector speed, Vector acelec, int rotationInDegrees, Health health, Damage damage, CollidableType collidableType) {
        this.position = position;
        this.speed = speed;
        this.acelec = acelec;
        this.rotationInDegrees = rotationInDegrees;
        this.health = health;
        this.damage = damage;
        this.collidableType = collidableType;
    }

    public Collidable setPosition(Vector position) {
        return new Collidable(position, this.speed, this.acelec, this.rotationInDegrees, this.health, this.damage, this.collidableType);
    }

    public Collidable setSpeed(Vector speed) {
        return new Collidable(this.position, speed, this.acelec, this.rotationInDegrees, this.health, this.damage, this.collidableType);
    }

    public Collidable setAcelec(Vector acelec) {
        return new Collidable(this.position, this.speed, acelec, this.rotationInDegrees, this.health, this.damage, this.collidableType);
    }

    public Collidable setRotationInDegrees(int rotationInDegrees) {
        return new Collidable(this.position, this.speed, this.acelec, rotationInDegrees, this.health, this.damage, this.collidableType);
    }

    public Collidable setHealth(Health health) {
        return new Collidable(this.position, this.speed, this.acelec, this.rotationInDegrees, health, this.damage, this.collidableType);
    }

    public Collidable setDamage(Damage damage) {
        return new Collidable(this.position, this.speed, this.acelec, this.rotationInDegrees, this.health, damage, this.collidableType);
    }

    public Collidable setCollidableType(CollidableType collidableType) {
        return new Collidable(this.position, this.speed, this.acelec, this.rotationInDegrees, this.health, this.damage, collidableType);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }

    public Vector getAcelec() {
        return acelec;
    }

    public int getRotationInDegrees() {
        return rotationInDegrees;
    }

    public CollidableType getCollidableType() {
        return collidableType;
    }

    public Health getHealth() {
        return health;
    }

    public Damage getDamage() {
        return damage;
    }
}
