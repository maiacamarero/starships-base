package starships;

public class Health {
    private final int value;

    public Health(int value) {
        this.value = value;
    }

    public Health reduce(int value){
        return new Health(this.value - value);
    }
}
