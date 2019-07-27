package ua.gram.munhauzen.interaction.timer2;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.Timer2Interaction;
import ua.gram.munhauzen.interaction.timer2.fragment.Timer2ScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class Timer2StoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final Timer2Interaction interaction;

    public Timer2Story timerStory;

    public Timer2StoryManager(GameScreen gameScreen, Timer2Interaction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public Timer2Story create(String begin) {

        reset();

        Timer2Story story = new Timer2Story();
        story.id = StringUtils.cid();

        for (Timer2Scenario timerScenario : interaction.scenarioRegistry) {
            if (timerScenario.name.equals(begin)) {
                findNext(timerScenario, story);
                break;
            }
        }

        story.init();

        return story;
    }

    public void resume() {

        if (timerStory == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (Timer2Scenario timerScenario : interaction.scenarioRegistry) {
                if (timerScenario.isBegin) {
                    timerStory = create(timerScenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + timerStory.id);

    }

    private void findNext(Timer2Scenario from, Timer2Story timerStory) {

        Log.i(tag, "findNext " + from.name + " #" + timerStory.scenarios.size);

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        Timer2StoryScenario storyScenario = new Timer2StoryScenario(gameScreen.game.gameState);
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        timerStory.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {
                if (isDecisionAvailable(decision, inventory)) {

                    for (Timer2Scenario hareScenario : interaction.scenarioRegistry) {
                        if (hareScenario.name.equals(decision.scenario)) {

                            findNext(hareScenario, timerStory);

                            break;
                        }
                    }


                }
            }
        }
    }

    public void update(float progress, int duration) {
        timerStory.update(progress, duration);
    }

    public void startLoadingAudio() {
        Timer2Story story = interaction.storyManager.timerStory;

        Timer2StoryScenario scenario = story.currentScenario;
        if (scenario == null) return;

        try {

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
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void startLoadingImages() {
        Timer2Story story = interaction.storyManager.timerStory;

        Timer2StoryScenario scenario = story.currentScenario;
        if (scenario == null) return;


        try {
            final StoryImage image = scenario.currentImage;
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

    public void startLoadingResources() {

        startLoadingAudio();

        startLoadingImages();
    }

    public void onCompleted() {

        startLoadingImages();

        Log.i(tag, "onCompleted " + timerStory.id);

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        for (StoryAudio audio : timerStory.currentScenario.scenario.audio) {
            if (audio.player != null) {
                audio.player.pause();
            }
        }

        if (timerStory.currentScenario.scenario.isExit) {

            Log.i(tag, "Exit reached");

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
        if (timerStory.currentScenario.scenario.decisions != null) {
            for (Decision decision : timerStory.currentScenario.scenario.decisions) {
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
                interaction.scenarioFragment = new Timer2ScenarioFragment(gameScreen, interaction);
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

        if (timerStory == null) return;

        Log.i(tag, "reset " + timerStory.id);

        for (Timer2StoryScenario storyScenario : timerStory.scenarios) {
            storyScenario.reset();
        }

        timerStory.reset();
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