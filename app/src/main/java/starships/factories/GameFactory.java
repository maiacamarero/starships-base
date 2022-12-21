package starships.factories;

import starships.GameConfiguration;
import starships.Juego;

public class GameFactory {
    private NewGameFactory newGameFactory = new NewGameFactory();
    private SavedGameFactory savedGameFactory = new SavedGameFactory();

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
