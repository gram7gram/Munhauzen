package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Set;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.repository.ScenarioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExternalFiles;
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

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

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

        try {
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
                gameScreen.externalImageService.prepare(optionImage, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            gameScreen.externalImageService.onPrepared(optionImage);
                        } catch (Throwable e) {
                            Log.e(tag, e);
                        }
                    }
                });

                if (optionImage.next != null) {
                    gameScreen.externalImageService.prepare(optionImage.next, new Timer.Task() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void onCompleted() {

        Story story = gameState.history.activeSave.story;
        Log.i(tag, "onCompleted " + story.id);

        for (StoryScenario storyScenario : story.scenarios) {

            Inventory inventory = InventoryRepository.findByScenario(gameState, storyScenario.scenario.name);
            if (inventory != null) {
                if (!gameScreen.game.inventoryService.isInInventory(inventory)) {
                    if (inventory.isGlobal()) {
                        gameScreen.game.inventoryService.addGlobalInventory(inventory);
                    } else {
                        gameScreen.game.inventoryService.addInventory(inventory);
                    }
                }
            }
        }

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

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

        if (availableDecisions.size() > 0) {

            if (gameScreen.scenarioFragment == null) {
                gameScreen.scenarioFragment = new ScenarioFragment(gameScreen);
            }

            gameScreen.progressBarFragment.fadeIn();

            gameScreen.scenarioFragment.create(availableDecisions);

            gameScreen.gameLayers.setStoryDecisionsLayer(
                    gameScreen.scenarioFragment
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

            String audioPath = ExternalFiles.getExpansionAudioDir().path();
            String imagePath = ExternalFiles.getExpansionImagesDir().path();

            for (StoryAudio storyAudio : storyScenario.scenario.audio) {

                try {
                    Audio audio = AudioRepository.find(gameScreen.game.gameState, storyAudio.audio);

                    String resource = audioPath + "/" + audio.file;

                    if (gameScreen.audioService.assetManager.isLoaded(resource, Music.class)) {
                        if (gameScreen.audioService.assetManager.getReferenceCount(resource) == 0) {
                            gameScreen.audioService.assetManager.unload(resource);
                        }
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            for (StoryImage storyImage : storyScenario.scenario.images) {
                try {

                    if (storyImage.image.equals(ImageRepository.LAST)) continue;

                    Image image = ImageRepository.find(gameScreen.game.gameState, storyImage.image);

                    String resource = imagePath + "/" + image.file;
                    if (gameScreen.externalImageService.assetManager.isLoaded(resource, Texture.class)) {
                        if (gameScreen.externalImageService.assetManager.getReferenceCount(resource) == 0) {
                            gameScreen.externalImageService.assetManager.unload(resource);
                        }
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
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