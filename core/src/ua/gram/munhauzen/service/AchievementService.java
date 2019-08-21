package ua.gram.munhauzen.service;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.utils.Log;

public class AchievementService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public AchievementService(MunhauzenGame game) {
        this.game = game;
    }

    public void onInventoryAdded(Inventory inventory) {

        try {
            if (game.inventoryService.isInInventory(inventory)) return;

            Log.i(tag, "onInventoryAdded " + inventory.name);

            game.inventoryService.addInventory(inventory);

            for (Image img : game.gameState.imageRegistry) {
                if (img.isStatue()) {
                    if (inventory.name.equals(img.relatedStatue)) {

                        Log.e(tag, "onInventoryAdded adds statue " + img.name);
                        onImageViewed(img);
                    }
                }
            }

            checkRelatedInventory();

        } catch (Throwable e) {
            Log.e(tag, e);

            game.onCriticalError(e);
        }
    }

    private void checkRelatedInventory() {

        ArrayList<Inventory> newItems = new ArrayList<>();

        for (Inventory item : game.gameState.inventoryRegistry) {

            if (game.inventoryService.isInInventory(item)) continue;

            if (item.relatedInventory != null && item.relatedInventory.size() > 0) {
                boolean hasAll = true;

                for (String name : item.relatedInventory) {
                    Inventory match = InventoryRepository.find(game.gameState, name);
                    if (!game.inventoryService.isInInventory(match)) {
                        hasAll = false;
                        break;
                    }
                }

                if (hasAll) {
                    newItems.add(item);
                }
            }
        }

        for (Inventory newItem : newItems) {
            Log.e(tag, "checkRelatedInventory adds inventory " + newItem.name);

            onInventoryAdded(newItem);
        }

    }

    public void onScenarioVisited(String name) {

        try {
            Inventory inventory = InventoryRepository.findByScenario(game.gameState, name);
            if (inventory != null) {
                if (!game.inventoryService.isInInventory(inventory)) {
                    onInventoryAdded(inventory);
                }
            }

            for (Image img : game.gameState.imageRegistry) {

                if (img.isBonus()) {
                    if (name.equals(img.relatedScenario)) {

                        Log.e(tag, "onScenarioVisited adds bonus " + img.name);
                        onImageViewed(img);
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            game.onCriticalError(e);
        }
    }

    public void onImageViewed(Image img) {

        if (game.gameState.history.viewedImages.contains(img.name)) return;

        Log.i(tag, "onImageViewed " + img.name);

        game.gameState.history.viewedImages.add(img.name);
    }

}
