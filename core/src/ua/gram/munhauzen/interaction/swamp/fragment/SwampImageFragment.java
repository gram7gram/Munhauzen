package ua.gram.munhauzen.interaction.swamp.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SwampImageFragment extends Fragment {

    private final SwampInteraction interaction;
    public Group root;
    public Image background, swamp, munhauzen;
    public Table backgroundTable, swampTable, munhauzenTable;
    Timer.Task task;

    public SwampImageFragment(SwampInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        final Texture backTex = interaction.assetManager.get("swamp/int_swamp_1.jpg", Texture.class);
        final Texture munauzenTex = interaction.assetManager.get("swamp/int_swamp_2.png", Texture.class);
        final Texture swampTex = interaction.assetManager.get("swamp/int_swamp_3.png", Texture.class);

        background = new FitImage();
        swamp = new FitImage();
        munhauzen = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        swampTable = new Table();
        swampTable.setFillParent(true);
        swampTable.add(swamp).bottom().expand().fill();

        munhauzenTable = new Table();
        munhauzenTable.setFillParent(true);
        munhauzenTable.add(munhauzen).center();

        munhauzen.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                if (munhauzen.getTouchable() == Touchable.disabled) return;

                try {
                    float newY = munhauzen.getY() + (deltaY);
                    float currentHeight = munhauzen.getHeight();

                    float topBound = MunhauzenGame.WORLD_HEIGHT - currentHeight;
                    float bottomBound = -currentHeight * .3f;

                    if (bottomBound < newY && newY < topBound) {
                        munhauzen.setY(newY);
                    }

                    if (munhauzen.getY() < bottomBound) munhauzen.setY(bottomBound);
                    if (munhauzen.getY() > topBound) munhauzen.setY(topBound);

                    checkIfWinner();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        task = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                float newY = munhauzen.getY() - new Random().between(2, 5);
                float currentHeight = munhauzen.getHeight();

                float bottomBound = -currentHeight * .3f;

                munhauzen.setY(newY);

                if (munhauzen.getY() < bottomBound) munhauzen.setY(bottomBound);

            }
        }, 0, .01f);

        root = new Group();
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(backgroundTable);
        root.addActor(munhauzenTable);
        root.addActor(swampTable);

        root.setName(tag);

        setBackground(backTex);

        setSwampBackground(swampTex);

        setMunhauzenBackground(munauzenTex);
    }

    public void update() {

    }

    private void checkIfWinner() {

        if (munhauzen.getY() > swamp.getY() + swamp.getHeight()) {
            munhauzen.setTouchable(Touchable.disabled);

            complete();
        }
    }

    private void cancelTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void complete() {
        Log.i(tag, "complete");

        cancelTask();

        munhauzen.addAction(Actions.repeat(5,
                Actions.sequence(
                        Actions.moveBy(5, 0, .1f),
                        Actions.moveBy(0, -5, .1f),
                        Actions.moveBy(-5, 0, .1f),
                        Actions.moveBy(0, 5, .1f)
                )
        ));

        float delay = playWin();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    interaction.gameScreen.interactionService.complete();

                    interaction.gameScreen.interactionService.findStoryAfterInteraction();

                    interaction.gameScreen.restoreProgressBarIfDestroyed();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }, delay / 1000f);

    }


    private float playWin() {
        try {
            StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = "s24_a";

            interaction.gameScreen.audioService.prepareAndPlay(storyAudio);

            return storyAudio.duration;

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return 1000;
    }


    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * width / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * scale;

        backgroundTable.getCell(background)
                .width(width)
                .height(height);
    }

    public void setSwampBackground(Texture texture) {

        swamp.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * width / swamp.getDrawable().getMinWidth();
        float height = 1f * swamp.getDrawable().getMinHeight() * scale;

        swampTable.getCell(swamp)
                .width(width)
                .height(height);
    }

    public void setMunhauzenBackground(Texture texture) {

        munhauzen.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * width / munhauzen.getDrawable().getMinWidth();
        float height = 1f * munhauzen.getDrawable().getMinHeight() * scale;

        munhauzenTable.getCell(munhauzen)
                .width(width)
                .height(height);

        munhauzen.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.delay(.5f),
                Actions.moveTo((MunhauzenGame.WORLD_WIDTH - width) / 2f, -height * .3f),
                Actions.parallel(
                        Actions.alpha(1, .3f)
                )
        ));
    }

    @Override
    public void dispose() {
        super.dispose();

        cancelTask();

    }
}
