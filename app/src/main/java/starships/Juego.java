package starships;

import javafx.scene.input.KeyCode;
import starships.collidable.Collidable;
import starships.collidable.CollidableType;
import starships.collidable.Collision;
import starships.collidable.Vector;
import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;
import starships.factories.AsteroidFactory;
import starships.factories.BulletFactory;

import java.util.*;

public class Juego {

    private final Map<String, Integer> points;
    private final List<String> deadElements;

    private final GameConfiguration configuration;
    private final State state;

    private boolean paused;
    private final boolean finished;

    public Juego(List<String> deadElements, Map<String, Integer> points, GameConfiguration configuration, State state, boolean paused, boolean finished) {
        this.deadElements = deadElements;
        this.points = points;
        this.configuration = configuration;
        this.state = state;
        this.paused = paused;
        this.finished = finished;
        //this.asteroidFactory = new AsteroidFactory();
    }

    public Juego setState(State state){
        return new Juego(getDeadElements(), getPoints(), getConfiguration(), state, isPaused(), isFinished());
    }

    public Juego move(String shipId, Vector direction){
        State newState = null;
        for (Collidable element : state.getElements()) {
            if (element.getCollidableType() == CollidableType.SHIP && !paused && element.getId().equals(shipId)){
                Ship ship = (Ship) element;
                Ship newShip = ship.move(direction);
                newState = state.setCollidable(newShip);
            }
        }
        return setState(newState);
    }

    public Juego update(){
        State newState = null;
        List<Collidable> asteroids;
        if (!paused){
            boolean hasShip = false;
            boolean entered = false;
            for (Collidable element : state.getElements()){
                if (element.getCollidableType() == CollidableType.SHIP) hasShip = true;
                if (element.getCollidableType() != CollidableType.SHIP && !entered){
                    asteroids = spawnAsteroids(state);
                    for (Collidable asteroid : asteroids) {
                        newState = state.setCollidable(asteroid);
                    }
                    entered = true;
                }
                Collidable newElement = element.update();
                if (newElement != null) newState = state.setCollidable(newElement);
            }
            if (!hasShip) hasFinished();
            if (state.getElements().size() == state.getPlayers().size()){
                assert newState != null;
                asteroids = spawnAsteroids(newState);
                for (Collidable asteroid : asteroids) {
                    newState = state.setCollidable(asteroid);
                }
            }
        }
        return setState(newState);
    }

    private List<Collidable> spawnAsteroids(State state) {
        AsteroidFactory asteroidFactory = new AsteroidFactory();
        List<Collidable> asteroids = asteroidFactory.generate(state.getElements());;
        for (Collidable element : state.getElements()){
            if (element.getCollidableType() == CollidableType.ASTEROID){
                asteroids.add(element);
            }
        }
        return asteroids;
    }

    public Juego handleCollision(String id, String id2){
        State newState = null;
        Collision collision = new Collision();
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
            newState = (collision.handleCollision(collidable, otherCollidable, state, this));
        }
        return setState(newState);
    }

    public Juego rotate(String shipId, double rotationInDegrees){
        State newState = null;
        for (Collidable element : state.getElements()) {
            if (element.getId().equals(shipId) && !paused){
                Ship ship = (Ship) element;
                Ship newShip = ship.rotate(rotationInDegrees);
                newState = state.setCollidable(newShip);
            }
        }
        return setState(newState);
    }

    public Juego shoot(String shipId){
        BulletFactory bulletFactory = new BulletFactory();
        State newState = null;
        Ship ship = null;
        for (Collidable element : state.getElements()) {
            if (element.getId().equals(shipId)){
                ship = (Ship) element;
                if (ship.canShoot()){
                    newState = state.setCollidable(ship.shoot());
                }
            }
        }
        if (ship != null && ship.canShoot()){
            Bullet bullet = bulletFactory.generate(ship);
            newState = state.setCollidable(bullet);
        }
        return setState(newState);
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

    public List<Collidable> getElements() {
        return state.getElements();
    }

    public Map<String, KeyCode> keyboardConfig(){
        return configuration.getKeyboardConfig();
    }

    public Juego setPaused(boolean paused){
        return new Juego(getDeadElements(),  getPoints(), getConfiguration(), getState(), paused, hasFinished());
    }

    public Juego setFinished(boolean finished){
        return new Juego(getDeadElements(),  getPoints(), getConfiguration(), getState(), isPaused(), finished);
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
        loadPoints();
        if (state.getPlayers() != null){
            System.out.println("Tabla de Clasificación");
            points.forEach((key, value) -> System.out.println(key + " = " + value + " puntos."));
        }
    }

    public Juego pausar(){
        return setPaused(true);
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
