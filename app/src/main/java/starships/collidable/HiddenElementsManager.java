package starships.collidable;

public class HiddenElementsManager {
    private static int x = 1000;
    private static int y = 0;

    public static int[] getEmptyPlace(){
        if (y == 1000){
            x += 50;
            y = 0;
        }
        int[] emptySpace = {x, y};
        y += 50;
        return emptySpace;
    }
}
