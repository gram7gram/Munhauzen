package ua.gram.munhauzen.service;

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

    public void onScenarioVisited(String name) {

        try {
            Inventory inventory = InventoryRepository.findByScenario(game.gameState, name);
            if (inventory != null) {
                if (!game.inventoryService.isInInventory(inventory)) {
                    if (inventory.isGlobal()) {
                        game.inventoryService.addGlobalInventory(inventory);
                    } else {
                        game.inventoryService.addSaveInventory(inventory);
                    }
                }
            }

            for (Image img : game.gameState.imageRegistry) {

                if (img.isBonus()) {
                    if (name.equals(img.relatedScenario)) {
                        openBonusImage(img);
                    }
                }

                if (img.isStatue()) {
                    Inventory match = InventoryRepository.find(game.gameState, img.relatedStatue);
                    if (match != null && game.inventoryService.isInInventory(match)) {
                        openStatueImage(img);
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openBonusImage(Image bonus) {

        if (game.gameState.history.viewedImages.contains(bonus.name)) return;

        Log.i(tag, "openBonusImage " + bonus.name);

        game.gameState.history.viewedImages.add(bonus.name);
    }

    private void openStatueImage(Image statue) {

        if (game.gameState.history.viewedImages.contains(statue.name)) return;

        Log.i(tag, "openStatueImage " + statue.name);

        game.gameState.history.viewedImages.add(statue.name);
    }

    public void onImageViewed(String name) {

        for (Image img : game.gameState.imageRegistry) {
            if (img.name.equals(name)) {
                game.gameState.history.viewedImages.add(name);
            }
        }
    }

}
