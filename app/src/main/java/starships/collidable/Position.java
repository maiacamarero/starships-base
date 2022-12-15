package starships.collidable;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position sum(Position position){
        return new Position(this.x + position.getX(), this.y + position.getY());
    }
}
