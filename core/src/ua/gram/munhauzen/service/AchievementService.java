package ua.gram.munhauzen.service;

import java.util.ArrayList;
import java.util.Stack;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
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

            boolean isAdded = game.inventoryService.addInventory(inventory);
            if (isAdded && inventory.isStatue) {
                if (game.gameState.achievementState.achievementsToDisplay == null) {
                    game.gameState.achievementState.achievementsToDisplay = new Stack<>();
                }
                game.gameState.achievementState.achievementsToDisplay.push(inventory.name);
            } else {
                Log.e(tag, "no added " + inventory.name);
            }

            for (Image img : game.gameState.imageRegistry) {
                if (img.isStatue()) {
                    if (inventory.name.equals(img.relatedStatue)) {

                        Log.e(tag, "onInventoryAdded adds statue " + img.name);
                        onImageViewed(img);
                    }
                }
            }

            checkRelatedInventory();

            checkMenuInventory();

        } catch (Throwable e) {
            Log.e(tag, e);

            game.navigator.onCriticalError(e);
        }
    }

    private void checkMenuInventory() {

        if (game.gameState.achievementState.areAllMenuInventoryUnlocked) return;

        boolean hasAll = true;

        for (Inventory item : game.gameState.inventoryRegistry) {
            if (item.isMenu) {
                if (!game.inventoryService.isInInventory(item)) {
                    hasAll = false;
                    break;
                }
            }
        }

        if (hasAll) {

            game.gameState.achievementState.areAllMenuInventoryUnlocked = true;

            game.sfxService.onAllMenuInventoryUnlocked();
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

    public void onScenarioVisited(Scenario scenario) {

        try {
            Inventory inventory = InventoryRepository.findByScenario(game.gameState, scenario.name);
            if (inventory != null) {
                if (!game.inventoryService.isInInventory(inventory)) {
                    onInventoryAdded(inventory);
                }
            }

            for (Image img : game.gameState.imageRegistry) {

                if (img.isBonus()) {
                    if (scenario.name.equals(img.relatedScenario)) {

                        if (!game.gameState.history.viewedImages.contains(img.name)) {

                            Log.e(tag, "onScenarioVisited adds bonus " + img.name);
                            onImageViewed(img);
                        }
                    }
                }
            }

            for (StoryImage storyImage : scenario.images) {
                onImageViewed(storyImage.image);
            }

            for (StoryAudio storyAudio : scenario.audio) {
                onAudioListened(storyAudio.audio);
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            game.navigator.onCriticalError(e);
        }
    }

    public void onImageViewed(Image img) {
        onImageViewed(img.name);
    }

    private void onImageViewed(String name) {

        if (game.gameState.history.viewedImages.contains(name)) return;

        Log.i(tag, "onImageViewed " + name);

        game.gameState.history.viewedImages.add(name);

        if (!game.gameState.achievementState.areAllImagesUnlocked) {

            boolean hasAll = true;
            for (Image a : game.gameState.getGalleryImages()) {
                if (!game.gameState.history.viewedImages.contains(a.name)) {
                    hasAll = false;
                    break;
                }
            }

            if (hasAll) {

                game.gameState.achievementState.areAllImagesUnlocked = true;

                if (game.gameState.achievementState.areAllGoofsUnlocked) {
                    game.sfxService.onAllGoofsAndImagesUnlocked();
                } else {
                    game.sfxService.onAllImagesUnlocked();
                }
            }
        }
    }

    public void onAudioListened(Audio audio) {
        onAudioListened(audio.name);
    }

    private void onAudioListened(String name) {

        if (game.gameState.history.listenedAudio.contains(name)) return;

        Log.i(tag, "onAudioListened " + name);

        game.gameState.history.listenedAudio.add(name);

        for (AudioFail audioFail : game.gameState.audioFailRegistry) {
            if (audioFail.audio.equals(name)) {

                if (!game.gameState.history.openedFails.contains(name)) {

                    Log.e(tag, "onAudioListened adds fail " + audioFail.name);

                    onFailOpened(audioFail);
                }
            }
        }
    }

    public void onFailOpened(AudioFail fail) {
        game.gameState.history.openedFails.add(fail.name);

        if (!game.gameState.achievementState.areAllGoofsUnlocked) {

            boolean hasAll = true;
            for (AudioFail a : game.gameState.audioFailRegistry) {
                if (!a.isFailOpenedOnComplete) {
                    if (!game.gameState.history.openedFails.contains(a.name)) {
                        hasAll = false;
                        break;
                    }
                }
            }

            if (hasAll) {
                for (AudioFail a : game.gameState.audioFailRegistry) {
                    if (a.isFailOpenedOnComplete) {
                        game.gameState.history.openedFails.add(a.name);
                    }
                }

                game.gameState.achievementState.areAllGoofsUnlocked = true;

                if (game.gameState.achievementState.areAllImagesUnlocked) {
                    game.sfxService.onAllGoofsAndImagesUnlocked();
                } else {
                    game.sfxService.onAllGoofsUnlocked();
                }
            }
        }
    }
}
