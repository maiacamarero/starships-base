package starships;

import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfiguration{
    private final int amountOfPlayers;
    private final Map<String, KeyCode> keyboardConfig;
    private final int amountOfLives;

    public GameConfiguration() {
        List<String> lines = getLines("/Users/maiacamarero/IdeaProjects/starships-base/app/src/main/java/starships/txtFiles/GameConfiguration");
        Map<String, String> map = generateMap(lines);
        this.amountOfPlayers = Integer.parseInt(map.get("amountOfPlayers"));
        this.keyboardConfig = getKeyBoardMap(map.get("keyBoardSettings"));
        this.amountOfLives = Integer.parseInt(map.get("amountOfLives"));
    }

    public List<String> getLines(String directory) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory))){
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private Map<String, KeyCode> getKeyBoardMap(String keyBoardSettings) {
        Map<String, KeyCode> mapToReturn = new HashMap<>();
        if (keyBoardSettings != null){
            String[] split = keyBoardSettings.split(";");
            for (String s : split){
                String[] innerSplit = s.split("=");
                mapToReturn.put(innerSplit[0], getKeyCode(innerSplit[1]));
            }
        }
        return mapToReturn;
    }

    private KeyCode getKeyCode(String s) {
        return switch (s){
            case "W" -> KeyCode.W;
            case "S" -> KeyCode.S;
            case "A" -> KeyCode.A;
            case "D" -> KeyCode.D;
            case "Q" -> KeyCode.Q;
            case "E" -> KeyCode.E;
            case "SPACE" -> KeyCode.SPACE;
            case "UP" -> KeyCode.UP;
            case "DOWN" -> KeyCode.DOWN;
            case "LEFT" -> KeyCode.LEFT;
            case "RIGHT" -> KeyCode.RIGHT;
            case "NUMPAD1" -> KeyCode.NUMPAD1;
            case "NUMPAD2" -> KeyCode.NUMPAD2;
            case "ENTER" -> KeyCode.ENTER;
            default -> KeyCode.M;
        };
    }

    private Map<String, String> generateMap(List<String> lines) {
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
