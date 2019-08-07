package ua.gram.munhauzen.interaction.servants.fragment;

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
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.hire.HireStory;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ServantsProgressBarFragment extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final ServantsInteraction interaction;
    public final GameScreen gameScreen;

    public ProgressBar bar;
    public Table root;
    public Stack stack;
    public Table controlsTable;
    public ImageButton rewindBackButton, rewindForwardButton, pauseButton, playButton;
    private Timer.Task fadeOutTask;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public ServantsProgressBarFragment(GameScreen gameScreen, ServantsInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        Texture line = interaction.assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class);
        Texture knob = interaction.assetManager.get("ui/player_progress_bar_knob.png", Texture.class);
        Texture backTexture = interaction.assetManager.get("ui/elements_player_fond_1.png", Texture.class);
        Texture sideTexture = interaction.assetManager.get("ui/elements_player_fond_3.png", Texture.class);
        Texture centerTexture = interaction.assetManager.get("ui/elements_player_fond_2.png", Texture.class);

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

        rewindBackButton = getRewindBack();
        playButton = getPlay();
        pauseButton = getPause();
        rewindForwardButton = getRewindForward();

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
        controlsTable.add(rewindBackButton).expandX().right().width(controlsSize).height(controlsSize);
        controlsTable.add(playPauseGroup).expandX().center().width(controlsSize).height(controlsSize);
        controlsTable.add(rewindForwardButton).expandX().left().width(controlsSize).height(controlsSize);

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

                    GameState.unpause();

                    startCurrentMusicIfPaused();

                    scheduleFadeOut();
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

                    GameState.pause();

                    scheduleFadeOut();
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

                    cancelFadeOut();

                    gameScreen.audioService.pause();

                    progressTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                HireStory story = interaction.storyManager.story;

                                GameState.pause();

                                story.progress -= story.totalDuration * 0.025f;

                                postProgressChanged(story.isCompleted);

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

                    GameState.unpause();

                    progressTask.cancel();
                    progressTask = null;

                    startCurrentMusicIfPaused();

                    scheduleFadeOut();
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

                    cancelFadeOut();

                    gameScreen.audioService.pause();

                    progressTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                HireStory story = interaction.storyManager.story;

                                GameState.pause();

                                story.progress += story.totalDuration * 0.025f;

                                postProgressChanged(story.isCompleted);

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

                    GameState.unpause();

                    progressTask.cancel();
                    progressTask = null;

                    startCurrentMusicIfPaused();

                    scheduleFadeOut();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

        });

        bar.addListener(new ActorGestureListener() {

            private void scrollTo(float percent) {
                try {
                    gameScreen.audioService.pause();

                    HireStory story = interaction.storyManager.story;
                    story.progress = story.totalDuration * percent;

                    postProgressChanged(story.isCompleted);

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
                    GameState.unpause();

                    startCurrentMusicIfPaused();

                    scheduleFadeOut();
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

                    GameState.pause();

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

                    GameState.pause();

                    scrollTo(percent);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        stack = new Stack();
        stack.addActor(backgroundContainer);
        stack.addActor(decorLeftContainer);
        stack.addActor(decorCenterContainer);
        stack.addActor(decorRightContainer);
        stack.addActor(barTable);

        root = new Table();
        root.setTouchable(Touchable.childrenOnly);
        root.add(stack).align(Align.bottom).fillX().expand().row();

        root.setName(tag);

        root.setVisible(false);

        scheduleFadeOut();
    }

    public float getHeight() {
        return stack.getHeight();
    }

    @Override
    public boolean isMounted() {
        return super.isMounted() && interaction.storyManager.story != null;
    }

    public void update() {

        if (!isMounted()) return;

        boolean hasServant = interaction.hireFragment.hasServant(interaction.hireFragment.hireDialog.servantName);

        root.setTouchable(Touchable.childrenOnly);
        stack.setVisible(true);

        if (hasServant) {
            root.setTouchable(Touchable.disabled);
            stack.setVisible(false);
        }

        if (interaction.hireFragment.hireDialog.isDecisionActive) {
            root.setTouchable(Touchable.disabled);
            stack.setVisible(false);
        }

        HireStory story = interaction.storyManager.story;

        interaction.hireFragment.hireDialog.root.setVisible(story.isCompleted);

        pauseButton.setVisible(!GameState.isPaused);
        playButton.setVisible(GameState.isPaused);

        rewindForwardButton.setDisabled(story.isCompleted);
        rewindForwardButton.setTouchable(rewindForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        rewindBackButton.setDisabled(story.progress == 0);
        rewindBackButton.setTouchable(rewindBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        bar.setTouchable(bar.isDisabled() ? Touchable.disabled : Touchable.enabled);

        bar.setRange(0, story.totalDuration);
        bar.setValue(story.progress);
    }

    public void fadeIn() {

        if (!isMounted()) return;
        if (isFadeIn) return;

        cancelFadeOut();

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

    public void startCurrentMusicIfPaused() {

        HireStory story = interaction.storyManager.story;

        for (HireStoryScenario scenarioOption : story.scenarios) {
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

    public boolean canFadeOut() {
        return isMounted() && root.isVisible() && !isFadeOut;
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.fadeOut(.5f),
                                Actions.moveTo(0, -40, .5f)
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

    private void postProgressChanged(boolean isCompletedBefore) {
        try {
            HireStory story = interaction.storyManager.story;

            story.update(story.progress, story.totalDuration);

            gameScreen.interactionService.update();

            if (story.isValid()) {

                if (!isCompletedBefore && story.isCompleted) {

                    gameScreen.storyManager.onCompleted();

                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
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
}
