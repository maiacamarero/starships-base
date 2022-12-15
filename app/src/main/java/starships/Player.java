package starships;

import starships.collidable.Health;

public class Player {
    private String playerId;
    private int points;
    private Health health;

    public String getShipId() {
        return shipId;
    }

    private String shipId;
    private boolean alive;

    public Player(String playerId, int points, Health health, String shipID) {
        this.playerId = playerId;
        this.points = points;
        this.health = health;
        this.shipId = shipID;
    }

    public Player(String playerId, Health health, String shipId) {
        this.playerId = playerId;
        this.points = 0;
        this.health = health;
        this.shipId = shipId;
        this.alive = true;
    }

    public void removeLife(){
        health.reduce(-1);
    }

    public void addPoints(int points){
        this.points += points;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getPoints() {
        return points;
    }

    public Health getHealth() {
        return health;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Player getNewPlayer(){
        return new Player(playerId, points, health, shipId);
    }
}
