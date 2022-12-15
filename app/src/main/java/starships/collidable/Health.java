package starships.collidable;

public class Health {
    private final int value;

    public Health(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Health reduce(int value){
        return new Health(this.value - value);
    }
}
