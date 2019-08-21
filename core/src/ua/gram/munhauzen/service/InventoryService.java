package ua.gram.munhauzen.service;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashSet;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InventoryService {

    final String tag = getClass().getSimpleName();
    final GameState gameState;

    public InventoryService(GameState gameState) {
        this.gameState = gameState;
    }

    public void addGlobalInventory(Inventory inventory) {

        if (isInInventory(inventory)) return;

        if (!inventory.isGlobal()) {
            throw new GdxRuntimeException("Inventory " + inventory.name + " is not global");
        }

        gameState.history.globalInventory.add(inventory.name);
    }

    public void addSaveInventory(Inventory inventory) {

        if (isInInventory(inventory)) return;

        if (inventory.isGlobal()) {
            throw new GdxRuntimeException("Inventory " + inventory.name + " is global");
        }

        gameState.history.activeSave.inventory.add(inventory.name);
    }

    public void addInventory(Inventory item) {
        if (item.isGlobal()) {
            addGlobalInventory(item);
        } else {
            addSaveInventory(item);
        }
    }

    public boolean isInInventory(Inventory item) {
        if (item.isGlobal()) {
            return gameState.history.globalInventory.contains(item.name);
        }

        return gameState.history.activeSave.inventory.contains(item.name);
    }

    public void remove(Inventory item) {

        Log.i(tag, "remove " + item.name);

        if (item.isGlobal()) {
            gameState.history.globalInventory.remove(item.name);
            return;
        }

        gameState.history.activeSave.inventory.remove(item.name);
    }

    public HashSet<String> getAllInventory() {
        HashSet<String> values = new HashSet<>();

        values.addAll(gameState.history.activeSave.inventory);
        values.addAll(gameState.history.globalInventory);

        values.add("DEFAULT");

        return values;
    }

}
