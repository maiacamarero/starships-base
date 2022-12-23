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
    private final AsteroidFactory asteroidFactory;

    public Juego(List<String> deadElements, Map<String, Integer> points, GameConfiguration configuration, State state, boolean paused, boolean finished) {
        this.deadElements = deadElements;
        this.points = points;
        this.configuration = configuration;
        this.state = state;
        this.paused = paused;
        this.finished = finished;
        this.asteroidFactory = new AsteroidFactory();
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

//    public Juego updateView() {
//        State newState = null;
//        List<Collidable> currentElements = state.getElements();
//        List<Collidable> asteroids;
//        Collidable newElement = null;
//        if (!paused) {
////            AsteroidFactory.generate(state.getElements());
////            for (Collidable element : state.getElements()) {
////                if (element.getCollidableType() == CollidableType.SHIP){
////                    Ship ship = (Ship) element;
////                    Ship newShip = ship.update();
////                    newState = state.setCollidable(newShip);
////                } else if (element.getCollidableType() == CollidableType.BULLET) {
////                    Bullet bullet = (Bullet) element;
////                    Bullet newBullet = bullet.update();
////                    newState = state.setCollidable(newBullet);
////                }else if (element.getCollidableType() == CollidableType.ASTEROID){
////                    Asteroid asteroid = (Asteroid) element;
////                    Asteroid newAsteroid = asteroid.update();
////                    newState = state.setCollidable(newAsteroid);
////                }
////            }
//            boolean hasShip = false;
//            boolean entered = false;
//            asteroidFactory.generate(currentElements);
//
//            for (Collidable element : currentElements) {
//                if (element.getCollidableType() == CollidableType.SHIP) {
//                    hasShip = true;
//                    //newState = state.setCollidable(element);
//                    Collidable newShip = element.update();
//                    newState = state.setCollidable(newShip);
//                }
//
//                if (element.getCollidableType() != CollidableType.SHIP && !entered) {
//                    asteroids = asteroidFactory.generate(currentElements);
//                    for (Collidable asteroid : asteroids) {
//                        newElement = asteroid.update();
//                        break;
//                    }
//                    newState = state.setCollidable(newElement);
//                    entered = true;
//                }//else newState = state.setCollidable(element);
//                //newState = state.setCollidable(element.update());
//            }
//            if (!hasShip) return setFinished(true);
//            if (newState.getElements().size() == newState.getPlayers().size()) {
//                asteroids = asteroidFactory.generate(currentElements);
//                for (Collidable asteroid : asteroids) {
//                    //if (element1.getCollidableType() == CollidableType.ASTEROID) {
//                    //int i = 0;
//                    //while (i < 20){
//                        newState = state.setCollidable(asteroid);
//                      //  i++;
//                    //}
//                        //newState = state.setCollidable(asteroid);
//                    //}
//                }
//            }
//        }
//        return setState(newState);
//
//    }

    public Juego update(){
        State newState = null;
        List<Collidable> asteroids;
        if (!paused && state != null){
            boolean hasShip = false;
            boolean entered = false;
            for (Collidable gameObject : state.getElements()){
                if (gameObject.getCollidableType() == CollidableType.SHIP) hasShip = true;
                if (gameObject.getCollidableType() != CollidableType.SHIP && !entered){
                    asteroids = spawnAsteroids(state);
                    for (Collidable asteroid : asteroids) {
                        newState = state.setCollidable(asteroid);
                    }
                    entered = true;
                }
                Collidable newGameObject = gameObject.update();
                if (newGameObject != null) newState = state.setCollidable(newGameObject);
                else{
                    addDeadElements(gameObject.getId());
                }
            }
            if (!hasShip) hasFinished();
            if (state.getElements().size() == state.getPlayers().size()){
                assert newState != null;
                asteroids = spawnAsteroids(state);
                for (Collidable asteroid : asteroids) {
                    newState = state.setCollidable(asteroid);
                }
            }
        }

        return setState(newState);
    }

    private List<Collidable> spawnAsteroids(State state) {
        List<Collidable> meteors = asteroidFactory.generate(state.getElements());;
        for (Collidable gameObject : state.getElements()){
            if (gameObject.getCollidableType() == CollidableType.ASTEROID){
                meteors.add(gameObject);
            }
        }

        return meteors;
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
