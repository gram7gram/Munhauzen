package ua.gram.munhauzen.interaction.lions.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.LionsInteraction;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LionsImageFragment extends InteractionFragment {

    private final LionsInteraction interaction;
    public FragmentRoot root;
    BackgroundImage backgroundImage;
    PrimaryButton attackBtn;
    StoryAudio freezeAudio, attackAudio, steadyAudio;

    public LionsImageFragment(LionsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        interaction.gameScreen.hideImageFragment();

        attackBtn = interaction.gameScreen.game.buttonBuilder.primary(interaction.t("lions_inter.attack_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                //dummy
            }
        });

        Table btnTable = new Table();
        btnTable.setFillParent(true);
        btnTable.pad(10);
        btnTable.add(attackBtn).bottom().expand()
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padBottom(80);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
        root.addContainer(btnTable);

        setBackground(
                interaction.assetManager.get("lions/int_lions_fond.jpg", Texture.class),
                "lions/int_lions_fond.jpg"
        );

        freeze();
    }

    private void freeze() {

        attackBtn.setVisible(false);

        stopAllAudio();

        playFreeze();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                stopAllAudio();

                steady();

            }
        }, freezeAudio.duration / 1000f);
    }

    private void steady() {

        playSteady();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                stopAllAudio();

                attack();

            }
        }, new Random().between(3, 15));

        attackBtn.setVisible(true);
        attackBtn.setDisabled(false);
        attackBtn.setTouchable(Touchable.enabled);

        attackBtn.clearListeners();
        attackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                attackBtn.setDisabled(true);
                attackBtn.setTouchable(Touchable.disabled);

                GameState.clearTimer(tag);

                failedEarly();
            }
        });
    }

    private void attack() {

        final float attackDuration = 1f;//сек

        playAttack();

        attackBtn.clearListeners();
        attackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                attackBtn.setVisible(false);

                root.setTouchable(Touchable.disabled);

                GameState.clearTimer(tag);

                try {
                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            complete();
                        }
                    }, attackAudio.duration / 1000f - attackDuration);
                } catch (Throwable ignore) {
                    complete();
                }
            }
        });

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                attackBtn.clearListeners();
                attackBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);

                        attackBtn.setVisible(false);

                        root.setTouchable(Touchable.disabled);

                        GameState.clearTimer(tag);

                        failedLate();
                    }
                });

            }
        }, attackDuration);
    }

    private void playFreeze() {
        try {

            freezeAudio = new StoryAudio();
            freezeAudio.audio = "slions_attack_freeze";

            interaction.gameScreen.audioService.prepareAndPlay(freezeAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    private void playSteady() {
        try {

            steadyAudio = new StoryAudio();
            steadyAudio.audio = "sfx_inter_lions_steady";

            interaction.gameScreen.audioService.prepareAndPlay(steadyAudio);

            steadyAudio.player.setLooping(true);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    private void playAttack() {
        try {

            attackAudio = new StoryAudio();
            attackAudio.audio = "slions_attack_lion";

            interaction.gameScreen.audioService.prepareAndPlay(attackAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void update() {

        if (freezeAudio != null) {
            interaction.gameScreen.audioService.updateVolume(freezeAudio);
        }
        if (attackAudio != null) {
            interaction.gameScreen.audioService.updateVolume(attackAudio);
        }
        if (steadyAudio != null) {
            interaction.gameScreen.audioService.updateVolume(steadyAudio);
        }

    }

    private void complete() {
        Log.i(tag, "complete");

        try {

            Inventory item = InventoryRepository.find(interaction.gameScreen.game.gameState, "ST_LION");

            interaction.gameScreen.onInventoryAdded(item);

            interaction.gameScreen.interactionService.complete();

            Story newStory = interaction.gameScreen.storyManager.create("alions_attack_win");

            interaction.gameScreen.setStory(newStory);

            interaction.gameScreen.restoreProgressBarIfDestroyed();

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

    private void stopAllAudio() {
        if (freezeAudio != null) {
            interaction.gameScreen.audioService.stop(freezeAudio);
            freezeAudio = null;
        }
        if (steadyAudio != null) {
            interaction.gameScreen.audioService.stop(steadyAudio);
            steadyAudio = null;
        }
        if (attackAudio != null) {
            interaction.gameScreen.audioService.stop(attackAudio);
            attackAudio = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        stopAllAudio();
    }
}
