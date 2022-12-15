package starships;

import starships.collidable.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Setup extends Configuration{

    public void saveGame(State state){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getDirectory()))){
            for (Colisionable element : state.getElements()) {
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
        return "id:" + player.getPlayerId() + ";points:" + player.getPoints() + ";lives:" + player.getHealth() ;
    }

    private String getDirectory() {
        return "app\\src\\main\\java\\starships\\configuration";
    }

    private String getStringToWriteColisionable(Colisionable colisionable) {
        String str = "id:" + colisionable.getId() + ";type:" + colisionable.getCollidableType().toString() + ";xPosition:" + colisionable.getPosition().getX() + ";yPosition:" + colisionable.getPosition().getY() + ";rotation:" + colisionable.getRotationInDegrees() + ";direction:" + colisionable.getDirection()
                + ";height:" + colisionable.getHeight() + ";width:" + colisionable.getWidth() + ";shape:" + colisionable.getCollidableShape();
        return str + parameters(colisionable);
    }

    private String parameters(Colisionable colisionable) {
        switch (colisionable.getCollidableType()){
            case SHIP -> {
                Ship ship = (Ship) colisionable;
                return ";lastBulletShot:" + ship.getLastBulletShot() + ";playerId:" + ship.getPlayerId() + ";boost:" + ship.getBoost() + ";bulletType:" + ship.getBulletType();
            }
            case BULLET -> {
                Bullet bullet = (Bullet) colisionable;
                return ";shipId:" + bullet.getShipId() + ";damage:" + bullet.getDamage() + ";bulletType:" + bullet.getBulletType();
            }
            case ASTEROID -> {
                Asteroid asteroid = (Asteroid) colisionable;
                return ";clockwise:" + asteroid.isClockwise() + ";initialHealth:" + asteroid.getInitialHealth() + ";currentHealth:" + asteroid.getCurrentHealth();
            }
        };
        return "";
    }

    public State getSavedGameState(){
        List<String> configLines = getLines(getDirectory());
        List<String> stringElements = null;
        List<String> stringPlayers = null;
        for (int i = 0; i < configLines.size(); i++) {
            String line = configLines.get(i);
            stringElements = configLines.subList(0, i);
            stringPlayers = configLines.subList(i+1, configLines.size());
            break;
        }
        List<Colisionable> elements = getSavedElements(stringElements);
        assert stringPlayers != null;
        List<Player> players = getSavedPlayers(stringPlayers);
        return new State(elements, players);
    }

    private List<Colisionable> getSavedElements(List<String> stringElements) {
        List<Colisionable> elements = new ArrayList<>();
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
            String shape = (String) transform(s[8]);
            String color = (String) transform(s[9]);
            elements.add(createElement(s, id, type, xPosition, yPosition, rotation, direction, height, width));
        }
        return elements;
    }

    private Colisionable createElement(String[] s, String id, String type, int xPosition, int yPosition, int rotation, int direction, int height, int width) {
        switch (type){
            case "SHIP" -> {
                long lastBulletShot = (long) transform(s[10]);
                String playerId = (String) transform(s[11]);
                double boost = (double) transform(s[12]);
                String bulletType = (String) transform(s[13]);
                Vector position = new Vector(xPosition, yPosition);
                return new Ship(id, position, rotation, height, width, playerId, lastBulletShot, direction, boost, getBulletType(bulletType));
            }
            case "ASTEROID" -> {
                boolean clockwise = (boolean) transform(s[10]);
                int initialHealth = (int) transform(s[11]);
                int currentHealth = (int) transform(s[12]);
                Vector position = new Vector(xPosition, yPosition);
                return new Asteroid(id, position, rotation, height, width, direction, clockwise, initialHealth, currentHealth);
            }
            case "BULLET" -> {
                String shipId = (String) transform(s[10]);
                int damage = (int) transform(s[11]);
                String bulletType = (String) transform(s[12]);
                Vector position = new Vector(xPosition, yPosition);
                return new Bullet(id, position, rotation, height, width, direction, shipId, damage, getBulletType(bulletType));
            }
        }
        return null;    }

    private BulletType getBulletType(String bulletType) {
        return BulletType.BULLET;
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
