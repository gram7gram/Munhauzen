package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.interaction.ContinueInteraction;
import ua.gram.munhauzen.interaction.DummyInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.StoryProgressBar;
import ua.gram.munhauzen.utils.Log;

public class GameProgressBarFragment extends StoryProgressBar<DummyInteraction> {

    public GameProgressBarFragment(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void create() {
        super.create();

        skipBackButton.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                try {
                    Story story = gameScreen.getStory();
                    if (story == null) return;
                    if (story.currentScenario == null) return;

                    StoryScenario skipTo;
                    if (story.currentScenario.previous != null) {
                        skipTo = story.currentScenario.previous;
                    } else {
                        skipTo = story.currentScenario;
                    }

                    Log.i(tag, "skipBackButton to " + skipTo.scenario.name + " at " + skipTo.startsAt + " ms");

                    story.progress = skipTo.startsAt;

                    postProgressChanged();

                    gameScreen.game.sfxService.onProgressSkip();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                restartScenario();
            }
        });

        skipForwardButton.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                try {
                    Story story = gameScreen.getStory();
                    if (story == null) return;
                    if (story.currentScenario == null) return;

                    if (story.currentScenario.next != null) {
                        story.progress = story.currentScenario.next.startsAt;
                    } else {
                        story.progress = story.currentScenario.finishesAt;
                    }

                    postProgressChanged();

                    gameScreen.game.sfxService.onProgressSkip();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                restartScenario();
            }
        });

        rewindBackButton.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                try {
                    Log.i(tag, "rewindBackButton enter");

                    gameScreen.audioService.pause();

                    GameState.pause(tag);

                    progressTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                Story story = gameScreen.getStory();
                                if (story == null) return;

                                //jump progress so that interaction does not start immediately
                                //example - Continue interaction
                                StoryScenario scenario = story.last();
                                if (scenario != null) {
                                    if (scenario.scenario.interaction != null) {
                                        if (story.progress >= scenario.startsAt) {
                                            story.progress = scenario.startsAt - 1;
                                        }
                                    }
                                }

                                story.progress -= story.totalDuration * 0.025f;

                                if (story.progress < 0) {
                                    story.progress = 0;
                                    restartScenario();
                                    return;
                                }

                                postProgressChanged();

                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    }, 0, 0.05f);

                    gameScreen.game.sfxService.onProgressScroll();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                restartScenario();
            }

        });

        rewindForwardButton.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                try {
                    Log.i(tag, "rewindForwardButton enter");

                    gameScreen.audioService.pause();

                    GameState.pause(tag);

                    progressTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                Story story = gameScreen.getStory();
                                if (story == null) return;

                                story.progress += story.totalDuration * 0.025f;

                                if (story.progress > story.totalDuration) {
                                    story.progress = story.totalDuration;

                                    if (story.isCompleted) {
                                        restartScenario();
                                        return;
                                    }
                                }

                                postProgressChanged();

                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    }, 0, 0.05f);

                    gameScreen.game.sfxService.onProgressScroll();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                restartScenario();
            }

        });

        bar.addListener(new ActorGestureListener() {

            private void scrollTo(float percent) {
                try {
                    gameScreen.audioService.pause();

                    Story story = gameScreen.getStory();

                    //do not scroll till the end
                    story.progress = Math.min(story.totalDuration * .975f, story.totalDuration * percent);

                    postProgressChanged();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                cancelFadeOut();

                gameScreen.game.sfxService.onProgressSkip();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                restartScenario();
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {

                    float totalLength = Math.max(1, bar.getWidth());

                    float percent = x / totalLength;

                    GameState.pause(tag);

                    scrollTo(percent);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                try {

                    float totalLength = Math.max(1, bar.getWidth());

                    float percent = x / totalLength;

                    GameState.pause(tag);

                    scrollTo(percent);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });
    }

    public void update() {

        if (!isMounted()) return;
        if (!root.isVisible()) return;

        Story story = gameScreen.getStory();
        if (story == null) return;

        boolean hasPrevious = false, hasNext = false;

        if (story.currentScenario != null) {
            hasPrevious = true;
            hasNext = true;
        }

        boolean hasVisitedBefore = MunhauzenGame.developmentSkipEnable || gameScreen.game.gameState.history.visitedStories.contains(story.id);

        bar.setEnabled(hasVisitedBefore);

        if (story.isCompleted) {
            pauseButton.setVisible(false);
            playButton.setVisible(true);
        } else {
            pauseButton.setVisible(!GameState.isPaused);
            playButton.setVisible(GameState.isPaused);
        }

        skipForwardButton.setDisabled(!hasVisitedBefore || story.isCompleted || !hasNext || story.isInteractionLocked());
        skipForwardButton.setTouchable(skipForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        skipBackButton.setDisabled(story.progress == 0 || !hasPrevious);
        skipBackButton.setTouchable(skipBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        rewindForwardButton.setDisabled(!hasVisitedBefore || story.isCompleted || story.isInteractionLocked());
        rewindForwardButton.setTouchable(rewindForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        rewindBackButton.setDisabled(story.progress == 0);
        rewindBackButton.setTouchable(rewindBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        bar.setRange(0, Math.max(10, story.totalDuration));
        bar.setValue(story.progress);
    }

    public void destroyContinueInteraction() {
        Story story = gameScreen.getStory();

        if (story.currentInteraction != null) {
            if (story.currentInteraction.interaction instanceof ContinueInteraction) {
                gameScreen.interactionService.destroy();

                story.currentInteraction = null;

                gameScreen.restoreProgressBarIfDestroyed();
            }

            story.currentInteraction = null;
        }
    }

    public void startCurrentMusicIfPaused() {

        Story story = gameScreen.getStory();
        if (story == null) return;
        if (story.isCompleted) return;

        for (StoryScenario scenarioOption : story.scenarios) {
            if (scenarioOption != story.currentScenario) {
                for (StoryAudio audio : scenarioOption.scenario.audio) {
                    if (audio.player != null) {
                        audio.isActive = false;
                        audio.player.pause();
                    }
                }
            } else {
                for (StoryAudio audio : scenarioOption.scenario.audio) {
                    if (audio.player != null) {
                        if (audio.isLocked) {
                            if (!audio.isActive) {
                                gameScreen.audioService.prepareAndPlay(audio);
                            }
                        } else {
                            audio.isActive = false;
                            audio.player.pause();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean canFadeOut() {
        return super.canFadeOut() && gameScreen.scenarioFragment == null;
    }

    @Override
    public void postProgressChanged() {
        try {
            Story story = gameScreen.getStory();
            if (story == null) return;

            boolean isCompletedBefore = story.isCompleted;

            destroyContinueInteraction();

            story.update(story.progress, story.totalDuration);

            if (!story.isCompleted) {
                gameScreen.hideAndDestroyScenarioFragment();
            }

            if (!isCompletedBefore && story.isCompleted) {

                if (canCompleteStory(story)) {
                    gameScreen.storyManager.onCompleted();
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private boolean canCompleteStory(Story story) {
        boolean isInteractionLocked = story.currentInteraction != null
                && story.currentInteraction.isLocked;

        if (isInteractionLocked) {
            return false;
        }

        if (gameScreen.scenarioFragment == null) {
            return true;
        }

        return !gameScreen.scenarioFragment.storyId.equals(story.id);
    }
}
