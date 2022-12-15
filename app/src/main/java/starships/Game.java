package starships;

import javafx.scene.input.KeyCode;
import starships.Factories.AsteroidFactory;
import starships.Factories.BulletFactory;
import starships.Factories.PlayerFactory;
import starships.Factories.ShipFactory;
import starships.collidable.*;

import java.util.*;

public class Game { // start, loadGame, saveGame, resetGame, loadOtherGame (uno que ya fue guardado)
                    // shoot, move y rotate ship, handle coliision, update, pause or resumeGame

    private List<Colisionable> elements;
    private List<Player> players;
    private boolean isPaused;
    private boolean finished;
    private final GameConfiguration config;
    private State state;
    private Map<String, Integer> points;
    private Setup setup;
    private List<String> deadElements;

    public Game() {
        this.config = new GameConfiguration();
        this.finished = false;
        this.points = new HashMap<>();
        this.setup = new Setup();
    }

    public void start(boolean resumeGame){
        if (resumeGame){
            startFromSavedGame();
        }else startFromNewFame();
        this.isPaused = false;
        loadPoints();
    }

    private void loadPoints() {
        for (Player player : players) {
            points.put(player.getPlayerId(), 0);
        }
    }

    private void startFromNewFame() {
        List<Player> players = PlayerFactory.generate(config);
        this.state = new State(ShipFactory.generate(players.size(), players), players);
    }

    private void startFromSavedGame() {
        this.state = setup.getSavedGameState();
    }

    public Map<String, KeyCode> keyboardConfig(){
        return config.getKeyboardConfig();
    }

    public void addPoints(String playerID, int points){
        int currentPoints = this.points.get(playerID);
        this.points.put(playerID, currentPoints + points);
    }

    public void shoot(String shipID){
        List<Colisionable> newElements = new ArrayList<>();
        Ship bulletsShip = null;
        for (Colisionable element : elements) {
            if (Objects.equals(element.getId(), shipID)){
                bulletsShip = (Ship) element;
                if (bulletsShip.canShoot()){
                    newElements.add(bulletsShip.shoot());
                }else newElements.add(bulletsShip.getNewElementColisionable());
            }else newElements.add(element.getNewElementColisionable());
        }
        if (bulletsShip != null && bulletsShip.canShoot()){
            newElements.add(BulletFactory.generate(bulletsShip));
        }
        refreshState(newElements, getNewPlayers());
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

    public void refreshState(List<Colisionable> elements, List<Player> players){
        this.state = new State(elements, players);
    }

    public void moveShip(String shipID, boolean up){
        List<Colisionable> newElements = new ArrayList<>();
        assert getElements() != null;
        for (Colisionable element : getElements()){
            if (element.getId().equals(shipID) && !isPaused){
                Ship ship = (Ship) element;
                newElements.add(ship.move(up));
            }else newElements.add(element.getNewElementColisionable());
        }
        refreshState(newElements, getNewPlayers());
    }

    private List<Colisionable> getElements() {
        if (state == null){
            return null;
        }else return state.getElements();
    }

    public List<Colisionable> getNewElements(){
        List<Colisionable> elements = new ArrayList<>();
        for (Colisionable element : Objects.requireNonNull(getElements())){
            elements.add(element.getNewElementColisionable());
        }
        return elements;
    }

    public void rotate(String shipID, int rotationInDegrees){
        List<Colisionable> newElements = new ArrayList<>();
        for (Colisionable element: Objects.requireNonNull(getElements())){
            if (element.getId().equals(shipID) && !isPaused){
                Ship ship = (Ship) element;
                newElements.add(ship.rotate(rotationInDegrees));
            }else newElements.add(element.getNewElementColisionable());
        }
        refreshState(newElements, getNewPlayers());
    }

    public void handleCollision(String id, String otherID){
        Colisionable colisionable = null;
        Colisionable otherColisionable = null;
        for (Colisionable element : getElements()){
            if (element.getId().equals(id)){
                colisionable = element;
            }
            if (element.getId().equals(otherID)){
                otherColisionable = element;
            }
        }
        if (colisionable != null && otherColisionable != null){
            this.state = Collision.handleCollision(colisionable, otherColisionable, state, this);
        }else {
            refreshState(getNewElements(), getNewPlayers());
        }
    }

    private void updateView(){
        if (!isPaused && state != null){
            boolean hasShip = false;
            List<Colisionable> newElements = new ArrayList<>();
            boolean entered = false;
            for (Colisionable element : getElements()){
                if (element.getCollidableType() == CollidableType.SHIP) hasShip = true;
                if (element.getCollidableType() != CollidableType.SHIP && !entered){
                    spawnAsteroid(newElements);
                    entered = true;
                }
                Colisionable newElement = element.update();
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

    private void spawnAsteroid(List<Colisionable> elements) {
        List<Asteroid> asteroids = new ArrayList<>();
        for (Colisionable element: getElements()){
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

    public void pauseOrResumeGame(){
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
}















