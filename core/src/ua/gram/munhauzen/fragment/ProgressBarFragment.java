package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.assets.AssetManager;
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
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ProgressBarFragment extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final GameScreen gameScreen;
    public final AssetManager assetManager;

    public ProgressBar bar;
    public Table root;
    public Stack stack;
    public Table controlsTable;
    public ImageButton skipBackButton, rewindBackButton, skipForwardButton, rewindForwardButton, pauseButton, playButton;
    private Timer.Task fadeOutTask;

    public ProgressBarFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new AssetManager();
    }

    public void create() {

        Log.i(tag, "create");

        assetManager.load("ui/playbar_pause.png", Texture.class);
        assetManager.load("ui/playbar_play.png", Texture.class);

        assetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_backward_off.png", Texture.class);

        assetManager.load("ui/playbar_rewind_forward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_forward_off.png", Texture.class);

        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward_off.png", Texture.class);

        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward_off.png", Texture.class);

        assetManager.load("ui/elements_player_fond_1.png", Texture.class);
        assetManager.load("ui/elements_player_fond_2.png", Texture.class);
        assetManager.load("ui/elements_player_fond_3.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);

        assetManager.finishLoading();

        Texture line = assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class);
        Texture knob = assetManager.get("ui/player_progress_bar_knob.png", Texture.class);
        Texture backTexture = assetManager.get("ui/elements_player_fond_1.png", Texture.class);
        Texture sideTexture = assetManager.get("ui/elements_player_fond_3.png", Texture.class);
        Texture centerTexture = assetManager.get("ui/elements_player_fond_2.png", Texture.class);

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

                    GameState.isPaused = false;

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

                    gameScreen.audioService.stop();

                    GameState.isPaused = true;
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
                    gameScreen.audioService.stop();

                    if (gameScreen.scenarioFragment != null) {
                        gameScreen.scenarioFragment.fadeOut(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    gameScreen.scenarioFragment.destroy();
                                    gameScreen.scenarioFragment = null;
                                } catch (Throwable e) {
                                    Log.e(tag, e);
                                }
                            }
                        });
                    }

                    Story story = gameScreen.getStory();
                    if (story.currentScenario == null) return;

                    StoryScenario previous = story.currentScenario.previous;
                    if (previous == null) {
                        return;
                    }

                    Log.i(tag, "skipBackButton to " + previous.scenario.name + " at " + previous.startsAt + " ms");

                    GameState.isPaused = true;

                    story.update(previous.startsAt, story.totalDuration);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                try {
                    GameState.isPaused = false;

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
                    gameScreen.audioService.stop();

                    Story story = gameScreen.getStory();
                    if (story.currentScenario == null) return;

                    StoryScenario next = story.currentScenario.next;
                    if (next == null) {
                        return;
                    }

                    Log.i(tag, "skipForwardButton to " + next.scenario.name + " at " + next.startsAt + " ms");

                    GameState.isPaused = true;

                    story.update(next.startsAt, story.totalDuration);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                try {
                    GameState.isPaused = false;

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

                    gameScreen.audioService.stop();

                    if (gameScreen.scenarioFragment != null) {
                        gameScreen.scenarioFragment.fadeOut(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    gameScreen.scenarioFragment.destroy();
                                    gameScreen.scenarioFragment = null;
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
                                Story story = gameScreen.getStory();

                                GameState.isPaused = true;

                                story.progress -= story.totalDuration * 0.025f;

                                story.update(story.progress, story.totalDuration);

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

                    GameState.isPaused = false;

                    progressTask.cancel();
                    progressTask = null;

                    startCurrentMusicIfPaused();
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

                    gameScreen.audioService.stop();

                    progressTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                Story story = gameScreen.getStory();

                                GameState.isPaused = true;

                                story.progress += story.totalDuration * 0.025f;

                                story.update(story.progress, story.totalDuration);

                                if (story.isValid()) {
                                    if (story.isCompleted) {
                                        if (gameScreen.scenarioFragment == null) {
                                            gameScreen.storyManager.onCompleted();
                                        }

                                    }
                                }

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

                    GameState.isPaused = false;

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
                    gameScreen.audioService.stop();

                    if (gameScreen.scenarioFragment != null) {
                        gameScreen.scenarioFragment.fadeOut(new Runnable() {
                            @Override
                            public void run() {
                                gameScreen.scenarioFragment.destroy();
                                gameScreen.scenarioFragment = null;
                            }
                        });
                    }

                    Story story = gameScreen.getStory();

                    story.update(story.totalDuration * percent, story.totalDuration);

                    if (story.isValid()) {
                        if (story.isCompleted) {
                            if (gameScreen.scenarioFragment == null) {
                                gameScreen.storyManager.onCompleted();
                            }

                        }
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                try {
                    GameState.isPaused = false;

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

                    GameState.isPaused = true;

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

                    GameState.isPaused = true;

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

        root = new Table();
        root.add(stack).align(Align.bottom).fillX().expand().row();

        root.setName(tag);

    }

    public float getHeight() {
        return stack.getHeight();
    }

    public void update() {

        if (!isMounted()) return;

        Story story = gameScreen.getStory();

        boolean hasPrevious = false, hasNext = false;

        if (story.currentScenario != null) {
            hasPrevious = story.currentScenario.previous != null;
            hasNext = story.currentScenario.next != null;
        }

        pauseButton.setVisible(!GameState.isPaused);
        playButton.setVisible(GameState.isPaused);

        skipForwardButton.setDisabled(story.isCompleted || !hasNext);
        rewindForwardButton.setDisabled(story.isCompleted);

        skipForwardButton.setTouchable(skipForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);
        rewindForwardButton.setTouchable(rewindForwardButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        skipBackButton.setDisabled(story.progress == 0 || !hasPrevious);
        rewindBackButton.setDisabled(story.progress == 0);

        skipBackButton.setTouchable(skipBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);
        rewindBackButton.setTouchable(rewindBackButton.isDisabled() ? Touchable.disabled : Touchable.enabled);

        bar.setRange(0, story.totalDuration);
        bar.setValue(story.progress);
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

        Log.i(tag, "fadeIn");

        if (fadeOutTask != null) {
            fadeOutTask.cancel();
        }

        root.setVisible(true);
        //root.setTouchable(Touchable.enabled);
        root.clearActions();
        root.addAction(
                Actions.parallel(
                        Actions.fadeIn(.3f),
                        Actions.moveTo(0, 0, .3f)
                )
        );
    }

    public void fadeOut() {

        if (!isMounted()) return;

        Log.i(tag, "fadeOut");

        //root.setTouchable(Touchable.disabled);
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
                            }
                        })
                )
        );
    }

    public void scheduleFadeOut() {

        if (!isMounted()) return;

        Log.i(tag, "scheduleFadeOut");

        if (fadeOutTask != null) {
            fadeOutTask.cancel();
        }
        fadeOutTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadeOut();
            }
        }, 10);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.clear();

        if (fadeOutTask != null) {
            fadeOutTask.cancel();
            fadeOutTask = null;
        }
    }

    private ImageButton getSkipBack() {
        Texture skipBack = assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }

    private ImageButton getRewindBack() {
        Texture rewindBack = assetManager.get("ui/playbar_rewind_backward.png", Texture.class);
        Texture rewindBackOff = assetManager.get("ui/playbar_rewind_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindBack));
        style.down = new SpriteDrawable(new Sprite(rewindBack));
        style.disabled = new SpriteDrawable(new Sprite(rewindBackOff));

        return new ImageButton(style);
    }

    private ImageButton getRewindForward() {
        Texture rewindForward = assetManager.get("ui/playbar_rewind_forward.png", Texture.class);
        Texture rewindForwardOff = assetManager.get("ui/playbar_rewind_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(rewindForward));
        style.down = new SpriteDrawable(new Sprite(rewindForward));
        style.disabled = new SpriteDrawable(new Sprite(rewindForwardOff));

        return new ImageButton(style);
    }

    private ImageButton getSkipForward() {
        Texture skipForward = assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipForwardOff = assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipForward));
        style.down = new SpriteDrawable(new Sprite(skipForward));
        style.disabled = new SpriteDrawable(new Sprite(skipForwardOff));

        return new ImageButton(style);
    }

    private ImageButton getPause() {
        Texture pause = assetManager.get("ui/playbar_pause.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(pause));
        style.down = new SpriteDrawable(new Sprite(pause));
        style.disabled = new SpriteDrawable(new Sprite(pause));

        return new ImageButton(style);
    }

    private ImageButton getPlay() {
        Texture play = assetManager.get("ui/playbar_play.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(play));
        style.down = new SpriteDrawable(new Sprite(play));
        style.disabled = new SpriteDrawable(new Sprite(play));

        return new ImageButton(style);
    }
}
