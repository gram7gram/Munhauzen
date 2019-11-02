package ua.gram.munhauzen.interaction.cannons.fragment;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.interaction.cannons.CannonsStory;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.ScenarioBar;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsProgressBarFragment extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final CannonsInteraction interaction;
    public final GameScreen gameScreen;

    public ScenarioBar bar;
    public Table root;
    public Stack stack;
    public Table controlsTable;
    public ImageButton rewindBackButton, rewindForwardButton, pauseButton, playButton, skipForwardButton, skipBackButton;
    private Timer.Task fadeOutTask;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public CannonsProgressBarFragment(GameScreen gameScreen, CannonsInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        interaction.assetManager.load("ui/playbar_pause.png", Texture.class);
        interaction.assetManager.load("ui/playbar_play.png", Texture.class);

        interaction.assetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        interaction.assetManager.load("ui/playbar_rewind_backward_off.png", Texture.class);

        interaction.assetManager.load("ui/playbar_rewind_forward.png", Texture.class);
        interaction.assetManager.load("ui/playbar_rewind_forward_off.png", Texture.class);

        interaction.assetManager.load("ui/elements_player_fond_1.png", Texture.class);
        interaction.assetManager.load("ui/elements_player_fond_2.png", Texture.class);
        interaction.assetManager.load("ui/elements_player_fond_3.png", Texture.class);

        interaction.assetManager.finishLoading();

        Texture backTexture = interaction.assetManager.get("ui/elements_player_fond_1.png", Texture.class);
        Texture sideTexture = interaction.assetManager.get("ui/elements_player_fond_3.png", Texture.class);
        Texture centerTexture = interaction.assetManager.get("ui/elements_player_fond_2.png", Texture.class);

        bar = new ScenarioBar(gameScreen);

        Sprite sideLeftSprite = new Sprite(sideTexture);
        Sprite sideRightSprite = new Sprite(sideTexture);
        sideRightSprite.setFlip(true, true);

        Image barBackgroundImage = new Image(backTexture);
        Image sideLeftDecor = new FitImage(new SpriteDrawable(sideLeftSprite));
        Image sideRightDecor = new FitImage(new SpriteDrawable(sideRightSprite));
        Image centerDecor = new FitImage(centerTexture);

        rewindBackButton = getRewindBack();
        playButton = getPlay();
        pauseButton = getPause();
        rewindForwardButton = getRewindForward();
        skipBackButton = getSkipBack();
        skipForwardButton = getSkipForward();

        Stack playPauseGroup = new Stack();
        playPauseGroup.add(playButton);
        playPauseGroup.add(pauseButton);

        int controlsSize = MunhauzenGame.WORLD_HEIGHT / 20;
        int fragmentHeight = controlsSize * 4;
        int decorHeight = fragmentHeight - controlsSize + 5;

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

                    gameScreen.game.sfxService.onProgressPlay();

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

                    gameScreen.game.sfxService.onProgressPause();

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

                    CannonsStory story = interaction.storyManager.story;
                    if (story.currentScenario == null) return;

                    if (story.currentScenario.previous != null) {
                        story.progress = story.currentScenario.previous.startsAt;
                    } else {
                        story.progress = story.currentScenario.startsAt;
                    }

                    GameState.pause(tag);

                    postProgressChanged();

                    gameScreen.game.sfxService.onProgressSkip();

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

                    CannonsStory story = interaction.storyManager.story;
                    if (story.currentScenario == null) return;

                    GameState.pause(tag);

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

                try {
                    GameState.unpause(tag);

                    startCurrentMusicIfPaused();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        rewindBackButton.addListener(new InputListener() {

            Timer.Task progressTask;

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
                                CannonsStory story = interaction.storyManager.story;

                                story.progress -= story.totalDuration * 0.025f;

                                postProgressChanged();
                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    }, 0, 0.05f);

                    gameScreen.game.sfxService.onProgressScrollStart();

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

                    gameScreen.game.sfxService.onProgressScrollEnd();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

        });

        rewindForwardButton.addListener(new InputListener() {

            Timer.Task progressTask;

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
                                CannonsStory story = interaction.storyManager.story;

                                story.progress += story.totalDuration * 0.025f;

                                postProgressChanged();
                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    }, 0, 0.05f);

                    gameScreen.game.sfxService.onProgressScrollStart();

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

                    gameScreen.game.sfxService.onProgressScrollEnd();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

        });

        bar.addListener(new ActorGestureListener() {

            private void scrollTo(float percent) {

                try {
                    gameScreen.audioService.pause();

                    CannonsStory story = interaction.storyManager.story;

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

                gameScreen.game.sfxService.onProgressSkip();

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

        scheduleFadeOut();

        stack = new Stack();
        stack.addActor(backgroundContainer);
        stack.addActor(decorLeftContainer);
        stack.addActor(decorCenterContainer);
        stack.addActor(decorRightContainer);
        stack.addActor(barTable);

        stack.addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                scheduleFadeOut();
            }
        });

        root = new Table();
        root.add(stack).align(Align.bottom).fillX().expand().row();

        root.setName(tag);

        scheduleFadeOut();
    }

    public float getHeight() {
        return stack.getHeight();
    }

    public void update() {

        if (!isMounted()) return;

        if (interaction.storyManager == null) return;

        CannonsStory story = interaction.storyManager.story;

        if (story == null) return;

        boolean hasVisitedBefore = gameScreen.game.gameState.history.visitedStories.contains(story.id);

        bar.setEnabled(hasVisitedBefore);

        pauseButton.setVisible(!GameState.isPaused);
        playButton.setVisible(GameState.isPaused);

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

    public void startCurrentMusicIfPaused() {

        if (interaction.storyManager == null) return;

        CannonsStory story = interaction.storyManager.story;

        if (story == null) return;

        for (CannonsStoryScenario scenarioOption : story.scenarios) {
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
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);
        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.fadeIn(.3f),
                                Actions.moveTo(0, 0, .3f)
                        ),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;

                                scheduleFadeOut();
                            }
                        })
                )
        );
    }

    public void fadeOut(Runnable task) {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

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

    public boolean canFadeOut() {
        return isMounted() && root.isVisible() && !isFadeOut && interaction.scenarioFragment == null;
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

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
                        })
                )
        );
    }

    public void scheduleFadeOut() {

        cancelFadeOut();

        fadeOutTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadeOut();
            }
        }, MunhauzenGame.PROGRESS_BAR_FADE_OUT_DELAY);
    }

    public void cancelFadeOut() {

        if (fadeOutTask != null) {
            fadeOutTask.cancel();
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        cancelFadeOut();
        isFadeIn = false;
        isFadeOut = false;
    }

    private ImageButton getRewindBack() {
        Texture rewindBack = interaction.assetManager.get("ui/playbar_rewind_backward.png", Texture.class);
        Texture rewindBackOff = interaction.assetManager.get("ui/playbar_rewind_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindBack));
        style.down = new SpriteDrawable(new Sprite(rewindBack));
        style.disabled = new SpriteDrawable(new Sprite(rewindBackOff));

        return new ImageButton(style);
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

    private ImageButton getSkipForward() {
        Texture skipForward = gameScreen.assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipForwardOff = gameScreen.assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipForward));
        style.down = new SpriteDrawable(new Sprite(skipForward));
        style.disabled = new SpriteDrawable(new Sprite(skipForwardOff));

        return new ImageButton(style);
    }


    private ImageButton getRewindForward() {
        Texture rewindForward = interaction.assetManager.get("ui/playbar_rewind_forward.png", Texture.class);
        Texture rewindForwardOff = interaction.assetManager.get("ui/playbar_rewind_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindForward));
        style.down = new SpriteDrawable(new Sprite(rewindForward));
        style.disabled = new SpriteDrawable(new Sprite(rewindForwardOff));

        return new ImageButton(style);
    }

    private ImageButton getPause() {
        Texture pause = interaction.assetManager.get("ui/playbar_pause.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(pause));
        style.down = new SpriteDrawable(new Sprite(pause));
        style.disabled = new SpriteDrawable(new Sprite(pause));

        return new ImageButton(style);
    }

    private ImageButton getPlay() {
        Texture play = interaction.assetManager.get("ui/playbar_play.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(play));
        style.down = new SpriteDrawable(new Sprite(play));
        style.disabled = new SpriteDrawable(new Sprite(play));

        return new ImageButton(style);
    }

    private void postProgressChanged() {
        try {
            CannonsStory story = interaction.storyManager.story;

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
