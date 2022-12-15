package starships;

import starships.collidable.Colisionable;

import java.util.List;

public class State {
    private List<Colisionable> elements;
    private List<Player> players;

    public State(List<Colisionable> elements, List<Player> players) {
        this.elements = elements;
        this.players = players;
    }

    public List<Colisionable> getElements() {
        return elements;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
