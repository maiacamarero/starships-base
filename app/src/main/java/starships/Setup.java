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

    public static void saveGame(State state){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getDirectory()))){
            for (Collidable element : state.getElements()) {
                String toWrite = getStringToWriteCollidable(element);
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

    private static String getStringToWritePlayer(Player player) {
        return "id:" + player.getPlayerId() + ";points:" + player.getPoints() + ";lives:" + player.getHealth().getValue() + ";shipId:" + player.getShipId() ;
    }

    private static String getDirectory() {
        return "/Users/maiacamarero/IdeaProjects/starships-base/app/src/main/java/starships/txtFiles/config";
    }

    private static String getStringToWriteCollidable(Collidable collidable) {
        String str = "id:" + collidable.getId()
                + ";type:" + collidable.getCollidableType().toString()
                + ";xPosition:" + collidable.getPosition().getX()
                + ";yPosition:" + collidable.getPosition().getY()
                + ";rotationInDegrees:" + collidable.getRotationInDegrees()
                + ";xDirection:" + collidable.getDirection().getX()
                + ";yDirection:" + collidable.getDirection().getY()
                + ";height:" + collidable.getHeight()
                + ";width:" + collidable.getWidth()
                + ";shape:" + collidable.getCollidableShape().toString()
                + ";health:" + collidable.getHealth().getValue()
                + ";speed:" + collidable.getSpeed();
        return str + parameters(collidable);
    }

    private static String parameters(Collidable collidable) {
        switch (collidable.getCollidableType()){
            case SHIP -> {
                Ship ship = (Ship) collidable;
                return ";lastBulletShot:" + ship.getLastBulletShot()
                        + ";playerId:" + ship.getPlayerId()
                        + ";bulletType:" + ship.getBulletType()
                        + ";amountOfShots:" + ship.getAmountOfShots();
            }
            case BULLET -> {
                Bullet bullet = (Bullet) collidable;
                return ";shipId:" + bullet.getShipId()
                        + ";damage:" + bullet.getDamage()
                        + ";bulletType:" + bullet.getBulletType();
            }
            case ASTEROID -> {
                Asteroid asteroid = (Asteroid) collidable;
                return ";clockwise:" + asteroid.isClockwise()
                        + ";initialHealth:" + asteroid.getInitialHealth()
                        + ";currentHealth:" + asteroid.getCurrentHealth();
            }
        };
        return "";
    }

    public static State getSavedState(){
        List<String> configLines = getLines(getDirectory());
        List<String> stringElements = null;
        List<String> stringPlayers = null;
        for (int i = 0; i < configLines.size(); i++) {
            stringElements = configLines.subList(0, 0);
            stringPlayers = configLines.subList(1, configLines.size());
            break;
        }
        assert stringElements != null;
        List<Collidable> elements = getSavedElements(stringElements);
        List<Player> players = getSavedPlayers(stringPlayers);
        return new State(elements, players);
    }

    private static List<Collidable> getSavedElements(List<String> stringElements) {
        List<Collidable> elements = new ArrayList<>();
        for (String stringElement : stringElements){
            String[] s = stringElement.split(";");
            String id = (String) transform(s[0]);
            String type = (String) transform(s[1]);
            int xPosition = (int) transform(s[2]);
            int yPosition = (int) transform(s[3]);
            int rotation = (int) transform(s[4]);
            int xDirection = (int) transform(s[5]);
            int yDirection = (int) transform(s[6]);
            int height = (int) transform(s[7]);
            int width = (int) transform(s[8]);
            String shape = (String) transform(s[9]);
            int health = (int) transform(s[10]);
            int speed = (int) transform(s[11]);

            Vector position = new Vector(xPosition, yPosition);
            Vector direction = new Vector(xDirection, yDirection);
            elements.add(createElement(s, id, type, position, rotation, direction, height, width, shape, health, speed));
        }
        return elements;
    }

    private static Collidable createElement(String[] s, String id, String type, Vector position, int rotationInDegrees, Vector direction, int height, int width, String shape, int health, int speed) {
        switch (type){
            case "SHIP" -> {
                long lastBulletShot = (long) transform(s[12]);
                String playerId = (String) transform(s[13]);
                String bulletType = (String) transform(s[14]);
                int amountOfShots = (int) transform(s[15]);
                return new Ship(id, position, rotationInDegrees, height, width, playerId, lastBulletShot, direction, speed, getBulletType(), true, new Health(health));
            }
            case "ASTEROID" -> {
                boolean clockwise = (boolean) transform(s[12]);
                int initialHealth = (int) transform(s[13]);
                int currentHealth = (int) transform(s[14]);
                return new Asteroid(id, position, rotationInDegrees, height, width, direction, speed, new Health(health), true, clockwise, new Health(initialHealth), new Health(currentHealth));
            }
            case "BULLET" -> {
                String shipId = (String) transform(s[12]);
                int damage = (int) transform(s[13]);
                return new Bullet(id, position, rotationInDegrees, height, width, direction, shipId, damage, getBulletType(), speed, true);
            }
        }
        return null;
    }

    private static BulletType getBulletType() {
        return BulletType.NORMAL;
    }

    private static List<Player> getSavedPlayers(List<String> stringPlayers) {
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
            case "xDirection", "yDirection","xPosition", "yPosition","points", "lives", "initialHealth", "currentHealth", "damage" -> Integer.parseInt(value);
            case "clockwise" -> Boolean.parseBoolean(value);
            case  "rotationInDegrees", "height", "width", "speed" -> Double.parseDouble(value);
            case "lastBulletShot" -> Long.parseLong(value);
            default -> "";
        };
    }
}
