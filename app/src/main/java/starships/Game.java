package starships;

import javafx.scene.input.KeyCode;
import starships.collidable.elements.Asteroid;
import starships.collidable.elements.Bullet;
import starships.collidable.elements.Ship;
import starships.factories.AsteroidFactory;
import starships.factories.BulletFactory;
import starships.factories.PlayerFactory;
import starships.factories.ShipFactory;
import starships.collidable.*;

import java.util.*;

public class Game { // start, loadGame, saveGame, resetGame, loadOtherGame (uno que ya fue guardado)
//                    // shoot, move y rotate ship, handle collision, update, pause or resumeGame
//    // da nullpointerexception en la factory de asteroides lpm
//    private boolean isPaused;
//    private boolean finished;
//    private final GameConfiguration config;
//    private State state;
//    private final Map<String, Integer> points;
//    private final Setup setup;
//    private final List<String> deadElements;
//    private List<Collidable> elements;
//
//    public Game() {
//        this.config = new GameConfiguration();
//        this.finished = false;
//        this.points = new HashMap<>();
//        this.setup = new Setup();
//        this.deadElements = new ArrayList<>();
//        this.elements = new ArrayList<>();
//    }
//
//    private Game(Game game, State state) {
//        this.state = state;
//        this.config = game.config;
//        this.finished = game.finished;
//        this.points = game.points;
//        this.setup = game.setup;
//        this.deadElements = game.deadElements;
//    }
//
//    private Game(Game game, List<String> deadElements) {
//        this.state = game.state;
//        this.config = game.config;
//        this.finished = game.finished;
//        this.points = game.points;
//        this.setup = game.setup;
//        this.deadElements = deadElements;
//    }
//
//    public Game(Game game, State state, List<String> deadElements) {
//        this.state = state;
//        this.config = game.config;
//        this.finished = game.finished;
//        this.points = game.points;
//        this.setup = game.setup;
//        this.deadElements = deadElements;
//    }
//
//    public void start(boolean resumeGame){
//        if (resumeGame){
//            startFromSavedGame();
//        }else startFromNewGame();
//        this.isPaused = false;
//        loadPoints();
//    }
//
//    private void loadPoints() {
//        for (Player player : getPlayers()) {
//            points.put(player.getPlayerId(), 0);
//        }
//    }
//
//    private void startFromNewGame() {
//        List<Player> players = PlayerFactory.generate(config);
//        this.state = new State(ShipFactory.generate(players.size(), players), players);
//    }
//
//    private void startFromSavedGame() {
//        this.state = Setup.getSavedState();
//    }
//
//    public Map<String, KeyCode> keyboardConfig(){
//        return config.getKeyboardConfig();
//    }
//
//    public void addPoints(String playerID, int pointsToAdd){
//        int currentPoints = points.get(playerID);
//        points.put(playerID, currentPoints + pointsToAdd);
//    }
//
//    //immutable
//    public Game shoot(String shipID){
//        List<Collidable> newElements = new ArrayList<>();
//        Ship shipShooting = null;
//        for (Collidable element : getElements()) {
//            if (Objects.equals(element.getId(), shipID)){
//                shipShooting = (Ship) element;
//                if (shipShooting.canShoot()){
//                    newElements.add(shipShooting.shoot());
//                }else {
//                    newElements.add(shipShooting.getNewElementCollidable());
//                }
//            }else newElements.add(element.getNewElementCollidable());
//        }
//        if (shipShooting != null && shipShooting.canShoot()){
//            newElements.add(BulletFactory.generate(shipShooting));
//        }
//        return new Game(this, restoreState(newElements, getNewPlayers()));
//    }
//
//    //immutable
//    public List<Player> getNewPlayers() {
//        List<Player> playersList = new ArrayList<>();
//        for (int i = 0; i < getPlayers().size(); i++) {
//            playersList.add(getPlayers().get(i).getNewPlayer());
//        }
//        return playersList;
//    }
//
//    //immutable
//    public List<Player> getPlayers() {
//        if (state == null){
//            return null;
//        }else return state.getPlayers();
//    }
//
//    //immutable
//    public State restoreState(List<Collidable> elements, List<Player> players){
//        return new State(elements, players);
//    }
//    //immutable
//    public Game moveShip(String shipID){
//        List<Collidable> newElements = new ArrayList<>();
//        for (Collidable element : getElements()) {
//            if (element.getCollidableType() == CollidableType.SHIP && !isPaused && element.getId().equals(shipID)){
//                Ship ship = (Ship) element;
//                Player owner = getPlayerByShip(ship);
//                owner =owner.setShipId(shipID);
//                newElements.add(ship);
//            }
//        }
////        List<Collidable> newElements = new ArrayList<>();
////        for (Collidable element : getElements()){
////            if (element.getId().equals(shipID) && !isPaused){
////                Ship ship = (Ship) element;
////                newElements.add(ship.moveY(accelerate));
////            }else newElements.add(element.getNewElementCollidable());
////        }
////        return new Game(this, restoreState(newElements, getNewPlayers()));
//        return new Game(this, restoreState(newElements, getPlayers()));
//    }
//
//    private Player getPlayerByShip(Ship ship) {
//        String playerId = ship.getPlayerId();
//        for (Player player : getPlayers()) {
//            if (player.getPlayerId().equals(playerId)){
//                return player;
//            }
//        }
//        return null;
//    }
//
//    //immutable
//    public List<Collidable> getElements() {
//        if (state == null){
//            return null;
//        }else return state.getElements();
//    }
//    //immutable
//    public List<Collidable> getNewElements(){
//        List<Collidable> elements = new ArrayList<>();
//        for (Collidable element : Objects.requireNonNull(getElements())){
//            elements.add(element.getNewElementCollidable());
//        }
//        return elements;
//    }
//    //immutable
//    public Game rotate(String shipID, int rotationInDegrees){
//        List<Collidable> newElements = new ArrayList<>();
//        for (Collidable element: getElements()){
//            if (element.getId().equals(shipID) && !isPaused){
//                Ship ship = (Ship) element;
//                newElements.add(ship.rotate(rotationInDegrees));
//            }else newElements.add(element.getNewElementCollidable());
//        }
//        return new Game(this, restoreState(newElements, getNewPlayers()));
//    }
//    //immutable
////    public Game handleCollision(String id, String otherID){
////        Collidable collidable = null;
////        Collidable otherCollidable = null;
////        for (Collidable element : getElements()){
////            if (element.getId().equals(id)){
////                collidable = element;
////            }
////            if (element.getId().equals(otherID)){
////                otherCollidable = element;
////            }
////        }
////        if (collidable != null && otherCollidable != null){
////            return new Game(this, Collision.handleCollision(collidable, otherCollidable, state, this));
////        }else {
////            return new Game(this, restoreState(getNewElements(), getNewPlayers()));
////        }
////    }
//
//    //immutable
//    public Game updateView(){
//        if (!isPaused && state != null){
//            boolean hasShip = false;
//            boolean entered = false;
//            List<Collidable> newElements = new ArrayList<>();
//            List<String> newDeadElements = new ArrayList<>();
//            List<Collidable> asteroids = new ArrayList<>();
//            for (int i = 0; i < getElements().size(); i++) {
//                Collidable element = getElements().get(i);
//                if (element.getCollidableType() != CollidableType.SHIP && !entered){
//                    asteroids = spawnAsteroid(getElements());
//                }
//                if (element.getCollidableType() == CollidableType.SHIP){
//                    hasShip = true;
//                    Ship ship = (Ship) element;
//                    Player owner = getPlayerByShip(ship);
//                    Ship newShip = ship.update();
//                    assert owner != null;
//                    //newElements.set(i, newShip);
//                    owner.setShipId(newShip.getId());
//                    if (newShip == null){
//                        newDeadElements.add(newShip.getId());
//                    }else newElements.add(newShip);
//                }else if (element.getCollidableType() == CollidableType.BULLET){
//                    Bullet bullet = (Bullet) element;
//                    Bullet newBullet = bullet.update();
//                    newElements.set(i, newBullet);
//                    if (newBullet == null){
//                        newDeadElements.add(newBullet.getId());
//                    }else newElements.add(newBullet);
//                }else if (element.getCollidableType() == CollidableType.ASTEROID){
//                    Asteroid asteroid = (Asteroid) element;
//                    Asteroid newAsteroid = asteroid.update();
//                    newElements.set(i, newAsteroid);
//                    asteroids.set(asteroids.indexOf(asteroid), newAsteroid);
//                    if (newAsteroid == null){
//                        newDeadElements.add(newAsteroid.getId());
//                    }else newElements.add(newAsteroid);
//                }
//
//            }
////            boolean hasShip = false;
////            List<Collidable> newElements = new ArrayList<>();
////            List<Collidable> elements = new ArrayList<>();
////            List<String> newDeadElements = new ArrayList<>();
////            boolean entered = false;
////            for (Collidable element : getElements()){
////                if (element.getCollidableType() == CollidableType.SHIP){
////                    hasShip = true;
////                }
////                if (element.getCollidableType() != CollidableType.SHIP && !entered){
////                    elements = spawnAsteroid(newElements);
////                    entered = true;
////                }
////                Collidable newElement = element.update();
////                if (newElement != null) {
////                    newElements.add(newElement);
////                }else {
////                    newDeadElements = addDeadElements(element.getId());
////                }
////            }
////            elements.addAll(newElements);
////            if (!hasShip) finishGame();
//            return new Game(this, new State(elements, getNewPlayers()), newDeadElements);
//        }
//        return this;
//    }
//
//    private List<Collidable> spawnAsteroid(List<Collidable> elements) {
//        List<Asteroid> asteroids = new ArrayList<>();
//        for (Collidable element: getElements()){
//            if (element.getCollidableType() == CollidableType.ASTEROID){
//                asteroids.add((Asteroid) element);
//            }
//        }
//        List<Collidable> asteroids1 = AsteroidFactory.generate(elements);
//        asteroids1.addAll(elements);
//        return asteroids1;
//    }
//
//    public boolean hasFinished(){
//        return finished;
//    }
//
//    public void finishGame(){
//        this.finished = true;
//    }
//
//    public void printLeaderBoard(){
//        if (getPlayers() != null){
//            System.out.println("Tabla de Clasificación");
//            points.forEach((key, value) -> System.out.println(key + " = " + value + " puntos."));
//        }
//    }
//
//    public void pauseUnpauseGame(){
//        this.isPaused = !isPaused;
//    }
//
//    public void saveGame(){
//        setup.saveGame(state);
//    }
//
//    public List<String> addDeadElements(String id){
//        deadElements.add(id);
//        return deadElements;
//    }
//
//    public List<String> getDeadElements() {
//        return deadElements;
//    }
//
//    public boolean isPaused() {
//        return isPaused;
//    }
}















