package ua.gram.munhauzen.interaction.timer.fragment;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.interaction.timer.TimerStory;
import ua.gram.munhauzen.interaction.timer.TimerStoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.StoryProgressBar;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerProgressBarFragment extends StoryProgressBar<TimerInteraction> {

    public TimerProgressBarFragment(GameScreen gameScreen, TimerInteraction interaction) {
        super(gameScreen, interaction);
    }

    @Override
    public void create() {
        super.create();

        skipBackButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    TimerStory story = interaction.storyManager.story;
                    if (story.currentScenario == null) return;

                    if (story.currentScenario.previous != null) {
                        story.progress = story.currentScenario.previous.startsAt;
                    } else {
                        story.progress = story.currentScenario.startsAt;
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

        skipForwardButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    TimerStory story = interaction.storyManager.story;
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

                    if (interaction.scenarioFragment != null) {
                        interaction.scenarioFragment.fadeOut(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (interaction.scenarioFragment != null) {
                                        interaction.scenarioFragment.destroy();
                                        interaction.scenarioFragment = null;
                                    }
                                } catch (Throwable e) {
                                    Log.e(tag, e);
                                }
                            }
                        });
                    }

                    progressTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                TimerStory story = interaction.storyManager.story;

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

//                    gameScreen.game.sfxService.onProgressScrollStart();
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
                                TimerStory story = interaction.storyManager.story;

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

//                    gameScreen.game.sfxService.onProgressScrollStart();
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

                    TimerStory story = interaction.storyManager.story;

                    story.progress = story.totalDuration * percent;

                    postProgressChanged();

                    if (!story.isCompleted) {
                        if (interaction.scenarioFragment != null) {
                            interaction.scenarioFragment.fadeOut(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (interaction.scenarioFragment != null) {
                                            interaction.scenarioFragment.destroy();
                                            interaction.scenarioFragment = null;
                                        }
                                    } catch (Throwable e) {
                                        Log.e(tag, e);
                                    }
                                }
                            });
                        }
                    }
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

        if (!interaction.isValid()) return;

        TimerStory story = interaction.storyManager.story;

        boolean hasVisitedBefore = MunhauzenGame.developmentSkipEnable || gameScreen.game.gameState.history.visitedStories.contains(story.id);

        bar.setEnabled(hasVisitedBefore);

        if (story.isCompleted) {
            pauseButton.setVisible(false);
            playButton.setVisible(true);
        } else {
            pauseButton.setVisible(!GameState.isPaused);
            playButton.setVisible(GameState.isPaused);
        }

        skipForwardButton.setDisabled(!hasVisitedBefore || story.isCompleted);
        skipForwardButton.setTouchable(skipForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        skipBackButton.setDisabled(story.progress == 0);
        skipBackButton.setTouchable(skipBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        rewindForwardButton.setDisabled(!hasVisitedBefore || story.isCompleted);
        rewindForwardButton.setTouchable(rewindForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        rewindBackButton.setDisabled(story.progress == 0);
        rewindBackButton.setTouchable(rewindBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        bar.setRange(0, story.totalDuration);
        bar.setValue(story.progress);
    }

    @Override
    public void startCurrentMusicIfPaused() {

        if (!interaction.isValid()) return;

        TimerStory story = interaction.storyManager.story;
        if (story.isCompleted) return;

        for (TimerStoryScenario scenarioOption : story.scenarios) {
            if (scenarioOption != story.currentScenario) {
                for (StoryAudio audio : scenarioOption.scenario.audio) {
                    Music player = audio.player;
                    if (player != null) {
                        audio.isActive = false;
                        player.pause();
                    }
                }
            } else {
                for (StoryAudio audio : scenarioOption.scenario.audio) {
                    Music player = audio.player;
                    if (player != null) {
                        if (audio.isLocked) {
                            if (!story.isCompleted && !audio.isActive) {
                                gameScreen.audioService.prepareAndPlay(audio);
                            }
                        } else {
                            audio.isActive = false;
                            player.pause();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void postProgressChanged() {
        try {
            if (!interaction.isValid()) return;

            TimerStory story = interaction.storyManager.story;

            boolean isCompletedBefore = story.isCompleted;

            story.update(story.progress, story.totalDuration);

            if (story.isValid()) {
                if (!isCompletedBefore && story.isCompleted) {
                    if (interaction.scenarioFragment == null) {
                        interaction.storyManager.onCompleted();
                    }

                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
