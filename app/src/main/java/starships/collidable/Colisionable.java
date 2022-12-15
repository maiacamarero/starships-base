package starships.collidable;

public abstract class Colisionable {

    private final String id;
    private boolean isHiding;
    private Vector position;
    private final int rotationInDegrees;
    private final int direction;
    private final int height;
    private final int width;
    //private final Health health;
    private final CollidableType collidableType;
    private final CollidableShape collidableShape;

    public Colisionable(String id, CollidableType collidableType, Vector position, int rotationInDegrees, int height, int width, CollidableShape collidableShape, int direction) {
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

//    public Colisionable setId(String id){
//        return new Colisionable(id, this.collidableType, this.position, this.rotationInDegrees, this.isHiding, this.height, this.width, this.collidableShape, this.direction);
//    }
//
//    public Colisionable setPosition(Vector position){
//        return new Colisionable(this.id, this.collidableType, position, this.rotationInDegrees, this.isHiding, this.height, this.width, this.collidableShape, this.direction);
//    }
//
//    public Colisionable setRotationInDegrees(int rotationInDegrees){
//        return new Colisionable(this.id, this.collidableType, this.position, rotationInDegrees, this.isHiding, this.height, this.width, this.collidableShape, this.direction);
//    }
//
//    public Colisionable setCollidableType(CollidableType collidableType){
//        return new Colisionable(this.id, collidableType, this.position, this.rotationInDegrees, this.isHiding, this.height, this.width, this.collidableShape, this.direction);
//    }
//
//    public Colisionable setCollidableShape(CollidableShape collidableShape){
//        return new Colisionable(this.id, this.collidableType, this.position, this.rotationInDegrees, this.isHiding, this.height, this.width, collidableShape, this.direction);
//    }
//
//    public Colisionable setHeight(int height){
//        return new Colisionable(this.id, this.collidableType, this.position, this.rotationInDegrees, this.isHiding, height, this.width, this.collidableShape, this.direction);
//    }
//
//    public Colisionable setWidth(int width){
//        return new Colisionable(this.id, this.collidableType, this.position, this.rotationInDegrees, this.isHiding, this.height, width, this.collidableShape, this.direction);
//    }
//
//    public Colisionable setDirection(int direction){
//        return new Colisionable(this.id, this.collidableType, this.position, this.rotationInDegrees, this.isHiding, this.height, this.width, this.collidableShape, direction);
//
//    }



    public String getId() {
        return id;
    }

    public Vector getPosition() {
        return position;
    }

    public int getRotationInDegrees() {
        return rotationInDegrees;
    }

    public CollidableType getCollidableType() {
        return collidableType;
    }

    public CollidableShape getCollidableShape() {
        return collidableShape;
    }

    public int getDirection() {
        return direction;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHiding() {
        return isHiding;
    }

    public void setHiding(boolean hiding) {
        isHiding = hiding;
    }

    public void hide(){
        int[] pos = HiddenElementsManager.getEmptyPlace();
        this.position = new Vector(pos[0], pos[1]);
        this.isHiding = true;
    }

    public int getWidth() {
        return width;
    }

    public boolean isInBounds(Vector position){
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
