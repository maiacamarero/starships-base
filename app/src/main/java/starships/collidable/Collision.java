package starships.collidable;

import starships.Game;
import starships.Player;
import starships.State;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Collision {
    public static State handleCollision(Collidable collidable, Collidable otherCollidable, State state, Game game) {
        List<Collidable> elements = state.getElements();
        List<Player> players = state.getPlayers();
        State newState = null;
        if (collidable.getCollidableType().equals(CollidableType.SHIP) && otherCollidable.getCollidableType().equals(CollidableType.ASTEROID)) {
            newState = manageShipAsteroidCollision(collidable, otherCollidable, elements, players, game);
        } else if (collidable.getCollidableType().equals(CollidableType.SHIP) && otherCollidable.getCollidableType().equals(CollidableType.BULLET)) {
            newState = manageBulletShipCollision(collidable, otherCollidable, elements, players, game);
        } else if (collidable.getCollidableType().equals(CollidableType.BULLET) && otherCollidable.getCollidableType().equals(CollidableType.ASTEROID)) {
            newState = manageBulletAsteroidCollision(collidable, otherCollidable, elements, players, game);
        }
        if (newState == null) {
            return new State(game.getNewElements(), game.getNewPlayers());
        }
        return newState;
    }

    private static State manageBulletAsteroidCollision(Collidable collidable, Collidable otherCollidable, List<Collidable> elements, List<Player> players, Game game) {
        Bullet bullet;
        Asteroid asteroid;
        if (collidable.getCollidableType() == CollidableType.BULLET){
            bullet = (Bullet) collidable;
            asteroid = (Asteroid) otherCollidable;
        }else{
            bullet = (Bullet) otherCollidable;
            asteroid = (Asteroid) collidable;
        }
        List<Collidable> newElements = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player player = getPlayer(bullet.getShipId(), players, elements);
        Player newPlayer = null;
        for (Collidable element : elements) {
            if (bullet.getId().equals(element.getId())) {
                game.addDeadElements(element.getId());
            } else if (element.getId().equals(asteroid.getId())) {
                Asteroid newAsteroid = new Asteroid(asteroid.getId(), asteroid.getPosition(), asteroid.getRotationInDegrees(), asteroid.getHeight(), asteroid.getWidth(), asteroid.getDirection(), asteroid.isClockwise(), asteroid.getInitialHealth(), asteroid.getCurrentHealth().reduce(bullet.getDamage()));
                if (newAsteroid.getCurrentHealth().getValue() <= 0) {
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
                newPlayers.add(newPlayer);
            }else {
                newPlayers.add(player1.getNewPlayer());
            }
        }
        return new State(newElements, newPlayers);
    }

    private static State manageShipAsteroidCollision(Collidable collidable, Collidable otherCollidable, List<Collidable> elements, List<Player> players, Game game) {
        Ship ship;
        Asteroid asteroid;
        if (collidable.getCollidableType() == CollidableType.SHIP){
            ship = (Ship) collidable;
            asteroid = (Asteroid) otherCollidable;
        }else {
            ship = (Ship) otherCollidable;
            asteroid = (Asteroid) collidable;
        }
        List<Collidable> newElements = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player player = getPlayer(ship.getPlayerId(), players, elements);
        Player newPlayer = null;
        for (Collidable element : elements){
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

    private static State manageBulletShipCollision(Collidable collidable, Collidable otherCollidable, List<Collidable> elements, List<Player> players, Game game) {
        Bullet bullet;
        Ship ship;
        if (collidable.getCollidableType() == CollidableType.BULLET){
            bullet = (Bullet) collidable;
            ship = (Ship) otherCollidable;
        }else {
            bullet = (Bullet) otherCollidable;
            ship = (Ship) collidable;
        }
        if (Objects.equals(ship.getId(), bullet.getShipId())) return null;
        List<Collidable> newGameObjects = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(ship.getPlayerId(), players);
        Player newPlayer = null;
        for (Collidable element : elements){
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

    private static Player getPlayer(String shipId, List<Player> players, List<Collidable> collidables) {
        String playerId = "";
        for (Collidable value : collidables) {
            if (value.getCollidableType() == CollidableType.SHIP && Objects.equals(value.getId(), shipId)) {
                Ship ship = (Ship) value;
                playerId = ship.getPlayerId();
            }
        }
        return getPlayer(playerId, players);
    }
}
