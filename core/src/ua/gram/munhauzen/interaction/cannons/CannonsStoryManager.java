package ua.gram.munhauzen.interaction.cannons;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.interaction.cannons.fragment.CannonsScenarioFragment;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class CannonsStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final CannonsInteraction interaction;

    public CannonsStory story;

    public CannonsStoryManager(GameScreen gameScreen, CannonsInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public CannonsStory create(String begin) {

        reset();

        int wormCount = 0;

        try {

            Inventory burnWorm = InventoryRepository.find(interaction.gameScreen.game.gameState, "BURN_WORM");
            Inventory floodWorm = InventoryRepository.find(interaction.gameScreen.game.gameState, "FLOOD_WORM");
            Inventory eatWorm = InventoryRepository.find(interaction.gameScreen.game.gameState, "EAT_WORM");
            Inventory epydemy = InventoryRepository.find(interaction.gameScreen.game.gameState, "EPYDEMY");

            boolean hasBurnWorm = interaction.gameScreen.game.inventoryService.isInInventory(burnWorm);
            boolean hasFloodWorm = interaction.gameScreen.game.inventoryService.isInInventory(floodWorm);
            boolean hasEatWorm = interaction.gameScreen.game.inventoryService.isInInventory(eatWorm);

            if (hasBurnWorm) ++wormCount;
            if (hasFloodWorm) ++wormCount;
            if (hasEatWorm) ++wormCount;

            if (wormCount == 3) {
                interaction.gameScreen.onInventoryAdded(epydemy);
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
            return null;
        }

        CannonsStory story = new CannonsStory();
        story.id = begin;

        String log = "create from " + story.id;

        for (CannonsScenario scenario : interaction.scenarioRegistry) {
            if (scenario.name.equals(begin)) {
                findNext(scenario, story, wormCount);
                break;
            }
        }

        story.init();

        for (CannonsStoryScenario scenario : story.scenarios) {
            log += "\n => " + scenario.scenario.name
                    + " " + scenario.duration
                    + " " + scenario.startsAt + " - " + scenario.finishesAt;
        }

        Log.i(tag, log);

        return story;
    }

    public void resume() {

        if (story == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (CannonsScenario scenario : interaction.scenarioRegistry) {
                if (scenario.isBegin) {
                    story = create(scenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + story.id);
    }

    private void findNext(CannonsScenario from, CannonsStory story, int wormCount) {

        Log.i(tag, "findNext " + from.name + " #" + story.scenarios.size());

        if ("aworm_check_a".equals(from.name)) {
            String decision;

            switch (wormCount) {
                case 3:
                    decision = "aworm_a_170";
                    break;
                case 2:
                    decision = "aworm_a_133";
                    break;
                default:
                    decision = "aworm_a_82";
            }

            for (CannonsScenario scenario : interaction.scenarioRegistry) {
                if (scenario.name.equals(decision)) {
                    findNext(scenario, story, wormCount);
                    return;
                }
            }
        }

        if ("aworm_check_c".equals(from.name)) {
            String decision;

            switch (wormCount) {
                case 3:
                    decision = "aworm_c_170";
                    break;
                case 2:
                    decision = "aworm_c_133";
                    break;
                default:
                    decision = "aworm_c_82";
            }

            for (CannonsScenario scenario : interaction.scenarioRegistry) {
                if (scenario.name.equals(decision)) {
                    findNext(scenario, story, wormCount);
                    return;
                }
            }
        }

        if ("aworm_check_d".equals(from.name)) {
            String decision;

            switch (wormCount) {
                case 3:
                    decision = "aworm_d_170";
                    break;
                case 2:
                    decision = "aworm_d_133";
                    break;
                default:
                    decision = "aworm_d_82";
            }

            for (CannonsScenario scenario : interaction.scenarioRegistry) {
                if (scenario.name.equals(decision)) {
                    findNext(scenario, story, wormCount);
                    return;
                }
            }
        }

        CannonsStoryScenario storyScenario = new CannonsStoryScenario(gameScreen.game.gameState);
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        story.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {

                for (CannonsScenario scenario : interaction.scenarioRegistry) {
                    if (scenario.name.equals(decision.scenario)) {

                        findNext(scenario, story, wormCount);

                        break;
                    }
                }

            }
        }
    }

    public void update(float progress, int duration) {
        story.update(progress, duration);
    }

    public void startLoadingImages() {

        try {

            displayCurrentImage();

            if (story == null) return;

            CannonsStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

            final CannonsStoryImage image = scenario.currentImage;
            if (image != null) {
                if (image.next != null) {
                    interaction.imageService.prepare(image.next, new Timer.Task() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void startLoadingAudio() {

        try {

            if (story == null) return;

            CannonsStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

            final StoryAudio audio = scenario.currentAudio;
            if (audio != null) {
                gameScreen.audioService.prepare(audio, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            if (gameScreen.audioService != null)
                                gameScreen.audioService.playAudio(audio);
                        } catch (Throwable e) {
                            Log.e(tag, e);

                            interaction.gameScreen.onCriticalError(e);
                        }
                    }
                });

                if (audio.next != null) {
                    gameScreen.audioService.prepare(audio.next, new Timer.Task() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void displayCurrentImage() {

        if (story == null) return;

        CannonsStoryScenario scenario = story.currentScenario;
        if (scenario == null) return;

        try {

            StoryImage image = scenario.currentImage;
            if (image != null) {
                if (image.isLocked)
                    interaction.imageService.prepareAndDisplay(image);
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void startLoadingResources() {

        startLoadingAudio();

        startLoadingImages();
    }

    public void onCompleted() {
        try {
            displayCurrentImage();

            Log.i(tag, "onCompleted " + story.id);

            GameState gameState = gameScreen.game.gameState;

            gameState.addVisitedScenario(story.id);

            for (CannonsStoryScenario storyScenario : story.scenarios) {
                gameState.addVisitedScenario(storyScenario.scenario.name);
            }

            for (StoryAudio audio : story.currentScenario.scenario.audio) {
                if (audio.player != null) {
                    audio.player.pause();
                }
            }

            if (story.currentScenario.scenario.isExit) {

                if (interaction.progressBarFragment != null) {
                    interaction.progressBarFragment.destroy();
                    interaction.progressBarFragment = null;
                }

                if (interaction.scenarioFragment != null) {
                    interaction.scenarioFragment.destroy();
                    interaction.scenarioFragment = null;
                }

                complete();

                return;
            }

            ArrayList<Decision> availableDecisions = new ArrayList<>();
            if (story.currentScenario.scenario.decisions != null) {
                for (Decision decision : story.currentScenario.scenario.decisions) {
                    //if (isDecisionAvailable(decision, inventory))
                    availableDecisions.add(decision);
                }
            }

            if (availableDecisions.size() > 0) {

                Collections.sort(availableDecisions, new Comparator<Decision>() {
                    @Override
                    public int compare(Decision a, Decision b) {
                        if (a.order > b.order) return 1;

                        if (a.order < b.order) return -1;

                        return 0;
                    }
                });

                if (interaction.scenarioFragment == null) {
                    interaction.scenarioFragment = new CannonsScenarioFragment(gameScreen, interaction);
                }

                interaction.progressBarFragment.fadeIn();

                interaction.scenarioFragment.create(availableDecisions);

                gameScreen.gameLayers.setStoryDecisionsLayer(
                        interaction.scenarioFragment
                );

                interaction.scenarioFragment.fadeIn();
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void complete() {

        Log.i(tag, "complete");

        try {
            gameScreen.interactionService.complete();

            gameScreen.interactionService.findStoryAfterInteraction();

            interaction.gameScreen.restoreProgressBarIfDestroyed();
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void reset() {

        if (story == null) return;

        Log.i(tag, "reset " + story.id);

        for (CannonsStoryScenario storyScenario : story.scenarios) {
            storyScenario.reset();
        }

        story.reset();
    }
}