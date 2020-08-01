package ua.gram.munhauzen.service;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.repository.ScenarioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.game.fragment.GameScenarioFragment;
import ua.gram.munhauzen.screen.game.fragment.VictoryFragment;
import ua.gram.munhauzen.utils.Log;

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
        story.id = optionId;

        Scenario scenario = ScenarioRepository.find(gameState, optionId);

        findNext(scenario, story);

        story.init();

        String log = "create `" + story.id + "` x" + story.scenarios.size() + "\n";

        for (StoryScenario storyScenario : story.scenarios) {
            log += storyScenario.startsAt + "-" + storyScenario.finishesAt + " " + storyScenario.scenario.name + "\n";

            for (StoryAudio item : storyScenario.scenario.audio) {
                log += " - audio " + item.audio + " " + item.startsAt + "-" + item.finishesAt + "\n";
            }

            for (StoryImage item : storyScenario.scenario.images) {
                log += " - image " + item.image + " " + item.startsAt + "-" + item.finishesAt + "\n";
            }
        }

        Log.i(tag, log);

        if (!story.isValid()) {
            throw new GdxRuntimeException("Created story is not valid!");
        }

        return story;
    }

    public void resume() {

        Story story = gameScreen.getStory();
        if (story != null) {
            if (!story.isInit) {
                float progressBefore = Math.max(0, story.progress - 1500);

                story.init();

                story.update(progressBefore, story.totalDuration);
            }
        }

        if (story == null || !story.isValid()) {
            Log.e(tag, "Story is not valid. Resetting");

            Story newStory = getDefaultStory();

            newStory.init();

            gameScreen.setStory(newStory);
        }

        Log.i(tag, "resume " + gameScreen.getStory().id);
    }

    public Story getDefaultStory() {
        Scenario start = ScenarioRepository.findBegin(gameState);

        return create(start.name);
    }

    private void findNext(Scenario from, Story story) {

        Log.i(tag, "findNext " + from.name + " #" + story.scenarios.size());

        StoryScenario storyScenario = new StoryScenario();
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        story.scenarios.add(storyScenario);

        if (from.interaction == null) {
            if ("GOTO".equals(from.action)) {

                Scenario next = getNextScenarioFromDecisions(from);
                if (next != null) {

                    findNext(next, story);
                }

            }
        }
    }

    public Scenario getNextScenarioFromDecisions(Scenario from) {

        Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

        for (Decision decision : from.decisions) {
            if (isDecisionAvailable(decision, inventory)) {

                return ScenarioRepository.find(gameState, decision.scenario);
            }
        }

        return null;
    }

    public void update(float progress, int duration) {

        Story story = gameScreen.getStory();

        story.update(progress, duration);
    }

    public void startLoadingImages() {
        Story story = gameScreen.getStory();
        if (story == null) return;

        try {

            displayCurrentImage();

            StoryScenario option = story.currentScenario;
            if (option == null) return;

            final StoryImage optionImage = option.currentImage;
            if (optionImage != null) {
                if (optionImage.next != null) {
                    gameScreen.imageService.prepare(optionImage.next, new Timer.Task() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }
    }

    public void displayCurrentImage() {
        Story story = gameScreen.getStory();
        if (story == null) return;

        try {
            StoryScenario option = story.currentScenario;
            if (option == null) return;

            final StoryImage optionImage = option.currentImage;
            if (optionImage != null) {
                if (optionImage.isLocked) {
                    gameScreen.imageService.prepareAndDisplay(optionImage);

//                    if (gameScreen.imageService.transition == null) {
//                        throw new GdxRuntimeException("Transition was not created");
//                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }
    }

    public void startLoadingAudio() {
        Story story = gameScreen.getStory();
        if (story == null) return;

        try {
            StoryScenario option = story.currentScenario;
            if (option == null) return;

            final StoryAudio optionAudio = option.currentAudio;
            if (optionAudio != null) {
                gameScreen.audioService.prepare(optionAudio, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            if (gameScreen.audioService != null)
                                gameScreen.audioService.playAudio(optionAudio);
                        } catch (Throwable e) {
                            Log.e(tag, e);

//                            gameScreen.onCriticalError(e);
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

        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }
    }

    public void startLoadingResources() {

        startLoadingAudio();

        startLoadingImages();
    }

    public void onCompleted() {

        try {

            final Story story = gameScreen.getStory();
            if (story == null) return;

            Log.i(tag, "onCompleted " + story.id);

            boolean isPurchased = gameScreen.checkExpansionIsPurchased();

            displayCurrentImage();

            GameState gameState = gameScreen.game.gameState;

            gameState.menuState.isContinueEnabled = true;

            gameState.addVisitedScenario(story.id);

            for (StoryScenario storyScenario : story.scenarios) {

                gameState.addVisitedScenario(storyScenario.scenario.name);

                Chapter chapter = ChapterRepository.find(gameState, storyScenario.scenario.chapter);

                gameScreen.game.achievementService.onChapterOpened(chapter);
                gameScreen.game.achievementService.onScenarioVisited(storyScenario.scenario);
            }

            for (StoryAudio audio : story.currentScenario.scenario.audio) {
                if (audio.player != null) {
                    audio.player.pause();
                }
            }

            if (!isPurchased) {
                gameScreen.createPurchaseFragment(story.currentScenario.scenario);
                return;
            }

            if (story.isVictory()) {

                startVictory(story);
                return;

            }

            String interaction;

            if (MunhauzenGame.developmentInteraction != null) {

                interaction = MunhauzenGame.developmentInteraction + "";

                MunhauzenGame.developmentInteraction = null;
            } else {
                interaction = story.currentScenario.scenario.interaction;
            }

            if (interaction != null) {
                gameScreen.interactionService.create(interaction);
            }

            if (story.currentInteraction != null && story.currentInteraction.isLocked) {
                startInteraction(story);
                return;
            }

            startScenarioDecisions(story);

        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }

    }

    private void startInteraction(final Story story) {
        try {
            Log.i(tag, "startInteraction");

            gameScreen.progressBarFragment.root.setTouchable(Touchable.disabled);
            gameScreen.hideAndDestroyScenarioFragment();

            gameScreen.audioService.stop(tag);
            gameScreen.audioService.dispose(story);

            GameState.clearTimer(tag);

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {
                        if (story.currentInteraction != null) {
                            if (story.currentInteraction.interaction != null) {
                                story.currentInteraction.interaction.start();
                            }
                        }
                    } catch (Throwable e) {
                        Log.e(tag, e);

//                        gameScreen.onCriticalError(e);
                    }
                }
            }, .4f);
        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }
    }

    private void startVictory(Story story) {
        try {
            Log.i(tag, "startVictory");

            GameState.isEndingReached = true;

            gameScreen.hideProgressBar();
            gameScreen.hideAndDestroyScenarioFragment();

            gameScreen.controlsFragment.fadeOut();

            gameScreen.audioService.dispose(story);

            GameState.pause(tag);

            GameState.clearTimer(tag);

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {
                        gameScreen.victoryFragment = new VictoryFragment(gameScreen);
                        gameScreen.victoryFragment.create();

                        gameScreen.gameLayers.setInteractionLayer(gameScreen.victoryFragment);

                        gameScreen.victoryFragment.fadeIn();

                    } catch (Throwable e) {
                        Log.e(tag, e);

//                        gameScreen.onCriticalError(e);
                    }
                }
            }, .4f);
        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }
    }

    private void startScenarioDecisions(Story story) {

        try {
            Log.i(tag, "startScenarioDecisions");

            Set<String> inventory = gameScreen.game.inventoryService.getAllInventory();

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

                if (gameScreen.scenarioFragment == null) {

                    gameScreen.scenarioFragment = new GameScenarioFragment(gameScreen, story.id);

                } else if (!gameScreen.scenarioFragment.storyId.equals(story.id)) {

                    gameScreen.scenarioFragment.destroy();

                    gameScreen.scenarioFragment = new GameScenarioFragment(gameScreen, story.id);
                }

                if (gameScreen.progressBarFragment != null)
                    gameScreen.progressBarFragment.fadeIn();

                gameScreen.scenarioFragment.create(availableDecisions);

                gameScreen.gameLayers.setInteractionLayer(null);
                gameScreen.gameLayers.setStoryDecisionsLayer(
                        gameScreen.scenarioFragment
                );

                gameScreen.scenarioFragment.fadeIn();
            } else {
                gameScreen.navigateTo(new MenuScreen(gameScreen.game));
            }
        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
        }
    }

    public void reset() {

        if (gameScreen == null) return;

        Story story = gameScreen.getStory();
        if (story == null || story.id == null) return;

        Log.i(tag, "reset " + story.id);

        for (StoryScenario storyScenario : story.scenarios) {
            storyScenario.reset();
        }

        gameScreen.audioService.dispose(story);

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

    public void dispose() {

        Story story = gameScreen.getStory();
        if (story == null) return;

        for (StoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.images != null) {
                for (StoryImage image : storyScenario.scenario.images) {
                    image.isPrepared = false;
                    image.drawable = null;
                }
            }

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio audio : storyScenario.scenario.audio) {
                    audio.isPrepared = false;
                    audio.player = null;
                }
            }

        }
    }
}