package ua.gram.munhauzen.interaction.generals;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class GeneralsStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final GeneralsInteraction interaction;

    public GeneralsStory generalsStory;

    public GeneralsStoryManager(GameScreen gameScreen, GeneralsInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public GeneralsStory create(String begin) {

        reset();

        GeneralsStory story = new GeneralsStory();
        story.id = StringUtils.cid();

        for (GeneralsScenario hareScenario : interaction.scenarioRegistry) {
            if (hareScenario.name.equals(begin)) {
                findNext(hareScenario, story);
                break;
            }
        }

        story.init();

        return story;
    }

    public void resume() {

        if (generalsStory == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (GeneralsScenario scenario : interaction.scenarioRegistry) {
                if (scenario.isBegin) {
                    generalsStory = create(scenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + generalsStory.id);

    }

    private void findNext(GeneralsScenario from, GeneralsStory story) {

        Log.i(tag, "findNext " + from.name + " #" + story.scenarios.size);

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        GeneralsStoryScenario storyScenario = new GeneralsStoryScenario(gameScreen.game.gameState);
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        story.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {
                if (isDecisionAvailable(decision, inventory)) {

                    for (GeneralsScenario scenario : interaction.scenarioRegistry) {
                        if (scenario.name.equals(decision.scenario)) {

                            findNext(scenario, story);

                            break;
                        }
                    }


                }
            }
        }
    }

    public void update(float progress, int duration) {
        generalsStory.update(progress, duration);
    }

    public void startLoadingResources() {

        try {
            GeneralsStoryScenario scenario = generalsStory.currentScenario;
            if (scenario == null) return;

            final StoryAudio audio = scenario.currentAudio;
            if (audio != null) {
                gameScreen.audioService.prepare(audio, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            gameScreen.audioService.onPrepared(audio);
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

            final GeneralsStoryImage image = scenario.currentImage;
            if (image != null) {
                interaction.imageService.prepare(image, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            interaction.imageService.onPrepared(image);
                        } catch (Throwable e) {
                            Log.e(tag, e);

                            interaction.gameScreen.onCriticalError(e);
                        }
                    }
                });

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

    public void onCompleted() {

        Log.i(tag, "onCompleted " + generalsStory.id);

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        for (StoryAudio audio : generalsStory.currentScenario.scenario.audio) {
            if (audio.player != null) {
                audio.player.pause();
            }
        }

        if (generalsStory.currentScenario.scenario.isExit) {

            if (interaction.progressBarFragment.canFadeOut()) {
                interaction.progressBarFragment.fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        complete();
                    }
                });
            } else {
                complete();
            }

            return;
        }

        ArrayList<Decision> availableDecisions = new ArrayList<>();
        if (generalsStory.currentScenario.scenario.decisions != null) {
            for (Decision decision : generalsStory.currentScenario.scenario.decisions) {
                if (isDecisionAvailable(decision, inventory)) {
                    availableDecisions.add(decision);
                }
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
                interaction.scenarioFragment = new GeneralsScenarioFragment(gameScreen, interaction);
            }

            interaction.progressBarFragment.fadeIn();

            interaction.scenarioFragment.create(availableDecisions);

            gameScreen.gameLayers.setStoryDecisionsLayer(
                    interaction.scenarioFragment
            );
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

        if (generalsStory == null) return;

        Log.i(tag, "reset " + generalsStory.id);

        for (GeneralsStoryScenario storyScenario : generalsStory.scenarios) {
            storyScenario.reset();
        }

        generalsStory.reset();
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