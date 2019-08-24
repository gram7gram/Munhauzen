package ua.gram.munhauzen.interaction.swamp.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SwampImageFragment extends InteractionFragment {

    private final SwampInteraction interaction;
    public FragmentRoot root;
    public Image swamp, munhauzen;
    public Table swampTable, munhauzenTable;
    public BackgroundImage backgroundImage;
    Timer.Task task;
    StoryAudio winAudio, bounceAudio, pullAudio;

    public SwampImageFragment(SwampInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        final Texture munauzenTex = interaction.assetManager.get("swamp/int_swamp_2.png", Texture.class);
        final Texture swampTex = interaction.assetManager.get("swamp/int_swamp_3.png", Texture.class);

        swamp = new FitImage();
        munhauzen = new FitImage();

        swampTable = new Table();
        swampTable.setFillParent(true);
        swampTable.add(swamp).bottom().expand().fill();

        munhauzenTable = new Table();
        munhauzenTable.setFillParent(true);
        munhauzenTable.add(munhauzen).center();

        munhauzen.addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                playPull();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                pausePull();
            }

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

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
        root.addContainer(munhauzenTable);
        root.addContainer(swampTable);

        root.setName(tag);

        setBackground(
                interaction.assetManager.get("swamp/int_swamp_1.jpg", Texture.class),
                "swamp/int_swamp_1.jpg"
        );

        setSwampBackground(swampTex);

        setMunhauzenBackground(munauzenTex);
    }

    public void update() {
        if (winAudio != null) {
            interaction.gameScreen.audioService.updateVolume(winAudio);
        }

        if (pullAudio != null) {
            interaction.gameScreen.audioService.updateVolume(pullAudio);
        }

        if (bounceAudio != null) {
            interaction.gameScreen.audioService.updateVolume(bounceAudio);
        }
    }

    private void checkIfWinner() {

        if (munhauzen.getY() > swamp.getY() + swamp.getHeight()) {
            munhauzen.setTouchable(Touchable.disabled);

            pausePull();

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

        playBounceBeforeWin();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    playWin();

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            try {

                                interaction.gameScreen.interactionService.complete();

                                interaction.gameScreen.interactionService.findStoryAfterInteraction();

                                interaction.gameScreen.restoreProgressBarIfDestroyed();

                            } catch (Throwable e) {
                                Log.e(tag, e);

                                interaction.gameScreen.onCriticalError(e);
                            }
                        }
                    }, winAudio.duration / 1000f);

                } catch (Throwable e) {
                    Log.e(tag, e);

                    interaction.gameScreen.onCriticalError(e);
                }
            }
        }, bounceAudio.duration / 1000f);

    }

    private void playBounceBeforeWin() {
        try {

            if (bounceAudio != null && bounceAudio.player != null) {
                bounceAudio.player.play();
                return;
            }

            bounceAudio = new StoryAudio();
            bounceAudio.name = "sfx_inter_swamp_2";

            interaction.gameScreen.audioService.prepareAndPlay(bounceAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }


    private void playWin() {
        try {
            winAudio = new StoryAudio();
            winAudio.name = "s24_a";

            interaction.gameScreen.audioService.prepareAndPlay(winAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void pausePull() {
        if (pullAudio != null && pullAudio.player != null) {
            pullAudio.player.pause();
        }
    }

    private void playPull() {
        try {
            if (pullAudio != null && pullAudio.player != null) {
                pullAudio.player.play();
                return;
            }

            pullAudio = new StoryAudio();
            pullAudio.name = "sfx_inter_swamp_1";

            interaction.gameScreen.audioService.prepareAndPlay(pullAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }


    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);

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
                Actions.moveTo(.7f * (MunhauzenGame.WORLD_WIDTH - width) / 2f, -height * .3f),
                Actions.alpha(1, .2f)
        ));
    }

    @Override
    public void dispose() {
        super.dispose();

        cancelTask();

        if (winAudio != null) {
            interaction.gameScreen.audioService.stop(winAudio);
            winAudio = null;
        }

        if (bounceAudio != null) {
            interaction.gameScreen.audioService.stop(bounceAudio);
            bounceAudio = null;
        }

        if (pullAudio != null) {
            interaction.gameScreen.audioService.stop(pullAudio);
            pullAudio = null;
        }

    }
}
