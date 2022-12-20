package starships;

import starships.collidable.Collidable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private Map<String, Collidable> elementsMap;
    private List<Player> players;

    public State(List<Collidable> elements, List<Player> players) {
        this.players = players;
        this.elementsMap = rancidMethodBecauseJavaSucks(elements);
    }

    public State(Map<String, Collidable> elementsMap, List<Player> players) {
        this.elementsMap = elementsMap;
        this.players = players;
    }

    private Map<String, Collidable> rancidMethodBecauseJavaSucks(List<Collidable> elements) {
        Map<String, Collidable> elementsMap = new HashMap<>();
        for (Collidable element : elements) {
            elementsMap.put(element.getId(), element);
        }
        return elementsMap;
    }

    public State setCollidable(Collidable collidable){
        Map<String, Collidable> newElementsMap = rancidMethodBecauseJavaSucks(elementsMap.values().stream().toList());
        newElementsMap.put(collidable.getId(), collidable);
        return new State(elementsMap, players);
    }

    public List<Collidable> getElements() {
        return elementsMap.values().stream().toList();
    }

    public List<Player> getPlayers() {
        return players;
    }
}
