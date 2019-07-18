package ua.gram.munhauzen.interaction.balloons.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.BalloonsInteraction;
import ua.gram.munhauzen.interaction.balloons.ui.Balloon;
import ua.gram.munhauzen.interaction.balloons.ui.Cloud;
import ua.gram.munhauzen.interaction.hare.animation.DucksAnimation;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class BalloonsImageFragment extends Fragment {

    private final BalloonsInteraction interaction;
    public Group root;
    public Image background;
    public Table backgroundTable;
    PrimaryButton resetButton;
    Balloon balloon1, balloon2, balloon3, balloon4;
    Label progressLabel;
    Table titleTable, restartTable, winTable;
    int progress, max = 21, spawnCount;
    Timer.Task task;
    StoryAudio introAudio, winAudio, missAudio, balloon21Audio;

    public BalloonsImageFragment(BalloonsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        final Texture backTex = interaction.assetManager.get("balloons/inter_balloons_fond.jpg", Texture.class);
        Texture cloud1Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_1.png", Texture.class);
        Texture cloud2Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_2.png", Texture.class);
        Texture cloud3Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_3.png", Texture.class);
        Texture ducksTexture = interaction.assetManager.get("balloons/ducks_sheet_1x5.png", Texture.class);
        final Texture bal1Texture = interaction.assetManager.get("balloons/inter_balloons_1.png", Texture.class);
        Texture bal2Texture = interaction.assetManager.get("balloons/inter_balloons_2.png", Texture.class);
        Texture bal3Texture = interaction.assetManager.get("balloons/inter_balloons_3.png", Texture.class);
        Texture bal4Texture = interaction.assetManager.get("balloons/inter_balloons_4.png", Texture.class);

        DucksAnimation ducks = new DucksAnimation(ducksTexture);
        int cloudSize = (int) (MunhauzenGame.WORLD_WIDTH / 4f);
        int balloonSize = (int) (MunhauzenGame.WORLD_WIDTH / 8f);

        balloon1 = new Balloon(interaction, bal1Texture, balloonSize, (int) (balloonSize * 1.5f));
        balloon2 = new Balloon(interaction, bal2Texture, balloonSize, (int) (balloonSize * 1.5f));
        balloon3 = new Balloon(interaction, bal3Texture, balloonSize, (int) (balloonSize * 1.5f));
        balloon4 = new Balloon(interaction, bal4Texture, balloonSize, (int) (balloonSize * 1.5f));

        Cloud cloud1 = new Cloud(cloud1Texture,
                cloudSize, cloudSize / 2,
                -cloudSize, MunhauzenGame.WORLD_HEIGHT * .9f);

        Cloud cloud2 = new Cloud(cloud2Texture,
                cloudSize, cloudSize / 2,
                -cloudSize * 2, MunhauzenGame.WORLD_HEIGHT * .8f);

        Cloud cloud3 = new Cloud(cloud3Texture,
                cloudSize, cloudSize / 2,
                -cloudSize - 100, MunhauzenGame.WORLD_HEIGHT * .75f);

        Label title = new Label("Catch them all!", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        Label restartTitle = new Label("One was missed! Eh!", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));
        restartTitle.setWrap(true);
        restartTitle.setAlignment(Align.center);

        Label winTitle = new Label("Nice! Full score!", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));
        winTitle.setWrap(true);
        winTitle.setAlignment(Align.center);

        progressLabel = new Label("", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));
        progressLabel.setWrap(false);
        progressLabel.setAlignment(Align.right);

        ducks.start();
        cloud1.start();
        cloud2.start();
        cloud3.start();

        background = new FitImage(backTex);

        resetButton = interaction.gameScreen.game.buttonBuilder.primary("Retry", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "restart");

                restartTable.addAction(Actions.sequence(
                        Actions.alpha(0, .3f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                start();
                            }
                        })
                ));
            }
        });

        PrimaryButton completeButton = interaction.gameScreen.game.buttonBuilder.primary("Continue", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                root.addAction(Actions.sequence(
                        Actions.alpha(0, .3f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                interaction.complete();
                            }
                        })
                ));
            }
        });

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.pad(10);
        titleTable.add(title).top().expand().row();
        titleTable.add(progressLabel).align(Align.bottomRight).expand().row();

        restartTable = new Table();
        restartTable.setFillParent(true);
        restartTable.pad(10);
        restartTable.add(restartTitle).padBottom(20).center().expandX().row();
        restartTable.add(resetButton).center().expandX()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f)
                .row();

        winTable = new Table();
        winTable.setFillParent(true);
        winTable.pad(10);
        winTable.add(winTitle).padBottom(20).center().expandX().row();
        winTable.add(completeButton).center().expandX()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f)
                .row();

        setBackground(backTex);

        root = new Group();
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(backgroundTable);
        root.addActor(cloud1);
        root.addActor(cloud2);
        root.addActor(cloud3);
        root.addActor(ducks);
        root.addActor(balloon1);
        root.addActor(balloon2);
        root.addActor(balloon3);
        root.addActor(balloon4);
        root.addActor(titleTable);
        root.addActor(restartTable);
        root.addActor(winTable);

        root.setName(tag);

        Runnable onMiss = new Runnable() {
            @Override
            public void run() {

                playMiss();

                failed();
            }
        };

        balloon1.onMiss = onMiss;
        balloon2.onMiss = onMiss;
        balloon3.onMiss = onMiss;
        balloon4.onMiss = onMiss;

        balloon1.addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                Log.i(tag, "Clicked balloon");

                progress += 1;

                checkProgress();

                playHit();

                balloon1.onHit();
            }
        });

        balloon2.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                Log.i(tag, "Clicked balloon");

                progress += 1;

                checkProgress();

                playHit();

                balloon2.onHit();
            }
        });

        balloon3.addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                Log.i(tag, "Clicked balloon");

                progress += 1;

                checkProgress();

                playHit();

                balloon3.onHit();
            }
        });

        balloon4.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                Log.i(tag, "Clicked balloon");

                progress += 1;

                checkProgress();

                playHit();

                balloon4.onHit();
            }
        });

        balloon1.setVisible(false);
        balloon2.setVisible(false);
        balloon3.setVisible(false);
        balloon4.setVisible(false);

        playIntro();

        start();
    }

    private void checkProgress() {
        if (progress == max) {
            win();
        }
    }

    private void start() {
        restartTable.setVisible(false);
        winTable.setVisible(false);

        progress = 0;
        spawnCount = 0;

        task = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnBalloon();
            }
        }, 1, 2.5f);
    }

    private ArrayList<Balloon> getActiveBalloons() {

        ArrayList<Balloon> balloons = new ArrayList<>();
        if (!balloon1.isLocked) balloons.add(balloon1);
        if (!balloon2.isLocked) balloons.add(balloon2);
        if (!balloon3.isLocked) balloons.add(balloon3);
        if (!balloon4.isLocked) balloons.add(balloon4);

        return balloons;
    }

    private void spawnBalloon() {

        Balloon balloon = MathUtils.random(getActiveBalloons());
        if (balloon != null) {

            ++spawnCount;

            Log.i(tag, "spawnBalloon " + spawnCount + "/" + max);

            boolean isLast = spawnCount == max;

            if (isLast) {
                balloon.startFast(new Runnable() {
                    @Override
                    public void run() {
                        stopBalloon21();
                    }
                });
            } else {
                balloon.start();
            }

            if (isLast) {
                stopSpawn();

                playBalloon21();
            }
        }
    }

    private void stopSpawn() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void win() {

        Log.i(tag, "win");

        spawnCount = 0;

        stopSpawn();

        titleTable.addAction(Actions.alpha(0, .5f));

        winTable.setVisible(true);
        winTable.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));

        playWin();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    balloon1.reset();

                    balloon2.reset();

                    balloon3.reset();

                    balloon4.reset();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }, 1);
    }

    private void failed() {
        Log.i(tag, "failed");

        spawnCount = 0;

        balloon1.reset();

        balloon2.reset();

        balloon3.reset();

        balloon4.reset();

        stopSpawn();

        restartTable.setVisible(true);
        restartTable.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));
    }

    private void playWin() {


        try {
            winAudio = new StoryAudio();
            winAudio.audio = "sfx_inter_balloons_win";

            interaction.gameScreen.audioService.prepareAndPlay(winAudio);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void playBalloon21() {
        try {
            balloon21Audio = new StoryAudio();
            balloon21Audio.audio = "sfx_inter_balloons_21";

            interaction.gameScreen.audioService.prepareAndPlay(balloon21Audio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void playIntro() {
        try {
            introAudio = new StoryAudio();
            introAudio.audio = "sfx_inter_balloons_start";

            interaction.gameScreen.audioService.prepareAndPlay(introAudio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void playHit() {
        try {
            StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_balloons_catch_1",
                    "sfx_inter_balloons_catch_2",
                    "sfx_inter_balloons_catch_3",
                    "sfx_inter_balloons_catch_4",
                    "sfx_inter_balloons_catch_5"
            });

            interaction.gameScreen.audioService.prepareAndPlay(storyAudio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void playMiss() {
        try {
            missAudio = new StoryAudio();
            missAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_balloons_loose_1",
                    "sfx_inter_balloons_loose_2",
                    "sfx_inter_balloons_loose_3"
            });

            interaction.gameScreen.audioService.prepareAndPlay(missAudio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture) {

        interaction.gameScreen.hideImageFragment();

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * scale;

        backgroundTable.getCell(background)
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(height);
    }

    public void update() {

        progressLabel.setText(progress + "/" + max);

        if (winAudio != null) {
            interaction.gameScreen.audioService.updateVolume(winAudio);
        }

        if (introAudio != null) {
            interaction.gameScreen.audioService.updateVolume(introAudio);
        }

        if (missAudio != null) {
            interaction.gameScreen.audioService.updateVolume(missAudio);
        }

    }

    private void stopBalloon21() {
        if (balloon21Audio != null) {
            interaction.gameScreen.audioService.stop(balloon21Audio);
            balloon21Audio = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        progress = 0;
        spawnCount = 0;

        stopSpawn();

        if (winAudio != null) {
            interaction.gameScreen.audioService.stop(winAudio);
            winAudio = null;
        }

        if (introAudio != null) {
            interaction.gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

        stopBalloon21();

        if (missAudio != null) {
            interaction.gameScreen.audioService.stop(missAudio);
            missAudio = null;
        }
    }
}
