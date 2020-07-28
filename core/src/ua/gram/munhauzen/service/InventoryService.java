package ua.gram.munhauzen.service;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashSet;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.History;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InventoryService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public InventoryService(MunhauzenGame game) {
        this.game = game;
    }

    public boolean addGlobalInventory(Inventory inventory) {

        if (isInInventory(inventory)) return false;

        if (!inventory.isGlobal()) {
            throw new GdxRuntimeException("Inventory " + inventory.name + " is not global");
        }

        game.gameState.history.globalInventory.add(inventory.name);

        return true;
    }

    public boolean addSaveInventory(Inventory inventory) {

        if (isInInventory(inventory)) return false;

        if (inventory.isGlobal()) {
            throw new GdxRuntimeException("Inventory " + inventory.name + " is global");
        }

        game.gameState.activeSave.inventory.add(inventory.name);

        return true;
    }

    public boolean addInventory(Inventory item) {
        if (item.isGlobal()) {
            return addGlobalInventory(item);
        } else {
            return addSaveInventory(item);
        }
    }

    public boolean isInInventory(Inventory item) {
        if (item.isGlobal()) {
            return game.gameState.history.globalInventory.contains(item.name);
        }

        return game.gameState.activeSave.inventory.contains(item.name);
    }

    public void remove(Inventory item) {

        Log.i(tag, "remove " + item.name);

        if (item.isGlobal()) {
            game.gameState.history.globalInventory.remove(item.name);
            return;
        }

        game.gameState.activeSave.inventory.remove(item.name);
    }

    public HashSet<String> getAllInventory() {
        HashSet<String> values = new HashSet<>();

        if (game.gameState != null && game.gameState.history != null && game.gameState.activeSave != null) {

            History history = game.gameState.history;

            if (game.gameState.activeSave.inventory != null) {
                values.addAll(game.gameState.activeSave.inventory);
            }

            if (history.globalInventory != null) {
                values.addAll(history.globalInventory);
            }
        }

        values.add("DEFAULT");

        return values;
    }

}
