package starships;

import javafx.scene.input.KeyCode;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Ship;
import starships.factories.AsteroidFactory;
import starships.factories.BulletFactory;
import starships.factories.PlayerFactory;
import starships.factories.ShipFactory;
import starships.collidable.*;

import java.util.*;

public class Game { // start, loadGame, saveGame, resetGame, loadOtherGame (uno que ya fue guardado)
                    // shoot, move y rotate ship, handle collision, update, pause or resumeGame
    private boolean isPaused;
    private boolean finished;
    private final GameConfiguration config;
    private State state;
    private final Map<String, Integer> points;
    private final Setup setup;
    private final List<String> deadElements;

    public Game() {
        this.config = new GameConfiguration();
        this.finished = false;
        this.points = new HashMap<>();
        this.setup = new Setup();
        this.deadElements = new ArrayList<>();
    }

    public void start(boolean resumeGame){
        if (resumeGame){
            startFromSavedGame();
        }else startFromNewFame();
        this.isPaused = false;
        loadPoints();
    }

    private void loadPoints() {
        for (Player player : getPlayers()) {
            points.put(player.getPlayerId(), 0);
        }
    }

    private void startFromNewFame() {
        List<Player> players = PlayerFactory.generate(config);
        this.state = new State(ShipFactory.generate(players.size(), players), players);
    }

    private void startFromSavedGame() {
        this.state = setup.getSavedState();
    }

    public Map<String, KeyCode> keyboardConfig(){
        return config.getKeyboardConfig();
    }

    public void addPoints(String playerID, int pointsToAdd){
        int currentPoints = points.get(playerID);
        points.put(playerID, currentPoints + pointsToAdd);
    }

    public void shoot(String shipID){
        List<Collidable> newElements = new ArrayList<>();
        Ship shipShooting = null;
        for (Collidable element : getElements()) {
            if (Objects.equals(element.getId(), shipID)){
                shipShooting = (Ship) element;
                if (shipShooting.canShoot()){
                    newElements.add(shipShooting.shoot());
                }else newElements.add(shipShooting.getNewElementColisionable());
            }else newElements.add(element.getNewElementColisionable());
        }
        if (shipShooting != null && shipShooting.canShoot()){
            newElements.add(BulletFactory.generate(shipShooting));
        }
        restoreState(newElements, getNewPlayers());
    }

    public List<Player> getNewPlayers() {
        List<Player> playersList = new ArrayList<>();
        for (int i = 0; i < getPlayers().size(); i++) {
            playersList.add(getPlayers().get(i).getNewPlayer());
        }
        return playersList;
    }

    public List<Player> getPlayers() {
        if (state == null){
            return null;
        }else return state.getPlayers();
    }

    public void restoreState(List<Collidable> elements, List<Player> players){
        this.state = new State(elements, players);
    }

    public void moveShip(String shipID, boolean accelerate){
        List<Collidable> newElements = new ArrayList<>();
        for (Collidable element : getElements()){
            if (element.getId().equals(shipID) && !isPaused){
                Ship ship = (Ship) element;
                newElements.add(ship.moveY(accelerate));
            }else newElements.add(element.getNewElementColisionable());
        }
        restoreState(newElements, getNewPlayers());
    }

    public void moveShipX(String shipID, boolean accept){
        List<Collidable> newElements = new ArrayList<>();
        for (Collidable element : getElements()){
            if (element.getId().equals(shipID) && !isPaused){
                Ship ship = (Ship) element;
                newElements.add(ship.moveX(accept));
            }else newElements.add(element.getNewElementColisionable());
        }
        restoreState(newElements, getNewPlayers());
    }

    public List<Collidable> getElements() {
        if (state == null){
            return null;
        }else return state.getElements();
    }

    public List<Collidable> getNewElements(){
        List<Collidable> elements = new ArrayList<>();
        for (Collidable element : Objects.requireNonNull(getElements())){
            elements.add(element.getNewElementColisionable());
        }
        return elements;
    }

    public void rotate(String shipID, int rotationInDegrees){
        List<Collidable> newElements = new ArrayList<>();
        for (Collidable element: getElements()){
            if (element.getId().equals(shipID) && !isPaused){
                Ship ship = (Ship) element;
                newElements.add(ship.rotate(rotationInDegrees));
            }else newElements.add(element.getNewElementColisionable());
        }
        restoreState(newElements, getNewPlayers());
    }

    public void handleCollision(String id, String otherID){
        Collidable collidable = null;
        Collidable otherCollidable = null;
        for (Collidable element : getElements()){
            if (element.getId().equals(id)){
                collidable = element;
            }
            if (element.getId().equals(otherID)){
                otherCollidable = element;
            }
        }
        if (collidable != null && otherCollidable != null){
            this.state = Collision.handleCollision(collidable, otherCollidable, state, this);
        }else {
            restoreState(getNewElements(), getNewPlayers());
        }
    }

    public void updateView(){
        if (!isPaused && state != null){
            boolean hasShip = false;
            List<Collidable> newElements = new ArrayList<>();
            boolean entered = false;
            for (Collidable element : getElements()){
                if (element.getCollidableType() == CollidableType.SHIP) hasShip = true;
                if (element.getCollidableType() != CollidableType.SHIP && !entered){
                    spawnAsteroid(newElements);
                    entered = true;
                }
                Collidable newElement = element.update();
                if (newElement != null) {
                    newElements.add(newElement);
                }else {
                    addDeadElements(element.getId());
                }
            }
            if (!hasShip) finishGame();
            if (getElements().size() == getPlayers().size()){
                spawnAsteroid(newElements);
            }
            this.state = new State(newElements, getNewPlayers());
        }
    }

    private void spawnAsteroid(List<Collidable> elements) {
        List<Asteroid> asteroids = new ArrayList<>();
        for (Collidable element: getElements()){
            if (element.getCollidableType() == CollidableType.ASTEROID){
                asteroids.add((Asteroid) element);
            }
        }
        AsteroidFactory.generate(asteroids, elements);
    }

    public boolean hasFinished(){
        return finished;
    }

    public void  finishGame(){
        this.finished = true;
    }

    public void printLeaderBoard(){
        if (getPlayers() != null){
            System.out.println("LEADERBOARD");
            points.forEach((key, value) -> System.out.println(key + " = " + value + " points"));
        }
    }

    public void pauseUnpauseGame(){
        this.isPaused = !isPaused;
    }

    public void saveGame(){
        setup.saveGame(state);
    }

    public void addDeadElements(String id){
        deadElements.add(id);
    }

    public List<String> getDeadElements() {
        return deadElements;
    }

    public boolean isPaused() {
        return isPaused;
    }
}















