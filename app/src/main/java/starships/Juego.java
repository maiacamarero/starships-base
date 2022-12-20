package starships;

import javafx.scene.input.KeyCode;
import starships.collidable.Collidable;
import starships.collidable.CollidableType;
import starships.collidable.Collision;
import starships.collidable.Vector;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Ship;
import starships.factories.AsteroidFactory;
import starships.factories.BulletFactory;

import java.util.*;

public class Juego {

    private Map<String, Integer> points;
    private List<String> deadElements;

    private GameConfiguration configuration;
    private State state;

    private boolean paused;
    private boolean finished;

    public Juego() {
        deadElements = new ArrayList<>();
        points = new HashMap<>();
        configuration = new GameConfiguration();
        finished  = false;
    }

//    public Juego(State state) {
//        //this = juego;
//        this.state = state;
//        players = new ArrayList<>();
//        deadElements = new ArrayList<>();
//        points = new HashMap<>();
//        configuration = new GameConfiguration();
//        finished  = false;
//    }

    public Juego(List<String> deadElements, Map<String, Integer> points, GameConfiguration configuration, State state, boolean paused, boolean finished) {
        this.deadElements = deadElements;
        this.points = points;
        this.configuration = configuration;
        this.state = state;
        this.paused = paused;
        this.finished = finished;
    }

    public Juego setState(State state){
        return new Juego(deadElements, points, configuration, state, paused, finished);
    }

//    public Juego start(boolean resumeGame){
//        if (resumeGame){
//            this.paused = false;
//            loadPoints();
//            return fromSavedGame();
//        }else {
//            this.paused = false;
//            loadPoints();
//            return fromNewGame();
//        }
//    }

//    private Juego fromNewGame() {
//        players = PlayerFactory.generate(configuration);
//        this.state = new State(ShipFactory.generate(players.size(), players), players);
//        state.getElements().addAll(state.getElements());
//        return setState(state);
//    }
//
//
//    private Juego fromSavedGame() {
//        //return new Juego(Setup.getSavedState());
//        return setState(Setup.getSavedState());
//    }

    public Juego move(String shipId, Vector direction){
        List<Collidable> newElements = new ArrayList<>();
        for (Collidable element : state.getElements()) {
            if (element.getCollidableType() == CollidableType.SHIP && !paused && element.getId().equals(shipId)){
                Ship ship = (Ship) element;
                Ship newShip = ship.move(direction);
                newElements.add(newShip);
                //newElements.addAll(elements);
            }else newElements.add(element.getNewElementCollidable());
        }
        //return new Juego(new State(newElements, getNewPlayers()));
        return setState(new State(newElements, getNewPlayers()));
    }

    public Juego updateView() {
        State state1 = null;
        if (!paused && state != null) {
//            AsteroidFactory.generate(elements);
//            List<Collidable> elementsAux = new ArrayList<>();
//            List<Bullet> bulletsAux = new ArrayList<>();
//            List<Asteroid> asteroidsAux = new ArrayList<>();
//            for (Collidable element : elements) {
//                if (element.getCollidableType() == CollidableType.SHIP){
//                    Ship ship = (Ship) element;
//                    Ship newShip = ship.update();
//                    elementsAux.add(newShip);
//                } else if (element.getCollidableType() == CollidableType.BULLET) {
//                    Bullet bullet = (Bullet) element;
//                    Bullet newBullet = bullet.update();
//                    elementsAux.add(newBullet);
//                    bulletsAux.add(newBullet);
//                } else if (element.getCollidableType() == CollidableType.ASTEROID) {
//                    Asteroid asteroid = (Asteroid) element;
//                    Asteroid newAsteroid = asteroid.update();
//                    elementsAux.add(newAsteroid);
//                    asteroidsAux.add(newAsteroid);
//                }
//            }
//            elements = elementsAux;
//            bullets = bulletsAux;
//            asteroids = asteroidsAux;
//            this.state = new State(elementsAux, getNewPlayers());
            boolean hasShip = false;
            boolean entered = false;
            List<Collidable> newElements = new ArrayList<>();
            for (Collidable element : state.getElements()) {
                if (element.getCollidableType() == CollidableType.SHIP) {
                    hasShip = true;
                }
                if (element.getCollidableType() != CollidableType.SHIP && !entered) {
                    spawnAsteroids(newElements);
                    entered = true;
                }
                Collidable newElement = element.update();
                if (newElement != null) {
                    newElements.add(newElement);
                } else deadElements.add(newElement.getId());
            }
            if (!hasShip) return setFinished(true);
            if (state.getElements().size() == state.getPlayers().size()) {
                spawnAsteroids(newElements);
            }
            this.state = new State(newElements, getNewPlayers());

        }
        //return new Juego(state);
        return setState(state);
    }

    public Juego handleCollision(String id, String id2){
        Collidable collidable = null;
        Collidable otherCollidable = null;
        for (Collidable element : state.getElements()) {
            if (element.getId().equals(id)) {
                collidable = element;
            }
            if (element.getId().equals(id2)){
                otherCollidable = element;
            }
        }
        if (collidable != null && otherCollidable != null){
            //return new Juego(Collision.handleCollision(collidable, otherCollidable, state, this));
            return setState(Collision.handleCollision(collidable, otherCollidable, state, this));
        }else {
            //return new Juego(new State(getNewElements(), getNewPlayers()));
            return setState(new State(getNewElements(), getNewPlayers()));
        }
    }

    public Juego rotate(String shipId, double rotationInDegrees){
        List<Collidable> newElements = new ArrayList<>();
        for (Collidable element : state.getElements()) {
            if (element.getId().equals(shipId) && !paused){
                Ship ship = (Ship) element;
                Ship newShip = ship.rotate(rotationInDegrees);
                newElements.add(newShip);
            }else newElements.add(element.getNewElementCollidable());
        }
        //return new Juego(new State(newElements, getNewPlayers()));
        return setState(new State(newElements, getNewPlayers()));
    }

    public Juego shoot(String shipId){
        List<Collidable> newElements = new ArrayList<>();
        Ship ship = null;
        for (Collidable element : state.getElements()) {
            if (element.getId().equals(shipId)){
                ship = (Ship) element;
                if (ship.canShoot()){
                    newElements.add(ship.shoot());
                }else {
                    newElements.add(ship.getNewElementCollidable());
                }
            }else {
                newElements.add(element.getNewElementCollidable());
            }
        }
        if (ship != null && ship.canShoot()){
            newElements.add(BulletFactory.generate(ship));
        }
        //return new Juego(new State(newElements, getNewPlayers()));
        return setState(new State(newElements, getNewPlayers()));
    }

    public List<Collidable> getNewElements(){
        List<Collidable> newElements = new ArrayList<>();
        for (Collidable element : state.getElements()){
            newElements.add(element.getNewElementCollidable());
        }
        return newElements;
    }

    public void addDeadElements(String id){
        deadElements.add(id);
    }
    public void addPoints(String playerID, int pointsToAdd){
        points.put(playerID, points.get(playerID) + pointsToAdd);
    }

    public List<Player> getNewPlayers() {
        List<Player> newPlayers = new ArrayList<>();
        for (Player player : state.getPlayers()) {
            newPlayers.add(player.getNewPlayer());
        }
        return newPlayers;
    }

    private void spawnAsteroids(List<Collidable> elements) {
        for (Collidable element : elements) {
            if (element.getCollidableType() == CollidableType.ASTEROID) {
                state.getElements().add((Asteroid) element);
            }
        }
        AsteroidFactory.generate(elements);
    }

    private Player getPlayerByShip(Ship ship) {
        String ownerId = ship.getPlayerId();
        for (Player player : state.getPlayers()) {
            if (player.getPlayerId().equals(ownerId)){
                return player;
            }
        }
        return null;
    }

    public List<Collidable> getElements() {
        return state.getElements();
    }

    public Map<String, KeyCode> keyboardConfig(){
        return configuration.getKeyboardConfig();
    }

    public Juego setPaused(boolean paused){
        return new Juego(deadElements,  points, configuration, state, paused, finished);
    }

    public Juego setFinished(boolean finished){
        return new Juego(deadElements,  points, configuration, state, paused, finished);
    }

    public void saveGame(){
        Setup.saveGame(this.state);
    }

    private void loadPoints() {
        for (Player player : state.getPlayers()) {
            points.put(player.getPlayerId(), 0);
        }
    }

    public boolean hasFinished(){
        return finished;
    }

    public void printLeaderBoard(){
        if (state.getPlayers() != null){
            System.out.println("Tabla de Clasificación");
            points.forEach((key, value) -> System.out.println(key + " = " + value + " puntos."));
        }
    }

    public void pause(){
        this.paused = !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public List<String> getDeadElements() {
        return deadElements;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public State getState() {
        return state;
    }

    public boolean isFinished() {
        return finished;
    }
}
