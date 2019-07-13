package ua.gram.munhauzen.interaction.hare;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.hare.fragment.HareScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class HareStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final HareInteraction interaction;

    public HareStory hareStory;

    public HareStoryManager(GameScreen gameScreen, HareInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public HareStory create(String begin) {

        reset();

        HareStory story = new HareStory();
        story.id = StringUtils.cid();

        for (HareScenario hareScenario : interaction.scenarioRegistry) {
            if (hareScenario.name.equals(begin)) {
                findNext(hareScenario, story);
                break;
            }
        }

        story.init();

        return story;
    }

    public void resume() {

        if (hareStory == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (HareScenario hareScenario : interaction.scenarioRegistry) {
                if (hareScenario.isBegin) {
                    hareStory = create(hareScenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + hareStory.id);

    }

    private void findNext(HareScenario from, HareStory hareStory) {

        Log.i(tag, "findNext " + from.name + " #" + hareStory.scenarios.size);

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        HareStoryScenario storyScenario = new HareStoryScenario(gameScreen.game.gameState);
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        hareStory.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {
                if (isDecisionAvailable(decision, inventory)) {

                    for (HareScenario hareScenario : interaction.scenarioRegistry) {
                        if (hareScenario.name.equals(decision.scenario)) {

                            findNext(hareScenario, hareStory);

                            break;
                        }
                    }


                }
            }
        }
    }

    public void update(float progress, int duration) {
        hareStory.update(progress, duration);
    }

    public void startLoadingResources() {
        try {
            HareStory story = interaction.storyManager.hareStory;

            HareStoryScenario scenario = story.currentScenario;
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
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void onCompleted() {

        Log.i(tag, "onCompleted " + hareStory.id);

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        for (StoryAudio audio : hareStory.currentScenario.scenario.audio) {
            if (audio.player != null) {
                audio.player.pause();
            }
        }

        if (hareStory.currentScenario.scenario.isExit) {

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
        if (hareStory.currentScenario.scenario.decisions != null) {
            for (Decision decision : hareStory.currentScenario.scenario.decisions) {
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
                interaction.scenarioFragment = new HareScenarioFragment(gameScreen, interaction);
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

        if (hareStory == null) return;

        Log.i(tag, "reset " + hareStory.id);

        for (HareStoryScenario storyScenario : hareStory.scenarios) {
            storyScenario.reset();
        }

        hareStory.reset();
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