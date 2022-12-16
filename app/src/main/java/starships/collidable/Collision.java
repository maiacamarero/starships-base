package starships.collidable;

import starships.Game;
import starships.Player;
import starships.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Collision {
    public static State handleCollision(Colisionable colisionable, Colisionable otherColisionable, State state, Game game) {
        List<Colisionable> elements = state.getElements();
        List<Player> players = state.getPlayers();
        State newState = null;
        if (colisionable.getCollidableType().equals(CollidableType.SHIP) && otherColisionable.getCollidableType().equals(CollidableType.ASTEROID)) {
            newState = manageShipAsteroidCollision(colisionable, otherColisionable, elements, players, game);
        } else if (colisionable.getCollidableType().equals(CollidableType.SHIP) && otherColisionable.getCollidableType().equals(CollidableType.BULLET)) {
            newState = manageBulletShipCollision(colisionable, otherColisionable, elements, players, game);
        } else if (colisionable.getCollidableType().equals(CollidableType.BULLET) && otherColisionable.getCollidableType().equals(CollidableType.ASTEROID)) {
            newState = manageBulletAsteroidCollision(colisionable, otherColisionable, elements, players, game);
        }
        if (newState == null) {
            return new State(game.getNewElements(), game.getNewPlayers());
        }
        return newState;
    }

    private static State manageBulletAsteroidCollision(Colisionable colisionable, Colisionable otherColisionable, List<Colisionable> elements, List<Player> players, Game game) {
        Bullet bullet;
        Asteroid asteroid;
        if (colisionable.getCollidableType() == CollidableType.BULLET){
            bullet = (Bullet) colisionable;
            asteroid = (Asteroid) otherColisionable;
        }else{
            bullet = (Bullet) otherColisionable;
            asteroid = (Asteroid) colisionable;
        }
        List<Colisionable> newElements = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player player = getPlayer(bullet.getShipId(), players, elements);
        Player newPlayer = null;
        for (Colisionable element : elements) {
            if (bullet.getId().equals(element.getId())) {
                game.addDeadElements(element.getId());
            } else if (element.getId().equals(asteroid.getId())) {
                Asteroid newAsteroid = new Asteroid(asteroid.getId(), asteroid.getPosition(), asteroid.getRotationInDegrees(), asteroid.getHeight(), asteroid.getWidth(), asteroid.getDirection(), asteroid.isClockwise(), asteroid.getInitialHealth(), asteroid.getCurrentHealth() - bullet.getDamage());
                if (newAsteroid.getCurrentHealth() <= 0) {
                    newPlayer = new Player(player.getPlayerId(), player.getPoints() + newAsteroid.getPoints(), player.getHealth(), player.getShipId());
                    game.addPoints(newPlayer.getPlayerId(), newAsteroid.getPoints());
                    game.addDeadElements(element.getId());
                } else {
                    newElements.add(newAsteroid);
                    newPlayer = new Player(player.getPlayerId(), player.getPoints(), player.getHealth(), player.getShipId());
                }
            } else {
                newElements.add(element.getNewElementColisionable());
            }
        }
        for (Player player1 : players) {
            if (player1.getPlayerId().equals(player.getPlayerId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }else {
                newPlayers.add(player1.getNewPlayer());
            }
        }
        return new State(newElements, newPlayers);
    }

    private static State manageShipAsteroidCollision(Colisionable colisionable, Colisionable otherColisionable, List<Colisionable> elements, List<Player> players, Game game) {
        Ship ship;
        Asteroid asteroid;
        if (colisionable.getCollidableType() == CollidableType.SHIP){
            ship = (Ship) colisionable;
            asteroid = (Asteroid) otherColisionable;
        }else {
            ship = (Ship) otherColisionable;
            asteroid = (Asteroid) colisionable;
        }
        List<Colisionable> newElements = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player player = getPlayer(ship.getPlayerId(), players, elements);
        Player newPlayer = null;
        for (Colisionable element : elements){
            if (ship.getId().equals(element.getId())){
                newPlayer = new Player(player.getPlayerId(), player.getPoints(), player.getHealth().reduce(-1), player.getShipId());
                if (newPlayer.getHealth().getValue() > 0){
                    newElements.add(new Ship(ship.getId(), new Position(300, 300), 180, ship.getHeight(), ship.getWidth(), ship.getPlayerId(), ship.getLastBulletShot(), 180, 0, ship.getBulletType()));
                }else {
                    game.addDeadElements(element.getId());
                    newPlayer = null;
                }
            }else if (element.getId().equals(asteroid.getId())){
                game.addDeadElements(element.getId());
            }else {
                newElements.add(element.getNewElementColisionable());
            }
        }
        for(Player player1 : players){
            if (player1.getPlayerId().equals(player.getPlayerId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }else {
                newPlayers.add(player1.getNewPlayer());
            }
        }
        if (newPlayers.isEmpty()) game.finishGame();
        return new State(newElements, newPlayers);
    }

    private static State manageBulletShipCollision(Colisionable colisionable, Colisionable otherColisionable, List<Colisionable> elements, List<Player> players, Game game) {
        Bullet bullet;
        Ship ship;
        if (colisionable.getCollidableType() == CollidableType.BULLET){
            bullet = (Bullet) colisionable;
            ship = (Ship) otherColisionable;
        }else {
            bullet = (Bullet) otherColisionable;
            ship = (Ship) colisionable;
        }
        if (Objects.equals(ship.getId(), bullet.getShipId())) return null;
        List<Colisionable> newGameObjects = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(ship.getPlayerId(), players);
        Player newPlayer = null;
        for (Colisionable element : elements){
            if (bullet.getId().equals(element.getId())){
                game.addDeadElements(element.getId());
            } else if (element.getId().equals(ship.getId())) {
                assert shipPlayer != null;
                newPlayer = new Player(shipPlayer.getPlayerId(),shipPlayer.getPoints(), new Health(shipPlayer.getHealth().reduce( 1).getValue()), shipPlayer.getShipId());
                if (newPlayer.getHealth().getValue() > 0){
                    //reset position
                    newGameObjects.add(new Ship(ship.getId(),new Position(300, 300),180, ship.getHeight(),ship.getWidth(),ship.getPlayerId(),ship.getLastBulletShot(),180,0, ship.getBulletType()));
                } else{
                    game.addDeadElements(ship.getId());
                }
            } else{
                newGameObjects.add(element.getNewElementColisionable());
            }
        }
        for (Player player : players){
            assert shipPlayer != null;
            if (player.getPlayerId().equals(shipPlayer.getPlayerId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }
            else {
                newPlayers.add(player.getNewPlayer());
            }
        }
        if (newPlayers.isEmpty()) game.finishGame();
        return new State(newGameObjects, newPlayers);
    }

    private static Player getPlayer(String playerId, List<Player> players) {
        for (Player player : players) {
            if (playerId.equals(player.getPlayerId())) {
                return player;
            }
        }
        return null;
    }

    private static Player getPlayer(String shipId, List<Player> players, List<Colisionable> colisionables) {
        String playerId = "";
        for (Colisionable value : colisionables) {
            if (value.getCollidableType() == CollidableType.SHIP && Objects.equals(value.getId(), shipId)) {
                Ship ship = (Ship) value;
                playerId = ship.getPlayerId();
            }
        }
        return getPlayer(playerId, players);
    }
}
