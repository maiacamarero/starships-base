package starships;

import starships.collidable.Collidable;

import java.util.List;

public class State {
    private List<Collidable> elements;
    private List<Player> players;

    public State(List<Collidable> elements, List<Player> players) {
        this.elements = elements;
        this.players = players;
    }

    public List<Collidable> getElements() {
        return elements;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
