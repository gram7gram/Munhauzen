package ua.gram.munhauzen.interaction.hare;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.hare.fragment.HareScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class HareStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final HareInteraction interaction;

    public HareStory story;

    public HareStoryManager(GameScreen gameScreen, HareInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public HareStory create(String begin) {

        reset();

        HareStory story = new HareStory();
        story.id = begin;

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

        if (story == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (HareScenario hareScenario : interaction.scenarioRegistry) {
                if (hareScenario.isBegin) {
                    story = create(hareScenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + story.id);

    }

    private void findNext(HareScenario from, HareStory hareStory) {

        Log.i(tag, "findNext " + from.name + " #" + hareStory.scenarios.size());

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
        story.update(progress, duration);
    }

    public void startLoadingResources() {
        try {

            if (story == null) return;

            HareStoryScenario scenario = story.currentScenario;
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

    public void onCompleted() {
        try {
            Log.i(tag, "onCompleted " + story.id);

            GameState gameState = gameScreen.game.gameState;

            gameState.addVisitedScenario(story.id);

            for (HareStoryScenario storyScenario : story.scenarios) {
                gameState.addVisitedScenario(storyScenario.scenario.name);
            }

            Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

            for (StoryAudio audio : story.currentScenario.scenario.audio) {
                if (audio.player != null) {
                    audio.player.pause();
                }
            }

            if (story.currentScenario.scenario.isExit) {

                Log.i(tag, "Exit reached");

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

        for (HareStoryScenario storyScenario : story.scenarios) {
            storyScenario.reset();
        }

        story.reset();
    }

    private boolean isDecisionAvailable(Decision decision, Set<String> inventory) {
        boolean hasRequired = true;
        if (decision.inventoryRequired != null) {
            if (decision.inventoryRequired.size() > 0) {
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
            if (decision.inventoryAbsent.size() > 0) {
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