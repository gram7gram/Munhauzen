package ua.gram.munhauzen.repository;


import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Scenario;

public class ScenarioRepository {

    public static Scenario findBegin(GameState gameState) {

        if (gameState.developmentScenario != null) {
            return find(gameState, gameState.developmentScenario);
        }

        for (Scenario o : gameState.scenarioRegistry) {
            if (o.isBegin) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing begin scenario");
    }

    public static Scenario find(GameState gameState, String id) {

        for (Scenario o : gameState.scenarioRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing scenario " + id);
    }
}
