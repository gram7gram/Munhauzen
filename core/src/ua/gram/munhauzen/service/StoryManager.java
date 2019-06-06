package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Set;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.repository.ScenarioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class StoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final GameState gameState;

    public StoryManager(GameScreen gameScreen, GameState gameState) {
        this.gameScreen = gameScreen;
        this.gameState = gameState;
    }

    public Story create(String optionId) {

        reset();

        Story story = new Story();
        story.id = StringUtils.cid();

        Scenario scenario = ScenarioRepository.find(gameState, optionId);

        findNext(scenario, story);

        story.init();

        String log = "create " + story.id + " x" + story.scenarios.size + "\r\n";

        for (StoryScenario storyScenario : story.scenarios) {
            log += storyScenario.startsAt + "-" + storyScenario.finishesAt + " " + storyScenario.scenario.name + "\r\n";

            for (StoryAudio item : storyScenario.scenario.audio) {
                log += " - audio " + item.audio + " " + item.startsAt + "-" + item.finishesAt + "\r\n";
            }

            for (StoryImage item : storyScenario.scenario.images) {
                log += " - image " + item.image + " " + item.startsAt + "-" + item.finishesAt + "\r\n";
            }
        }

        Log.i(tag, log);

        if (!story.isValid()) {
            throw new IllegalArgumentException("Created story is not valid!");
        }

        return story;
    }

    public void resume() {

        Story story = gameScreen.getStory();
        if (story == null || !story.isValid()) {
            Log.e(tag, "Story is not valid. Resetting");

            Scenario start = ScenarioRepository.findBegin(gameState);

            gameScreen.setStory(
                    create(start.name)
            );
        }

        Log.i(tag, "resume " + gameScreen.getStory().id);

    }

    private void findNext(Scenario from, Story story) {

        Log.i(tag, "findNext " + from.name + " #" + story.scenarios.size);

        Set<String> inventory = gameState.history.activeSave.getUniqueInventory();

        StoryScenario storyScenario = new StoryScenario();
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        story.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {
                if (isDecisionAvailable(decision, inventory)) {

                    Scenario next = ScenarioRepository.find(gameState, decision.scenario);

                    findNext(next, story);
                }
            }
        }
    }

    public void update(float progress, int duration) {

        Story story = gameState.history.activeSave.story;

        story.update(progress, duration);
    }

    public void startLoadingResources() {

        Story story = gameState.history.activeSave.story;

        StoryScenario option = story.currentScenario;
        if (option == null) return;

        final StoryAudio optionAudio = option.currentAudio;
        if (optionAudio != null) {
            gameScreen.audioService.prepare(optionAudio, new Timer.Task() {
                @Override
                public void run() {
                    try {
                        gameScreen.audioService.onPrepared(optionAudio);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });

            if (optionAudio.next != null) {
                gameScreen.audioService.prepare(optionAudio.next, new Timer.Task() {
                    @Override
                    public void run() {

                    }
                });
            }
        }

        final StoryImage optionImage = option.currentImage;
        if (optionImage != null) {
            gameScreen.imageService.prepare(optionImage, new Timer.Task() {
                @Override
                public void run() {
                    try {
                        gameScreen.imageService.onPrepared(optionImage);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });

            if (optionImage.next != null) {
                gameScreen.imageService.prepare(optionImage.next, new Timer.Task() {
                    @Override
                    public void run() {

                    }
                });
            }
        }

    }

    public void onCompleted() {

        Story story = gameState.history.activeSave.story;
        Log.i(tag, "onCompleted " + story.id);

        for (StoryScenario storyScenario : story.scenarios) {

            Inventory inventory = InventoryRepository.findByScenario(gameState, storyScenario.scenario.name);
            if (inventory != null) {
                if (!gameScreen.inventoryService.isInInventory(inventory)) {
                    if (inventory.isGlobal()) {
                        gameScreen.inventoryService.addGlobalInventory(inventory);
                    } else {
                        gameScreen.inventoryService.addInventory(inventory);
                    }
                }
            }
        }

        Set<String> inventory = gameState.history.activeSave.getUniqueInventory();

        for (StoryAudio audio : story.currentScenario.scenario.audio) {
            if (audio.player != null) {
                audio.player.pause();
            }
        }

        ArrayList<Decision> availableDecisions = new ArrayList<>();
        if (story.currentScenario.scenario.decisions != null) {
            for (Decision decision : story.currentScenario.scenario.decisions) {
                if (isDecisionAvailable(decision, inventory)) {
                    availableDecisions.add(decision);
                }
            }
        }

        if (gameScreen.scenarioFragment != null) {
            gameScreen.scenarioFragment.dispose();
            gameScreen.scenarioFragment = null;
        }

        if (gameScreen.gameLayers.storeDecisionsLayer != null) {
            gameScreen.gameLayers.storeDecisionsLayer.remove();
            gameScreen.gameLayers.storeDecisionsLayer = null;
        }

        if (availableDecisions.size() > 0) {
            gameScreen.scenarioFragment = new ScenarioFragment(gameScreen);

            gameScreen.progressBarFragment.fadeIn();
            gameScreen.gameLayers.setStoryDecisionsLayer(
                    gameScreen.scenarioFragment.create(availableDecisions)
            );
        }
    }

    public void reset() {

        Save save = gameState.history.activeSave;

        Story story = save.story;
        if (story == null) return;

        Log.i(tag, "reset " + story.id);

        if (!save.storyStack.contains(story)) {
            save.storyStack.push(story);
        }

        for (StoryScenario storyScenario : story.scenarios) {

            for (StoryAudio audio : storyScenario.scenario.audio) {
                String resource = audio.getResource();
                if (gameScreen.assetManager.isLoaded(resource, Music.class)) {
                    if (gameScreen.assetManager.getReferenceCount(resource) == 0) {
                        gameScreen.assetManager.unload(resource);
                    }
                }
            }

            for (StoryImage image : storyScenario.scenario.images) {
                String resource = image.getResource();
                if (gameScreen.assetManager.isLoaded(resource, Texture.class)) {
                    if (gameScreen.assetManager.getReferenceCount(resource) == 0) {
                        gameScreen.assetManager.unload(resource);
                    }
                }
            }

            storyScenario.reset();
        }

        gameScreen.assetManager.finishLoading();

        story.reset();
    }

    private boolean isDecisionAvailable(Decision decision, Set<String> inventory) {
        boolean hasRequired = true;
        if (decision.inventoryRequired != null) {
            if (decision.inventoryRequired.size > 0) {
                for (String item : decision.inventoryRequired) {
                    if (!inventory.contains(item)) {
                        hasRequired = false;
                        break;
                    }
                }
            }
        }

        boolean hasAbsent = false;
        if (decision.inventoryAbsent != null) {
            if (decision.inventoryAbsent.size > 0) {
                for (String item : decision.inventoryAbsent) {
                    if (inventory.contains(item)) {
                        hasAbsent = true;
                        break;
                    }
                }
            }
        }

        return hasRequired && !hasAbsent;
    }
}