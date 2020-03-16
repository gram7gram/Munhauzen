package ua.gram.munhauzen.interaction.wauwau;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.wauwau.fragment.WauScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class WauStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final WauInteraction interaction;

    public WauStory story;

    public WauStoryManager(GameScreen gameScreen, WauInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public WauStory create(String begin) {

        reset();

        WauStory story = new WauStory();
        story.id = begin;

        Log.i(tag, "create from " + story.id);

        for (WauScenario scenario : interaction.scenarioRegistry) {
            if (scenario.name.equals(begin)) {
                findNext(scenario, story);
                break;
            }
        }

        story.init();

        return story;
    }

    public void resume() {

        if (story == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (WauScenario scenario : interaction.scenarioRegistry) {
                if (scenario.isBegin) {
                    story = create(scenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + story.id);
    }

    private void findNext(WauScenario from, WauStory story) {

        Log.i(tag, "findNext " + from.name + " #" + story.scenarios.size());

        if ("awau_2_check".equals(from.name)) {
            String decision;

            if (interaction.wauCounter >= interaction.maxWauCounter) {

                Log.i(tag, "wauCounter redirects to awau_2_a_win");

                interaction.wauCounter = 0;

                decision = "awau_2_a_win";
            } else {
                decision = "awau_2_a_loose";
            }

            for (WauScenario scenario : interaction.scenarioRegistry) {
                if (scenario.name.equals(decision)) {
                    findNext(scenario, story);
                    return;
                }
            }
        }

        WauStoryScenario storyScenario = new WauStoryScenario(gameScreen.game.gameState);
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        story.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {

                for (WauScenario scenario : interaction.scenarioRegistry) {
                    if (scenario.name.equals(decision.scenario)) {

                        findNext(scenario, story);

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

            WauStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

            final WauStoryImage image = scenario.currentImage;
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

            WauStoryScenario scenario = story.currentScenario;
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

        try {

            StoryImage image = story.currentScenario.currentImage;
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

            for (WauStoryScenario storyScenario : story.scenarios) {
                gameState.addVisitedScenario(storyScenario.scenario.name);
            }

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
                    interaction.scenarioFragment = new WauScenarioFragment(gameScreen, interaction);
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

        for (WauStoryScenario storyScenario : story.scenarios) {
            storyScenario.reset();
        }

        story.reset();
    }
}