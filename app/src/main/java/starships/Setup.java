package starships;

import starships.collidable.*;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Setup{

    public static List<String> getLines(String directory) {
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

    public void saveGame(State state){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getDirectory()))){
            for (Collidable element : state.getElements()) {
                String toWrite = getStringToWriteColisionable(element);
                writer.write(toWrite + "\n");
            }
            writer.write("%\n");
            for (Player player : state.getPlayers()) {
                String toWrite = getStringToWritePlayer(player);
                writer.write(toWrite + "\n");
            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringToWritePlayer(Player player) {
        return "id:" + player.getPlayerId() + ";points:" + player.getPoints() + ";lives:" + player.getHealth().getValue() ;
    }

    private String getDirectory() {
        return "/Users/maiacamarero/IdeaProjects/starships-base/app/src/main/java/starships/txtFiles/config";
    }

    private String getStringToWriteColisionable(Collidable collidable) {
        String str = "id:" + collidable.getId() + ";type:" + collidable.getCollidableType().toString() + ";xPosition:" + collidable.getPosition().getX() + ";yPosition:" + collidable.getPosition().getY() + ";rotation:" + collidable.getRotationInDegrees() + ";direction:" + collidable.getDirection()
                + ";height:" + collidable.getHeight() + ";width:" + collidable.getWidth() + ";shape:" + collidable.getCollidableShape();
        return str + parameters(collidable);
    }

    private String parameters(Collidable collidable) {
        switch (collidable.getCollidableType()){
            case SHIP -> {
                Ship ship = (Ship) collidable;
                return ";lastBulletShot:" + ship.getLastBulletShot() + ";playerId:" + ship.getPlayerId() + ";boost:" + ship.getAccelerate() + ";bulletType:" + ship.getBulletType();
            }
            case BULLET -> {
                Bullet bullet = (Bullet) collidable;
                return ";shipId:" + bullet.getShipId() + ";damage:" + bullet.getDamage() + ";bulletType:" + bullet.getBulletType();
            }
            case ASTEROID -> {
                Asteroid asteroid = (Asteroid) collidable;
                return ";clockwise:" + asteroid.isClockwise() + ";initialHealth:" + asteroid.getInitialHealth() + ";currentHealth:" + asteroid.getCurrentHealth();
            }
        };
        return "";
    }

    public State getSavedState(){
        List<String> configLines = getLines(getDirectory());
        List<String> stringElements = null;
        List<String> stringPlayers = null;
        for (int i = 0; i < configLines.size(); i++) {
            stringElements = configLines.subList(0, i);
            stringPlayers = configLines.subList(i+1, configLines.size());
            break;
        }
        List<Collidable> elements = getSavedElements(stringElements);
        List<Player> players = getSavedPlayers(stringPlayers);
        return new State(elements, players);
    }

    private List<Collidable> getSavedElements(List<String> stringElements) {
        List<Collidable> elements = new ArrayList<>();
        for (String stringElement : stringElements){
            String[] s = stringElement.split(";");
            String id = (String) transform(s[0]);
            String type = (String) transform(s[1]);
            int xPosition = (int) transform(s[2]);
            int yPosition = (int) transform(s[3]);
            int rotation = (int) transform(s[4]);
            int direction = (int) transform(s[5]);
            int height = (int) transform(s[6]);
            int width = (int) transform(s[7]);
            Position position = new Position(xPosition, yPosition);
            elements.add(createElement(s, id, type, position, rotation, direction, height, width));
        }
        return elements;
    }

    private Collidable createElement(String[] s, String id, String type, Position position, int rotation, int direction, int height, int width) {
        switch (type){
            case "SHIP" -> {
                long lastBulletShot = (long) transform(s[10]);
                String playerId = (String) transform(s[11]);
                double boost = (double) transform(s[12]);
                return new Ship(id, position, rotation, height, width, playerId, lastBulletShot, direction, boost, getBulletType());
            }
            case "ASTEROID" -> {
                boolean clockwise = (boolean) transform(s[10]);
                int initialHealth = (int) transform(s[11]);
                int currentHealth = (int) transform(s[12]);
                return new Asteroid(id, position, rotation, height, width, direction, clockwise, new Health(initialHealth), new Health(currentHealth));
            }
            case "BULLET" -> {
                String shipId = (String) transform(s[10]);
                int damage = (int) transform(s[11]);
                return new Bullet(id, position, rotation, height, width, direction, shipId, damage, getBulletType());
            }
        }
        return null;    }

    private BulletType getBulletType() {
        return BulletType.LIGHTNING;
    }

    private List<Player> getSavedPlayers(List<String> stringPlayers) {
        List<Player> players = new ArrayList<>();
        for (String stringPlayer : stringPlayers) {
            String[] s = stringPlayer.split(";");
            String id = (String) transform(s[0]);
            int points = (int) transform(s[1]);
            int lives = (int) transform(s[2]);
            String shipId = (String) transform(s[3]);
            Player player = new Player(id, points, new Health(lives), shipId);
            players.add(player);
        }
        return players;
    }

    private static Object transform(String line){
        String[] str = line.split(":");
        String variable = str[0];
        String value = str[1];
        return switch (variable){
            case "id", "playerId", "type", "shape", "shipId", "bulletType" -> value;
            case "points", "lives", "initialHealth", "currentHealth", "damage" -> Integer.parseInt(value);
            case "clockwise" -> Boolean.parseBoolean(value);
            case "xPosition", "yPosition", "rotation", "direction", "height", "width", "boost" -> Double.parseDouble(value);
            case "lastBulletShot" -> Long.parseLong(value);
            default -> "";
        };
    }
}
