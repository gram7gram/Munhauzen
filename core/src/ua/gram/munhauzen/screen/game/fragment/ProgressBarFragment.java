package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.interaction.ContinueInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ProgressBarFragment extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final GameScreen gameScreen;

    public ProgressBar bar;
    public Table root;
    public Stack content;
    public Table controlsTable;
    public ImageButton skipBackButton, rewindBackButton, skipForwardButton, rewindForwardButton, pauseButton, playButton;
    private Timer.Task fadeOutTask, progressTask;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public ProgressBarFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void create() {

        Log.i(tag, "create");

        Texture line = gameScreen.assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class);
        Texture knob = gameScreen.assetManager.get("ui/player_progress_bar_knob.png", Texture.class);
        Texture backTexture = gameScreen.assetManager.get("ui/elements_player_fond_1.png", Texture.class);
        Texture sideTexture = gameScreen.assetManager.get("ui/elements_player_fond_3.png", Texture.class);
        Texture centerTexture = gameScreen.assetManager.get("ui/elements_player_fond_2.png", Texture.class);

        Sprite knobSprite = new Sprite(knob);

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new NinePatchDrawable(new NinePatch(
                line,
                10, 10, 0, 0
        ));
        barStyle.knob = new SpriteDrawable(knobSprite);

        bar = new ProgressBar(0, 100, 1, false, barStyle);

        Sprite sideLeftSprite = new Sprite(sideTexture);
        Sprite sideRightSprite = new Sprite(sideTexture);
        sideRightSprite.setFlip(true, true);

        Image barBackgroundImage = new Image(backTexture);
        Image sideLeftDecor = new FitImage(new SpriteDrawable(sideLeftSprite));
        Image sideRightDecor = new FitImage(new SpriteDrawable(sideRightSprite));
        Image centerDecor = new FitImage(centerTexture);

        skipBackButton = getSkipBack();
        rewindBackButton = getRewindBack();
        playButton = getPlay();
        pauseButton = getPause();
        rewindForwardButton = getRewindForward();
        skipForwardButton = getSkipForward();

        Stack playPauseGroup = new Stack();
        playPauseGroup.add(playButton);
        playPauseGroup.add(pauseButton);

        int controlsSize = MunhauzenGame.WORLD_HEIGHT / 20;
        int fragmentHeight = controlsSize * 4;
        int decorHeight = fragmentHeight - controlsSize + 5;

        float originalKnobHeight = barStyle.knob.getMinHeight();
        float knobScale = originalKnobHeight > 0 ? 1f * controlsSize / originalKnobHeight : 1;

        barStyle.knob.setMinHeight(controlsSize);
        barStyle.knob.setMinWidth(barStyle.knob.getMinWidth() * knobScale);

        controlsTable = new Table();
        controlsTable.add(skipBackButton).expandX().left().width(controlsSize).height(controlsSize);
        controlsTable.add(rewindBackButton).expandX().right().width(controlsSize).height(controlsSize);
        controlsTable.add(playPauseGroup).expandX().center().width(controlsSize).height(controlsSize);
        controlsTable.add(rewindForwardButton).expandX().left().width(controlsSize).height(controlsSize);
        controlsTable.add(skipForwardButton).expandX().right().width(controlsSize).height(controlsSize);

        Table barTable = new Table();
        barTable.pad(controlsSize, controlsSize * 2, controlsSize, controlsSize * 2);
        barTable.add(controlsTable).padTop(5).padBottom(5).fillX().expandX().row();
        barTable.add(bar).fillX().expandX().height(controlsSize).row();

        Table backgroundContainer = new Table();
        backgroundContainer.add(barBackgroundImage).fill().expand().height(fragmentHeight);

        Table decorLeftContainer = new Table();
        decorLeftContainer.add(sideLeftDecor).left().expand()
                .width(sideLeftDecor.getWidth() * (1f * decorHeight / sideLeftDecor.getHeight()))
                .height(decorHeight);

        Table decorCenterContainer = new Table();
        decorCenterContainer.add(centerDecor).top().expand().padTop(controlsSize / 2f)
                .width(MunhauzenGame.WORLD_WIDTH / 2f);

        Table decorRightContainer = new Table();
        decorRightContainer.add(sideRightDecor).right().expand()
                .width(sideRightDecor.getWidth() * (1f * decorHeight / sideRightDecor.getHeight()))
                .height(decorHeight);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    Log.i(tag, "playButton clicked");

                    GameState.unpause(tag);

                    startCurrentMusicIfPaused();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    Log.i(tag, "pauseButton clicked");

                    gameScreen.audioService.pause();

                    GameState.pause(tag);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        skipBackButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                try {
                    gameScreen.audioService.pause();

                    Story story = gameScreen.getStory();
                    if (story.currentScenario == null) return;

                    StoryScenario skipTo;
                    if (story.currentScenario.previous != null) {
                        skipTo = story.currentScenario.previous;
                    } else {
                        skipTo = story.currentScenario;
                    }

                    Log.i(tag, "skipBackButton to " + skipTo.scenario.name + " at " + skipTo.startsAt + " ms");

                    story.progress = skipTo.startsAt;

                    GameState.pause(tag);

                    postProgressChanged();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                try {
                    GameState.unpause(tag);

                    startCurrentMusicIfPaused();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        skipForwardButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                try {
                    gameScreen.audioService.pause();

                    Story story = gameScreen.getStory();
                    if (story.currentScenario == null) return;

                    GameState.pause(tag);

                    if (story.currentScenario.next != null) {
                        story.progress = story.currentScenario.next.startsAt;
                    } else {
                        story.progress = story.currentScenario.finishesAt;
                    }

                    postProgressChanged();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                try {
                    GameState.unpause(tag);

                    startCurrentMusicIfPaused();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
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

                                story.progress -= story.totalDuration * 0.025f;

                                postProgressChanged();

                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    }, 0, 0.05f);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                try {
                    Log.i(tag, "rewindBackButton enter");

                    GameState.unpause(tag);

                    progressTask.cancel();
                    progressTask = null;

                    startCurrentMusicIfPaused();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
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

                                postProgressChanged();

                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    }, 0, 0.05f);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                try {
                    Log.i(tag, "rewindForwardButton exit");

                    GameState.unpause(tag);

                    progressTask.cancel();
                    progressTask = null;

                    startCurrentMusicIfPaused();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

        });

        bar.addListener(new ActorGestureListener() {

            private void scrollTo(float percent) {
                try {
                    gameScreen.audioService.pause();

                    Story story = gameScreen.getStory();
                    story.progress = story.totalDuration * percent;

                    postProgressChanged();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                cancelFadeOut();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                try {
                    GameState.unpause(tag);

                    startCurrentMusicIfPaused();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {

                    Story story = gameScreen.getStory();

                    float currentPercent = story.progress / story.totalDuration;

                    float totalLength = Math.max(1, bar.getWidth());

                    float percent = x / totalLength;

                    if (story.isInteractionLocked()) {
                        if (percent > currentPercent) return;
                    }

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
                    Story story = gameScreen.getStory();

                    float currentPercent = story.progress / story.totalDuration;

                    float totalLength = Math.max(1, bar.getWidth());

                    float percent = x / totalLength;

                    if (story.isInteractionLocked()) {
                        if (percent > currentPercent) return;
                    }

                    GameState.pause(tag);

                    scrollTo(percent);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        content = new Stack();
        content.addActor(backgroundContainer);
        content.addActor(decorLeftContainer);
        content.addActor(decorCenterContainer);
        content.addActor(decorRightContainer);
        content.addActor(barTable);

        content.addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                scheduleFadeOut();
            }
        });

        root = new Table();
        root.add(content).align(Align.bottom).fillX().expand().row();

        root.setName(tag);

        scheduleFadeOut();
    }

    public float getHeight() {
        return content.getHeight();
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

        pauseButton.setVisible(!GameState.isPaused);
        playButton.setVisible(GameState.isPaused);

        skipForwardButton.setDisabled(story.isCompleted || !hasNext || story.isInteractionLocked());
        skipForwardButton.setTouchable(skipForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        rewindForwardButton.setDisabled(story.isCompleted || story.isInteractionLocked());
        rewindForwardButton.setTouchable(rewindForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        skipBackButton.setDisabled(story.progress == 0 || !hasPrevious);
        rewindBackButton.setDisabled(story.progress == 0);

        skipBackButton.setTouchable(skipBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);
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
        }
    }

    public void startCurrentMusicIfPaused() {

        Story story = gameScreen.getStory();

        for (StoryScenario scenarioOption : story.scenarios) {
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
                                gameScreen.audioService.playAudio(audio);
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

    public void fadeIn() {

        if (!isMounted()) return;
        if (root.isVisible()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        root.setVisible(true);
        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.alpha(1, .3f),
                                Actions.moveTo(0, 0, .3f)
                        ),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;

                                content.setTouchable(Touchable.enabled);

                                scheduleFadeOut();
                            }
                        })
                )
        );
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        content.setTouchable(Touchable.disabled);

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.alpha(0, .3f),
                                Actions.moveTo(0, -40, .3f)
                        ),
                        Actions.visible(false),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        })
                )
        );
    }

    public boolean canFadeOut() {
        return isMounted() && root.isVisible() && !isFadeOut && gameScreen.scenarioFragment == null;
    }

    public void fadeOut(Runnable task) {

        if (!canFadeOut()) return;

        Log.i(tag, "fadeOut");

        content.setTouchable(Touchable.disabled);

        isFadeIn = false;
        isFadeOut = true;

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.fadeOut(.5f),
                                Actions.moveTo(0, -40, .5f)
                        ),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                root.setVisible(false);

                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        }),
                        Actions.run(task)
                )
        );
    }

    public void scheduleFadeOut() {

        cancelFadeOut();

        fadeOutTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                try {
                    if (canFadeOut()) {
                        fadeOut();
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);

                    cancelFadeOut();
                }
            }
        }, MunhauzenGame.PROGRESS_BAR_FADE_OUT_DELAY);
    }

    public void cancelFadeOut() {

        Log.i(tag, "cancelFadeOut");

        if (fadeOutTask != null) {
            fadeOutTask.cancel();
            fadeOutTask = null;
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (progressTask != null) {
            progressTask.cancel();
            progressTask = null;
        }

        cancelFadeOut();

        isFadeIn = false;
        isFadeOut = false;
    }

    private ImageButton getSkipBack() {
        Texture skipBack = gameScreen.assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = gameScreen.assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }

    private ImageButton getRewindBack() {
        Texture rewindBack = gameScreen.assetManager.get("ui/playbar_rewind_backward.png", Texture.class);
        Texture rewindBackOff = gameScreen.assetManager.get("ui/playbar_rewind_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindBack));
        style.down = new SpriteDrawable(new Sprite(rewindBack));
        style.disabled = new SpriteDrawable(new Sprite(rewindBackOff));

        return new ImageButton(style);
    }

    private ImageButton getRewindForward() {
        Texture rewindForward = gameScreen.assetManager.get("ui/playbar_rewind_forward.png", Texture.class);
        Texture rewindForwardOff = gameScreen.assetManager.get("ui/playbar_rewind_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindForward));
        style.down = new SpriteDrawable(new Sprite(rewindForward));
        style.disabled = new SpriteDrawable(new Sprite(rewindForwardOff));

        return new ImageButton(style);
    }

    private ImageButton getSkipForward() {
        Texture skipForward = gameScreen.assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipForwardOff = gameScreen.assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipForward));
        style.down = new SpriteDrawable(new Sprite(skipForward));
        style.disabled = new SpriteDrawable(new Sprite(skipForwardOff));

        return new ImageButton(style);
    }

    private ImageButton getPause() {
        Texture pause = gameScreen.assetManager.get("ui/playbar_pause.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(pause));
        style.down = new SpriteDrawable(new Sprite(pause));
        style.disabled = new SpriteDrawable(new Sprite(pause));

        return new ImageButton(style);
    }

    private ImageButton getPlay() {
        Texture play = gameScreen.assetManager.get("ui/playbar_play.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(play));
        style.down = new SpriteDrawable(new Sprite(play));
        style.disabled = new SpriteDrawable(new Sprite(play));

        return new ImageButton(style);
    }

    private void postProgressChanged() {
        try {
            Story story = gameScreen.getStory();

            boolean isCompletedBefore = story.isCompleted;

            story.update(story.progress, story.totalDuration);

            destroyContinueInteraction();

            if (!story.isCompleted) {
                gameScreen.hideAndDestroyScenarioFragment();
            }

            if (!isCompletedBefore && story.isCompleted) {

                if (canCompleteStory(story)) {

                    gameScreen.storyManager.onCompleted();
                } else {
                    Log.e(tag, "complete story ignored");
                }
            }
        } catch (GdxRuntimeException e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private boolean canCompleteStory(Story story) {
        boolean isInteractionLocked = story.currentInteraction != null && story.currentInteraction.isLocked;

        if (isInteractionLocked) {
            return false;
        }

        if (gameScreen.scenarioFragment == null) {
            return true;
        }

        return !gameScreen.scenarioFragment.storyId.equals(story.id);
    }
}
