package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import ua.gram.munhauzen.interaction.AbstractInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.ProgressIconButton;
import ua.gram.munhauzen.ui.ScenarioBar;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class StoryProgressBar<I extends AbstractInteraction> extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final I interaction;
    public final AssetManager assetManager;
    public final GameScreen gameScreen;

    public ScenarioBar bar;
    public Table root;
    public Stack stack;
    public Table controlsTable;
    public ImageButton rewindBackButton, rewindForwardButton, pauseButton, playButton, skipForwardButton, skipBackButton;
    public Timer.Task fadeOutTask, progressTask;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public StoryProgressBar(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        interaction = null;
        assetManager = gameScreen.assetManager;
    }

    public StoryProgressBar(GameScreen gameScreen, I interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
        assetManager = interaction.assetManager;
    }

    public void create() {

        Log.i(tag, "create");

        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_pause.png", Texture.class);
        assetManager.load("ui/playbar_play.png", Texture.class);
        assetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_forward.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);

        assetManager.load("ui/elements_player_fond_1.png", Texture.class);
        assetManager.load("ui/elements_player_fond_2.png", Texture.class);
        assetManager.load("ui/elements_player_fond_3.png", Texture.class);

        assetManager.finishLoading();

        Texture backTexture = assetManager.get("ui/elements_player_fond_1.png", Texture.class);
        Texture sideTexture = assetManager.get("ui/elements_player_fond_3.png", Texture.class);
        Texture centerTexture = assetManager.get("ui/elements_player_fond_2.png", Texture.class);

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

                    GameState.pause(tag);

                    gameScreen.game.sfxService.onProgressPause();

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

                                stack.setTouchable(Touchable.enabled);

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

        stack.setTouchable(Touchable.disabled);

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
        return isMounted() && root.isVisible() && !isFadeOut;
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

        if (progressTask != null) {
            progressTask.cancel();
            progressTask = null;
        }

        cancelFadeOut();
        isFadeIn = false;
        isFadeOut = false;

        gameScreen.game.stopCurrentSfx();
    }

    private ImageButton getRewindBack() {
        Texture rewindBack = assetManager.get("ui/playbar_rewind_backward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindBack));
        style.down = new SpriteDrawable(new Sprite(rewindBack));
        style.disabled = new SpriteDrawable(new Sprite(rewindBack));

        return new ProgressIconButton(style);
    }

    private ImageButton getSkipBack() {
        Texture skipBack = assetManager.get("ui/playbar_skip_backward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBack));

        return new ProgressIconButton(style);
    }

    private ImageButton getSkipForward() {
        Texture skipForward = assetManager.get("ui/playbar_skip_forward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipForward));
        style.down = new SpriteDrawable(new Sprite(skipForward));
        style.disabled = new SpriteDrawable(new Sprite(skipForward));

        return new ProgressIconButton(style);
    }


    private ImageButton getRewindForward() {
        Texture rewindForward = assetManager.get("ui/playbar_rewind_forward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindForward));
        style.down = new SpriteDrawable(new Sprite(rewindForward));
        style.disabled = new SpriteDrawable(new Sprite(rewindForward));

        return new ProgressIconButton(style);
    }

    private ImageButton getPause() {
        Texture pause = assetManager.get("ui/playbar_pause.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(pause));
        style.down = new SpriteDrawable(new Sprite(pause));
        style.disabled = new SpriteDrawable(new Sprite(pause));

        return new ProgressIconButton(style);
    }

    private ImageButton getPlay() {
        Texture play = assetManager.get("ui/playbar_play.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(play));
        style.down = new SpriteDrawable(new Sprite(play));
        style.disabled = new SpriteDrawable(new Sprite(play));

        return new ProgressIconButton(style);
    }

    public abstract void postProgressChanged();

    public abstract void startCurrentMusicIfPaused();

    public void restartScenario() {
        try {

            if (interaction != null && !interaction.isValid()) return;

            gameScreen.game.stopCurrentSfx();

            Log.i(tag, "restartScenario");

            GameState.unpause(tag);

            if (progressTask != null) {
                progressTask.cancel();
                progressTask = null;
            }

            startCurrentMusicIfPaused();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
