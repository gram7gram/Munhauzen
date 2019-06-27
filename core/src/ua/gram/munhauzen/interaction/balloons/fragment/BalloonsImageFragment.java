package ua.gram.munhauzen.interaction.balloons.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import ua.gram.munhauzen.interaction.hare.animation.DucksAnimation;
import ua.gram.munhauzen.interaction.hare.ui.Cloud;
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
    int progress, max = 1, spawnCount, missCount;
    Timer.Task task;

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
        Texture bal1Texture = interaction.assetManager.get("balloons/inter_balloons_1.png", Texture.class);
        Texture bal2Texture = interaction.assetManager.get("balloons/inter_balloons_2.png", Texture.class);
        Texture bal3Texture = interaction.assetManager.get("balloons/inter_balloons_3.png", Texture.class);
        Texture bal4Texture = interaction.assetManager.get("balloons/inter_balloons_4.png", Texture.class);

        DucksAnimation ducks = new DucksAnimation(ducksTexture);

        balloon1 = new Balloon(bal1Texture, 100, 150, 0, -150);
        balloon2 = new Balloon(bal2Texture, 100, 150, 0, -150);
        balloon3 = new Balloon(bal3Texture, 100, 150, 0, -150);
        balloon4 = new Balloon(bal4Texture, 100, 150, 0, -150);

        Cloud cloud1 = new Cloud(cloud1Texture, 200, 100, -100, MunhauzenGame.WORLD_HEIGHT * .9f);
        Cloud cloud2 = new Cloud(cloud2Texture, 200, 100, -200, MunhauzenGame.WORLD_HEIGHT * .8f);
        Cloud cloud3 = new Cloud(cloud3Texture, 200, 100, -180, MunhauzenGame.WORLD_HEIGHT * .75f);

        Label title = new Label("Catch them all!", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

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

        resetButton = interaction.gameScreen.game.buttonBuilder.primary("Restart", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "restart");

                start();
            }
        });
        resetButton.setVisible(false);

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.pad(10);
        titleTable.add(title).top().expand().row();
        titleTable.add(resetButton).center().expand().row();
        titleTable.add(progressLabel).align(Align.bottomRight).expand().row();

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

        root.setName(tag);

        Runnable onMiss = new Runnable() {
            @Override
            public void run() {
                Log.i(tag, "missed");

                ++missCount;

                playMiss();

                if (missCount > 0 && spawnCount == max) {
                    failed();
                }
            }
        };

        balloon1.onMiss = onMiss;
        balloon2.onMiss = onMiss;
        balloon3.onMiss = onMiss;
        balloon4.onMiss = onMiss;

        balloon1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Log.i(tag, "Clicked balloon");

                progress += 1;

                playHit();

                balloon1.onHit();

                checkProgress();
            }
        });

        balloon2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Log.i(tag, "Clicked balloon");

                progress += 1;

                playHit();

                balloon2.onHit();

                checkProgress();
            }
        });

        balloon3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Log.i(tag, "Clicked balloon");

                progress += 1;

                playHit();

                balloon3.onHit();

                checkProgress();
            }
        });

        balloon4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Log.i(tag, "Clicked balloon");

                progress += 1;

                playHit();

                balloon4.onHit();

                checkProgress();
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
            complete();
        }
    }

    private void start() {
        resetButton.setVisible(false);

        progress = 0;
        spawnCount = 0;
        missCount = 0;

        task = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnBalloon();
            }
        }, 1, 1);
    }

    private void spawnBalloon() {

        ArrayList<Balloon> balloons = new ArrayList<>();
        if (!balloon1.isLocked) balloons.add(balloon1);
        if (!balloon2.isLocked) balloons.add(balloon2);
        if (!balloon3.isLocked) balloons.add(balloon3);
        if (!balloon4.isLocked) balloons.add(balloon4);

        Balloon balloon = MathUtils.random(balloons);
        if (balloon != null) {

            ++spawnCount;

            Log.i(tag, "spawnBalloon " + spawnCount + "/" + max + " " + missCount);

            balloon.start(spawnCount == max);

            if (spawnCount == max) {
                stopSpawn();
            }
        }
    }

    private void stopSpawn() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void complete() {
        Log.i(tag, "complete");

        spawnCount = 0;
        missCount = 0;

        balloon1.reset();

        balloon2.reset();

        balloon3.reset();

        balloon4.reset();

        stopSpawn();

        int delay = playWin();

//        Timer.instance().scheduleTask(new Timer.Task() {
//            @Override
//            public void run() {
//                try {
//
//                    interaction.gameScreen.interactionService.complete();
//
//                    interaction.gameScreen.interactionService.findStoryAfterInteraction();
//
//                    interaction.gameScreen.restoreProgressBarIfDestroyed();
//
//                } catch (Throwable e) {
//                    Log.e(tag, e);
//                }
//            }
//        }, delay / 1000f);
    }

    private void failed() {
        Log.i(tag, "failed");

        spawnCount = 0;
        missCount = 0;

        balloon1.reset();

        balloon2.reset();

        balloon3.reset();

        balloon4.reset();

        stopSpawn();

        resetButton.setVisible(true);
    }

    private int playWin() {

        int delay = 1000;

        try {
            StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = "sfx_inter_balloons_win";

            interaction.gameScreen.audioService.prepareAndPlay(storyAudio);

            delay = storyAudio.duration;

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return delay;
    }

    private void playIntro() {
        try {
            StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = "sfx_inter_balloons_start";

            interaction.gameScreen.audioService.prepareAndPlay(storyAudio);
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
            StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_balloons_loose_1",
                    "sfx_inter_balloons_loose_2",
                    "sfx_inter_balloons_loose_3"
            });

            interaction.gameScreen.audioService.prepareAndPlay(storyAudio);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * scale;

        backgroundTable.getCell(background)
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(height);
    }

    public void update() {

        progressLabel.setText(progress + "/" + max);

    }

    @Override
    public void dispose() {
        super.dispose();
        progress = 0;
        spawnCount = 0;
        missCount = 0;

        stopSpawn();
    }
}
