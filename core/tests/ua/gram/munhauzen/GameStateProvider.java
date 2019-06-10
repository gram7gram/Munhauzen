package ua.gram.munhauzen;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.history.Save;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameStateProvider {

    public static GameState create() {
        GameState state = new GameState();

        History history = new History();
        history.activeSave = new Save();

        state.history = history;

        return state;
    }
}