package ua.gram.munhauzen.repository;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;

public class InventoryRepository {

    public static Inventory findByScenario(GameState gameState, String scenario) {
        for (Inventory o : gameState.inventoryRegistry) {
            if (o.relatedScenario != null) {
                if (o.relatedScenario.size() > 0) {
                    if (o.relatedScenario.contains(scenario)) {
                        return o;
                    }
                }
            }
        }

        return null;
    }

    public static Inventory find(GameState gameState, String id) {

        for (Inventory o : gameState.inventoryRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing inventory " + id);
    }
}
