package starships.collidable;

public class Vector {
    private final int x;
    private final int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector sum(Vector vector){
        return new Vector(this.x + vector.getX(), this.y + vector.getY());
    }
}
