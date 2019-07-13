package ua.gram.munhauzen.interaction.slap.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.SlapInteraction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SlapImageFragment extends Fragment {

    private final SlapInteraction interaction;
    public Stack root;
    public Image beforeImg, afterImg, betweenImg, doorsImg;
    public Table beforeTable, afterTable, betweenTable, headerTable, doorsTable;
    Group group;
    float afterWidth, betweenWidth, beforeWidth;
    StoryAudio introAudio, winAudio;

    public SlapImageFragment(SlapInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        Label header = new Label("Slap him!", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));
        header.setAlignment(Align.center);

        beforeImg = new FitImage();
        afterImg = new FitImage();
        betweenImg = new FitImage();
        doorsImg = new Image();

        beforeTable = new Table();
        beforeTable.setFillParent(true);
        beforeTable.add(beforeImg).center().expand().fill();

        afterTable = new Table();
        afterTable.setFillParent(true);
        afterTable.add(afterImg).center().expand().fill();

        betweenTable = new Table();
        betweenTable.setFillParent(true);
        betweenTable.add(betweenImg).center().expand().fill();

        doorsTable = new Table();
        doorsTable.setFillParent(true);
        doorsTable.add(doorsImg).center().expand().fill();
        doorsTable.setTouchable(Touchable.disabled);

        headerTable = new Table();
        headerTable.setFillParent(true);
        headerTable.pad(10);
        headerTable.add(header).top().expand();
        headerTable.setTouchable(Touchable.disabled);

        group = new Group();
        group.addActor(beforeTable);
        group.addActor(betweenTable);
        group.addActor(afterTable);

        group.addListener(new ActorGestureListener() {

            int delta;

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                delta = (int) x;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                delta = (int) (delta - x);

                if (delta > 100) {
                    start();
                }
            }
        });

        root = new Stack();
        root.setFillParent(true);
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(group);
        root.addActor(doorsTable);
        root.addActor(headerTable);

        root.setName(tag);

        setBeforeBackground(interaction.assetManager.get("slap/inter_slap_1.jpg", Texture.class));

        setAfterBackground(interaction.assetManager.get("slap/inter_slap_2.jpg", Texture.class));

        setDoorsBackground(interaction.assetManager.get("slap/inter_slap_doors.png", Texture.class));

        setBetweenBackground(interaction.assetManager.get("slap/inter_slap_3.jpg", Texture.class));

        playIntro();
    }

    public void update() {

        beforeImg.setX(0);
        betweenImg.setX(beforeImg.getX() + beforeWidth);
        afterImg.setX(betweenImg.getX() + betweenWidth);

        if (introAudio != null) {
            if (introAudio.player != null) {
                interaction.gameScreen.audioService.updateVolume(introAudio);
            }
        }

        if (winAudio != null) {
            if (winAudio.player != null) {
                interaction.gameScreen.audioService.updateVolume(winAudio);
            }
        }

    }

    public void start() {

        Log.i(tag, "start");

        if (introAudio != null) {
            interaction.gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

        playWin();

        int animationDuration = 8;

        group.addAction(
                Actions.moveTo(-beforeWidth - betweenWidth, group.getY(), animationDuration, Interpolation.fastSlow)
        );

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                complete();
            }
        }, Math.max(winAudio.duration / 1000f, animationDuration) + 2);
    }

    private void complete() {
        Log.i(tag, "complete");

        try {
            interaction.gameScreen.interactionService.complete();

            interaction.gameScreen.interactionService.findStoryAfterInteraction();

            interaction.gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    private void playWin() {
        try {
            winAudio = new StoryAudio();
            winAudio.audio = "sfx_inter_slap_2";

            interaction.gameScreen.audioService.prepareAndPlay(winAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            winAudio.duration = 1000;
        }
    }

    private void playIntro() {
        try {
            introAudio = new StoryAudio();
            introAudio.audio = "sfx_inter_slap_1";

            interaction.gameScreen.audioService.prepareAndPlay(introAudio);

            introAudio.player.setLooping(true);

        } catch (Throwable e) {
            Log.e(tag, e);

            introAudio.duration = 1000;
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBeforeBackground(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        beforeImg.setDrawable(new SpriteDrawable(new Sprite(texture)));

        beforeWidth = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * beforeWidth / beforeImg.getDrawable().getMinWidth();
        float height = 1f * beforeImg.getDrawable().getMinHeight() * scale;

        beforeTable.getCell(beforeImg)
                .width(beforeWidth)
                .height(height);
    }

    public void setDoorsBackground(Texture texture) {

        doorsImg.setDrawable(new SpriteDrawable(new Sprite(texture)));

        doorsTable.getCell(doorsImg)
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(MunhauzenGame.WORLD_HEIGHT);
    }

    public void setAfterBackground(Texture texture) {

        afterImg.setDrawable(new SpriteDrawable(new Sprite(texture)));

        afterWidth = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * afterWidth / afterImg.getDrawable().getMinWidth();
        float height = 1f * afterImg.getDrawable().getMinHeight() * scale;

        afterTable.getCell(afterImg)
                .width(afterWidth)
                .height(height);
    }

    public void setBetweenBackground(Texture texture) {

        betweenImg.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float height = MunhauzenGame.WORLD_HEIGHT;
        float scale = 1f * height / betweenImg.getDrawable().getMinHeight();
        betweenWidth = 1f * betweenImg.getDrawable().getMinWidth() * scale;

        betweenTable.getCell(betweenImg)
                .width(betweenWidth)
                .height(height);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (introAudio != null) {
            interaction.gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

        if (winAudio != null) {
            interaction.gameScreen.audioService.stop(winAudio);
            winAudio = null;
        }


    }
}
