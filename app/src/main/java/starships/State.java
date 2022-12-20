package starships;

import starships.collidable.Collidable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private Map<String, Collidable> elementsMap;
    private List<Player> players;
    private List<String> modifiedCollidable;

    public State(List<Collidable> elements, List<Player> players) {
        this.players = players;
        this.elementsMap = rancidMethodBecauseJavaSucks(elements);
        this.modifiedCollidable = new ArrayList<>();
    }

    private State(Map<String, Collidable> elementsMap, List<Player> players, List<String> modifiedCollidable) {
        this.elementsMap = elementsMap;
        this.players = players;
        this.modifiedCollidable = modifiedCollidable;
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
        modifiedCollidable.add(collidable.getId());
        return new State(newElementsMap, players, modifiedCollidable);
    }

    public List<Collidable> getElements() {
        return elementsMap.values().stream().toList();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<String, Collidable> getElementsMap() {
        return elementsMap;
    }

    public List<String> getModifiedCollidable() {
        return modifiedCollidable;
    }

    public State copy(){
        Map<String, Collidable> newElementsMap = rancidMethodBecauseJavaSucks(elementsMap.values().stream().toList());
        List<Player> newPlayers = new ArrayList<>(players);
        List<String> newModifiedCollidable = new ArrayList<>(modifiedCollidable);
        return new State(newElementsMap, newPlayers, newModifiedCollidable);
    }
}
