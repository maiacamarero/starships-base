package starships.collidable;

import starships.Juego;
import starships.Player;
import starships.State;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;
import starships.factories.PlayerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Collision {
    public State handleCollision(Collidable collidable, Collidable otherCollidable, State state, Juego game) {
        List<Collidable> elements = state.getElements();
        List<Player> players = state.getPlayers();
        State newState = null;
        if ((collidable.getCollidableType().equals(CollidableType.SHIP) && otherCollidable.getCollidableType().equals(CollidableType.ASTEROID)) || (collidable.getCollidableType().equals(CollidableType.ASTEROID) && otherCollidable.getCollidableType().equals(CollidableType.SHIP))) {
            newState = manageShipAsteroidCollision(collidable, otherCollidable, elements, players, game);
        } else if ((collidable.getCollidableType().equals(CollidableType.SHIP) && otherCollidable.getCollidableType().equals(CollidableType.BULLET)) || (collidable.getCollidableType().equals(CollidableType.BULLET) && otherCollidable.getCollidableType().equals(CollidableType.SHIP))) {
            newState = manageBulletShipCollision(collidable, otherCollidable, elements, players, game);
        } else if ((collidable.getCollidableType().equals(CollidableType.BULLET) && otherCollidable.getCollidableType().equals(CollidableType.ASTEROID)) || (collidable.getCollidableType().equals(CollidableType.ASTEROID) && otherCollidable.getCollidableType().equals(CollidableType.BULLET))) {
            newState = manageBulletAsteroidCollision(collidable, otherCollidable, elements, players, game);
        }
        if (newState == null) {
            return new State(game.getNewElements(), game.getNewPlayers());
        }
        return newState;
    }

    private State manageBulletAsteroidCollision(Collidable collidable, Collidable otherCollidable, List<Collidable> elements, List<Player> players, Juego game) {
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
        Player currentPlayer = getPlayer(bullet.getShipId(), game);
        Player newPlayer = null;
        for (Collidable element : elements) {
            if (bullet.getId().equals(element.getId())) {
                game.addDeadElements(element.getId());
            } else if (element.getId().equals(asteroid.getId())) {
                Asteroid newAsteroid = (Asteroid) asteroid.setHealth(asteroid.getCurrentHealth().reduce(bullet.getDamage()));
                //Asteroid newAsteroid = new Asteroid(asteroid.getId(), asteroid.getPosition(), asteroid.getRotationInDegrees(), asteroid.getHeight(), asteroid.getWidth(), asteroid.getDirection(), asteroid.isClockwise(), asteroid.getInitialHealth(), asteroid.getCurrentHealth().reduce(bullet.getDamage()));
                if (newAsteroid.getCurrentHealth().getValue() <= 0) {
                    //newPlayer = new Player(currentPlayer.getPlayerId(), currentPlayer.getPoints() + newAsteroid.getPoints(), currentPlayer.getHealth(), currentPlayer.getShipId());
                    assert currentPlayer != null;
                    newPlayer = currentPlayer.setPoints(currentPlayer.getPoints() + newAsteroid.getPoints());
                    game.addPoints(newPlayer.getPlayerId(), newAsteroid.getPoints());
                    game.addDeadElements(element.getId());
                } else {
                    newElements.add(newAsteroid);
                    assert currentPlayer != null;
                    newPlayer = new Player(currentPlayer.getPlayerId(), currentPlayer.getPoints(), currentPlayer.getHealth(), currentPlayer.getShipId());
                }
            } else {
                newElements.add(element.getNewElementCollidable());
            }
        }
        for (Player player : players) {
            if (player.getPlayerId().equals(currentPlayer.getPlayerId())){
                newPlayers.add(newPlayer);
            }else {
                newPlayers.add(player.getNewPlayer());
            }
        }
        return new State(newElements, newPlayers);
    }

    private State manageShipAsteroidCollision(Collidable collidable, Collidable otherCollidable, List<Collidable> elements, List<Player> players, Juego game) {
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
//        Player player = getPlayer(ship.getPlayerId(), elements, game);
        Player player = getPlayer(ship.getPlayerId(), game);
        Player newPlayer = null;
        for (Collidable element : elements){
            if (ship.getId().equals(element.getId())){
                //newPlayer = new Player(player.getPlayerId(), player.getPoints(), player.getHealth().reduce(-1), player.getShipId());
                newPlayer = player.setHealth(player.getHealth().reduce(1));
                if (newPlayer.getHealth().getValue() > 0){
                    ship.setPosition(new Vector(300, 300));
                    ship.setRotationInDegrees(180);
                    ship.setDirection(new Vector(180, 180));
                    ship.setSpeed(0);
                    ship.setIsVisible(true);
                    newElements.add(ship);
                    //newElements.add(new Ship(ship.getId(), new Vector(300, 300), 180, ship.getHeight(), ship.getWidth(), ship.getPlayerId(), ship.getLastBulletShot(), 180, 0, ship.getBulletType()));
                }else {
                    game.addDeadElements(element.getId());
                    newPlayer = null;
                }
            }else if (element.getId().equals(asteroid.getId())){
                game.addDeadElements(element.getId());
            }else {
                newElements.add(element.getNewElementCollidable());
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
        if (newPlayers.isEmpty()) game.setFinished(true);
        return new State(newElements, newPlayers);
    }

    //immutabel
    private State manageBulletShipCollision(Collidable collidable, Collidable otherCollidable, List<Collidable> elements, List<Player> players, Juego game) {
        Bullet bullet;
        Ship ship;
        if (collidable.getCollidableType() == CollidableType.BULLET){
            bullet = (Bullet) collidable;
            ship = (Ship) otherCollidable;
        }else {
            bullet = (Bullet) otherCollidable;
            ship = (Ship) collidable;
        }
        //if (Objects.equals(ship.getId(), bullet.getShipId())) return null;
        List<Collidable> newElements = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player currentPlayer = getPlayer(ship.getPlayerId(), game);
        Player newPlayer = null;
        for (Collidable element : elements){
            if (bullet.getId().equals(element.getId())){
                game.addDeadElements(element.getId());
            } else if (element.getId().equals(ship.getId())) {
                assert currentPlayer != null;
                //newPlayer = new Player(currentPlayer.getPlayerId(),currentPlayer.getPoints(), new Health(currentPlayer.getHealth().reduce( 1).getValue()), currentPlayer.getShipId());
                newPlayer = currentPlayer.setHealth(new Health(currentPlayer.getHealth().reduce( 1).getValue()));
                if (newPlayer.getHealth().getValue() > 0){
                    //reset position
                    ship.setPosition(new Vector(300, 300));
                    ship.setRotationInDegrees(180);
                    ship.setDirection(new Vector(180, 180));
                    ship.setSpeed(0);
                    ship.setIsVisible(true);
                    newElements.add(ship);
                    //newElements.add(new Ship(ship.getId(),new Vector(300, 300),180, ship.getHeight(),ship.getWidth(),ship.getPlayerId(),ship.getLastBulletShot(),direction 180,speed 0, ship.getBulletType()));
                } else{
                    game.addDeadElements(ship.getId());
                }
            } else{
                newElements.add(element.getNewElementCollidable());
            }
        }
        for (Player player : players){
            assert currentPlayer != null;
            if (player.getPlayerId().equals(currentPlayer.getPlayerId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }
            else {
                newPlayers.add(player.getNewPlayer());
            }
        }
        if (newPlayers.isEmpty()) game.setFinished(true);
        return new State(newElements, newPlayers);
    }

    private Player getPlayer(String playerId, Juego juego) {
        for (Player player : juego.getState().getPlayers()) {
            if (player.getPlayerId().equals(playerId)){
                return player;
            }
        }
        return null;
    }
}
