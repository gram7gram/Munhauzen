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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ProgressBarFragment implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    public ProgressBar bar;
    public Stack barContainer;
    public Table controlsTable;
    public ImageButton skipBackButton, rewindBackButton, skipForwardButton, rewindForwardButton, pauseButton, playButton;
    public final AssetManager assetManager;

    public ProgressBarFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new AssetManager();
    }

    public Stack create() {

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

        assetManager.load("ui/player_progress_bar.9.png", Texture.class);
        assetManager.load("ui/player_progress_bar_right.9.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);

        assetManager.finishLoading();

        Texture line = assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class);
        Texture knob = assetManager.get("ui/player_progress_bar_knob.png", Texture.class);
        Texture backLeft = assetManager.get("ui/player_progress_bar.9.png", Texture.class);
        Texture backRight = assetManager.get("ui/player_progress_bar_right.9.png", Texture.class);
        Texture pause = assetManager.get("ui/playbar_pause.png", Texture.class);
        Texture play = assetManager.get("ui/playbar_play.png", Texture.class);
        Texture rewindBack = assetManager.get("ui/playbar_rewind_backward.png", Texture.class);
        Texture rewindBackOff = assetManager.get("ui/playbar_rewind_backward_off.png", Texture.class);
        Texture rewindForward = assetManager.get("ui/playbar_rewind_forward.png", Texture.class);
        Texture rewindForwardOff = assetManager.get("ui/playbar_rewind_forward_off.png", Texture.class);
        Texture skipBack = assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);
        Texture skipForward = assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipForwardOff = assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        Sprite knobSprite = new Sprite(knob);

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new NinePatchDrawable(new NinePatch(
                line,
                10, 10, 0, 0
        ));
        barStyle.knob = new SpriteDrawable(knobSprite);

        bar = new ProgressBar(0, 100, 1, false, barStyle);

        Image barBackgroundImageLeft = new Image(new NinePatchDrawable(new NinePatch(
                backLeft,
                130, 500 - 270, 0, 0
        )));

        Image barBackgroundImageRight = new Image(new NinePatchDrawable(new NinePatch(
                backRight,
                260, 500 - 360, 0, 0
        )));

        ImageButton.ImageButtonStyle skipBackStyle = new ImageButton.ImageButtonStyle();
        skipBackStyle.up = new SpriteDrawable(new Sprite(skipBack));
        skipBackStyle.down = new SpriteDrawable(new Sprite(skipBack));
        skipBackStyle.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        ImageButton.ImageButtonStyle rewindBackStyle = new ImageButton.ImageButtonStyle();
        rewindBackStyle.up = new SpriteDrawable(new Sprite(rewindBack));
        rewindBackStyle.down = new SpriteDrawable(new Sprite(rewindBack));
        rewindBackStyle.disabled = new SpriteDrawable(new Sprite(rewindBackOff));

        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.up = new SpriteDrawable(new Sprite(play));
        playStyle.down = new SpriteDrawable(new Sprite(play));
        playStyle.disabled = new SpriteDrawable(new Sprite(play));

        ImageButton.ImageButtonStyle pauseStyle = new ImageButton.ImageButtonStyle();
        pauseStyle.up = new SpriteDrawable(new Sprite(pause));
        pauseStyle.down = new SpriteDrawable(new Sprite(pause));
        pauseStyle.disabled = new SpriteDrawable(new Sprite(pause));

        ImageButton.ImageButtonStyle rewindForwardStyle = new ImageButton.ImageButtonStyle();
        rewindForwardStyle.up = new SpriteDrawable(new Sprite(rewindForward));
        rewindForwardStyle.down = new SpriteDrawable(new Sprite(rewindForward));
        rewindForwardStyle.disabled = new SpriteDrawable(new Sprite(rewindForwardOff));

        ImageButton.ImageButtonStyle skipForwardStyle = new ImageButton.ImageButtonStyle();
        skipForwardStyle.up = new SpriteDrawable(new Sprite(skipForward));
        skipForwardStyle.down = new SpriteDrawable(new Sprite(skipForward));
        skipForwardStyle.disabled = new SpriteDrawable(new Sprite(skipForwardOff));

        skipBackButton = new ImageButton(skipBackStyle);
        rewindBackButton = new ImageButton(rewindBackStyle);
        playButton = new ImageButton(playStyle);
        pauseButton = new ImageButton(pauseStyle);
        rewindForwardButton = new ImageButton(rewindForwardStyle);
        skipForwardButton = new ImageButton(skipForwardStyle);

        Stack playPauseGroup = new Stack();
        playPauseGroup.add(playButton);
        playPauseGroup.add(pauseButton);

        int controlsSize = 50;
        int fragmentHeight = controlsSize * 4;

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
        barTable.pad(40, 100, 40, 100);
        barTable.add(controlsTable).padTop(5).padBottom(5).fillX().expandX().row();
        barTable.add(bar).fillX().expandX().height(controlsSize).row();

        Table backgroundContainer = new Table();
        backgroundContainer.add(barBackgroundImageLeft).fillX().expandX().height(fragmentHeight);
        backgroundContainer.add(barBackgroundImageRight).fillX().expandX().height(fragmentHeight);

        barContainer = new Stack();
        barContainer.addActor(backgroundContainer);
        barContainer.addActor(barTable);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "playButton clicked");

                GameState.isPaused = false;

                startCurrentMusicIfPaused();
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "pauseButton clicked");

                gameScreen.audioService.stop();

                GameState.isPaused = true;
            }
        });

        skipBackButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                gameScreen.audioService.stop();

                if (gameScreen.scenarioFragment != null) {
                    gameScreen.scenarioFragment.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            gameScreen.scenarioFragment = null;
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
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                GameState.isPaused = false;

                startCurrentMusicIfPaused();
            }
        });

        skipForwardButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

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
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                GameState.isPaused = false;

                startCurrentMusicIfPaused();
            }
        });

        rewindBackButton.addListener(new InputListener() {

            Timer.Task progressTask;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                Log.i(tag, "rewindBackButton enter");

                gameScreen.audioService.stop();

                if (gameScreen.scenarioFragment != null) {
                    gameScreen.scenarioFragment.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            gameScreen.scenarioFragment = null;
                        }
                    });
                }

                progressTask = Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Story story = gameScreen.getStory();

                        GameState.isPaused = true;
                        story.isCompleted = false;
                        story.progress -= 100;

                        story.progress = Math.max(0, story.progress);
                    }
                }, 0, 0.05f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                Log.i(tag, "rewindBackButton enter");

                GameState.isPaused = false;

                progressTask.cancel();
                progressTask = null;

                startCurrentMusicIfPaused();
            }

        });

        rewindForwardButton.addListener(new InputListener() {

            Timer.Task progressTask;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                Log.i(tag, "rewindForwardButton enter");

                gameScreen.audioService.stop();

                progressTask = Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Story story = gameScreen.getStory();

                        GameState.isPaused = true;
                        story.isCompleted = false;
                        story.progress += 100;

                        story.progress = Math.min(story.progress, story.totalDuration);
                    }
                }, 0, 0.05f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                Log.i(tag, "rewindForwardButton exit");

                GameState.isPaused = false;

                progressTask.cancel();
                progressTask = null;

                startCurrentMusicIfPaused();
            }

        });

        bar.addListener(new ActorGestureListener() {

            private void scrollTo(float percent) {

                gameScreen.audioService.stop();

                if (gameScreen.scenarioFragment != null) {
                    gameScreen.scenarioFragment.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            gameScreen.scenarioFragment = null;
                        }
                    });
                }

                Story story = gameScreen.getStory();

                story.isCompleted = false;
                story.update(story.totalDuration * percent, story.totalDuration);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                GameState.isPaused = false;

                startCurrentMusicIfPaused();
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                float totalLength = Math.max(1, bar.getWidth());

                float percent = x / totalLength;

                GameState.isPaused = true;

                scrollTo(percent);
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                float totalLength = Math.max(1, bar.getWidth());

                float percent = x / totalLength;

                GameState.isPaused = true;

                scrollTo(percent);
            }
        });

        return barContainer;
    }

    public void update() {
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

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
