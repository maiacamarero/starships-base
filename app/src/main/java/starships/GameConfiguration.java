package starships;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfiguration extends Configuration{
    private final int amountOfPlayers;
    private final Map<String, KeyCode> keyboardConfig;
    private final int amountOfLives;

    public GameConfiguration() {
        List<String> lines = getLines("app\\src\\main\\java\\game\\config\\GameConfiguration");
        Map<String, String> map = getMap(lines);
        this.amountOfPlayers = Integer.parseInt(map.get("amountOfPlayers"));
        this.keyboardConfig = getKeyBoardMap(map.get("keyBoardSettings"));
        this.amountOfLives = Integer.parseInt(map.get("amountOfLives"));
        //this.shipColors = getShipColorMap(map.get("ships"));
    }

    private Map<String, KeyCode> getKeyBoardMap(String keyBoardSettings) {
        Map<String, KeyCode> mapToReturn = new HashMap<>();
        String[] split = keyBoardSettings.split(":");
        for (String s: split){
            String[] innerSplit = s.split("=");
            mapToReturn.put(innerSplit[0], getKeyCode(innerSplit[1]));
        }
        return mapToReturn;
    }

    private KeyCode getKeyCode(String s) {
        return switch (s){
            case "W" -> KeyCode.W;
            case "S" -> KeyCode.S;
            case "SPACE" -> KeyCode.SPACE;
            case "LEFT" -> KeyCode.LEFT;
            case "RIGHT" -> KeyCode.RIGHT;
            case "T" -> KeyCode.T;
            case "G" -> KeyCode.G;
            case "L" -> KeyCode.L;
            case "J" -> KeyCode.J;
            default -> KeyCode.K;
        };
    }

    private Map<String, String> getMap(List<String> lines) {
        Map<String, String> mapToReturn = new HashMap<>();
        for (String line: lines){
            String[] split = line.split(":");
            mapToReturn.put(split[0], split[1]);
        }
        return mapToReturn;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public Map<String, KeyCode> getKeyboardConfig() {
        return keyboardConfig;
    }

    public int getAmountOfLives() {
        return amountOfLives;
    }
}
