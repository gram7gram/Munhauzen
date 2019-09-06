package ua.gram.munhauzen.interaction.timer2.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.Timer2Interaction;
import ua.gram.munhauzen.interaction.timer2.Timer2Story;
import ua.gram.munhauzen.interaction.timer2.animation.Bar;
import ua.gram.munhauzen.interaction.timer2.animation.Bomb;
import ua.gram.munhauzen.interaction.timer2.animation.BoomAnimation;
import ua.gram.munhauzen.interaction.timer2.animation.SparkAnimation;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Timer2BombFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final Timer2Interaction interaction;
    public final GameScreen gameScreen;
    public Group root;
    SparkAnimation spark;
    Bomb bomb;
    Bar bar;

    public Timer2BombFragment(Timer2Interaction interaction) {
        this.gameScreen = interaction.gameScreen;
        this.interaction = interaction;
    }

    public void update() {

    }

    public void create() {

        bomb = new Bomb(interaction.assetManager.get("timer/inter_bomb.png", Texture.class));
        spark = new SparkAnimation(
                interaction.assetManager.get("timer/an_timer_sheet_1x8.png", Texture.class),
                bomb, bomb.getWidth(), interaction.burnDurationInSeconds);
        bar = new Bar(bomb, spark);

        root = new Group();
        root.setTouchable(Touchable.disabled);
        root.addActor(bar);
        root.addActor(spark);
        root.addActor(bomb);

        spark.start(new Runnable() {
            @Override
            public void run() {
                failed();
            }
        });
    }

    public void cancelTimer() {
        stopTimer();

        interaction.isBombCanceled = true;

        root.addAction(Actions.sequence(
                Actions.alpha(0, .3f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        destroy();

                        interaction.imageFragment.bombFragment = null;
                    }
                })
        ));
    }

    public void stopTimer() {
        spark.reset();
    }

    public void failed() {

        Log.i(tag, "failed");

        GameState.pause(tag);

        try {

            root.setTouchable(Touchable.disabled);

            interaction.storyManager.reset();

            interaction.gameScreen.audioService.stop(tag);

            stopTimer();

            bomb.setVisible(false);
            spark.setVisible(false);
            bar.setVisible(false);

            BoomAnimation boom = new BoomAnimation(
                    interaction.assetManager.get("timer/an_bam_sheet_1x7.png", Texture.class),
                    bomb, bomb.getWidth());

            root.addActor(boom);

            boom.start();

            gameScreen.game.sfxService.onTimerBombExploded();

            if (interaction.scenarioFragment != null) {
                interaction.scenarioFragment.fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        if (interaction.scenarioFragment != null) {
                            interaction.scenarioFragment.destroy();
                            interaction.scenarioFragment = null;
                        }
                    }
                });
            }

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    GameState.unpause(tag);

                    try {

                        Log.i(tag, "redirect to " + interaction.burnScenario);

                        Timer2Story newStory = interaction.storyManager.create(interaction.burnScenario);

                        interaction.storyManager.story = newStory;

                        interaction.storyManager.startLoadingResources();

                        interaction.progressBarFragment.fadeIn();
                    } catch (Throwable e) {
                        Log.e(tag, e);

                        interaction.gameScreen.onCriticalError(e);
                    }
                }
            }, 1);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        stopTimer();
    }
}