package starships.factories;

import starships.GameConfiguration;
import starships.Juego;

public class GameFactory {
    private final NewGameFactory newGameFactory = new NewGameFactory();
    private final SavedGameFactory savedGameFactory = new SavedGameFactory();

    public Juego generate(boolean resumeGame, GameConfiguration configuration){
        if (resumeGame){
            //paused = false;
            //loadPoints();
            return savedGameFactory.generate(configuration);
        }else {
            //paused = false;
            //loadPoints();
            return newGameFactory.ganerate(configuration);
        }
    }
}
