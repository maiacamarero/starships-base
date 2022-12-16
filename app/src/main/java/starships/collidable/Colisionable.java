package starships.collidable;

public abstract class Colisionable {

    private final String id;
    private Position position;
    private final double rotationInDegrees;
    private final double direction;
    private final double height;
    private final double width;
    private final CollidableType collidableType;
    private final CollidableShape collidableShape;

    public Colisionable(String id, CollidableType collidableType, Position position, double rotationInDegrees, double height, double width, CollidableShape collidableShape, double direction) {
        this.id = id;
        this.position = position;
        this.rotationInDegrees = rotationInDegrees;
        this.collidableType = collidableType;
        this.direction = direction;
        this.height = height;
        this.width = width;
        this.collidableShape = collidableShape;
    }

    public abstract Colisionable update();
    public abstract Colisionable getNewElementColisionable();

    public String getId() {
        return id;
    }

    public Position getPosition() {
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

    public double getDirection() {
        return direction;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public boolean isInBounds(Position position){
        if (position.getX() > 0 && position.getX() < 800 && position.getY() > 0 && position.getY() < 800){
            return true;
        }else return false;
    }

    public boolean isInBounds(){
        if (getPosition().getX() > 0 && getPosition().getX() < 800 && getPosition().getY() > 0 && getPosition().getY() < 800){
            return true;
        }else return false;
    }

}
