package ua.gram.munhauzen.interaction.lions.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.LionsInteraction;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.ui.BackgroundImage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LionsImageFragment extends Fragment {

    private final LionsInteraction interaction;
    public Stack root;
    BackgroundImage backgroundImage;
    PrimaryButton attackBtn;
    StoryAudio steadyAudio, goAudio, sleepAudio;
    Timer.Task sleepTask, delayedTask, readyTask;

    public LionsImageFragment(LionsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        interaction.gameScreen.hideImageFragment();

        attackBtn = interaction.gameScreen.game.buttonBuilder.primary("Attack...", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });

        Table btnTable = new Table();
        btnTable.setFillParent(true);
        btnTable.pad(10);
        btnTable.add(attackBtn).bottom().expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f)
                .padBottom(80);

        root = new Stack();
        root.setFillParent(true);
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(backgroundImage);
        root.addActor(btnTable);

        setBackground(
                interaction.assetManager.get("lions/int_lions_fond.jpg", Texture.class),
                "lions/int_lions_fond.jpg"
        );

        start();
    }

    private void start() {

        attackBtn.clearListeners();
        attackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                attackBtn.setDisabled(true);

                failedEarly();
            }
        });

        playSteady();

        delayedTask = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                stopSteady();

                playGo();

                readyTask = Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {

                        attackBtn.setText("Attack now!");

                        attackBtn.clearListeners();
                        attackBtn.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                super.clicked(event, x, y);

                                attackBtn.setDisabled(true);

                                if (sleepTask != null) {
                                    sleepTask.cancel();
                                    sleepTask = null;
                                }

                                complete();
                            }
                        });

                        sleepTask = Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {

                                playSleep();

                            }
                        }, 5);

                        Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {

                                attackBtn.setText("Attack");

                                attackBtn.clearListeners();
                                attackBtn.addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        super.clicked(event, x, y);

                                        attackBtn.setDisabled(true);

                                        failedLate();
                                    }
                                });

                            }
                        }, .4f);

                    }
                }, goAudio.duration / 1000f);

            }
        }, new Random().between(3, 15));
    }

    private void playSteady() {
        try {

            steadyAudio = new StoryAudio();
            steadyAudio.audio = "alions_steady";

            interaction.gameScreen.audioService.prepareAndPlay(steadyAudio);

            steadyAudio.player.setLooping(true);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);

            steadyAudio.duration = 1000;
        }

    }

    private void playSleep() {
        try {

            sleepAudio = new StoryAudio();
            sleepAudio.audio = "alions_sleep";

            interaction.gameScreen.audioService.prepareAndPlay(sleepAudio);

            sleepAudio.player.setLooping(true);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);

            sleepAudio.duration = 1000;
        }

    }

    private void playGo() {
        try {

            goAudio = new StoryAudio();
            goAudio.audio = MathUtils.random(new String[]{
                    "alions_attacklion_1",
                    "alions_attacklion_2",
                    "alions_attacklion_3",
                    "alions_attacklion_4",
                    "alions_attacklion_5"
            });

            interaction.gameScreen.audioService.prepareAndPlay(goAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);

            goAudio.duration = 1000;
        }

    }

    public void update() {

        attackBtn.setTouchable(Touchable.enabled); //always touchable

        if (steadyAudio != null) {
            interaction.gameScreen.audioService.updateVolume(steadyAudio);
        }
        if (goAudio != null) {
            interaction.gameScreen.audioService.updateVolume(goAudio);
        }

    }

    private void complete() {
        Log.i(tag, "complete");

        try {

            interaction.gameScreen.interactionService.complete();

            Story newStory = interaction.gameScreen.storyManager.create("alions_attack_win");

            interaction.gameScreen.setStory(newStory);

            interaction.gameScreen.restoreProgressBarIfDestroyed();

            Inventory item = InventoryRepository.find(interaction.gameScreen.game.gameState, "ST_LION");

            interaction.gameScreen.game.inventoryService.addInventory(item);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void failedEarly() {
        Log.i(tag, "failedEarly");

        try {

            interaction.gameScreen.interactionService.complete();

            Story newStory = interaction.gameScreen.storyManager.create("alions_attack_early");

            interaction.gameScreen.setStory(newStory);

            interaction.gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private void failedLate() {
        Log.i(tag, "failedLate");

        try {

            interaction.gameScreen.interactionService.complete();

            Story newStory = interaction.gameScreen.storyManager.create("alions_attack_late");

            interaction.gameScreen.setStory(newStory);

            interaction.gameScreen.restoreProgressBarIfDestroyed();

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

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }

    private void stopSteady() {
        if (steadyAudio != null) {
            interaction.gameScreen.audioService.stop(steadyAudio);
            steadyAudio = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        stopSteady();

        if (goAudio != null) {
            interaction.gameScreen.audioService.stop(goAudio);
            goAudio = null;
        }

        if (sleepAudio != null) {
            interaction.gameScreen.audioService.stop(sleepAudio);
            sleepAudio = null;
        }

        if (sleepTask != null) {
            sleepTask.cancel();
            sleepTask = null;
        }

        if (delayedTask != null) {
            delayedTask.cancel();
            delayedTask = null;
        }

        if (readyTask != null) {
            readyTask.cancel();
            readyTask = null;
        }

    }
}
