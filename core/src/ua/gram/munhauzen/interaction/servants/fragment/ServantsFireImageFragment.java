package ua.gram.munhauzen.interaction.servants.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.FireDialog;
import ua.gram.munhauzen.interaction.servants.HiredServant;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class ServantsFireImageFragment extends InteractionFragment {

    private final MunhauzenGame game;
    private final ServantsInteraction interaction;
    public BackgroundImage backgroundImage;
    public FragmentRoot root;
    public Group items;
    public Table fireContainer;
    public FireDialog fireDialog;
    public ArrayList<HiredServant> hiredServants;
    final int servantLimit = 5;
    Label progressLabel;
    StoryAudio dismissAudio, backAudio, discardAudio;
    PrimaryButton backBtn, clearBtn;

    public static final String[] names = {
            "GIGANT",
            "USURER",
            "JOKER",
            "JUMPER",
            "RUNNER",
            "VASILIY",
            "LISTENER",
            "SHOOTER",
            "BLOWER",
            "CARPETENER",
    };

    public ServantsFireImageFragment(ServantsInteraction interaction) {
        this.interaction = interaction;
        this.game = interaction.gameScreen.game;

        hiredServants = new ArrayList<>(servantLimit);
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        fireDialog = new FireDialog(interaction);

        backBtn = interaction.gameScreen.game.buttonBuilder.primary(interaction.t("servants_inter.back_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    backBtn.setDisabled(true);

                    root.setTouchable(Touchable.disabled);

                    playBack();

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                interaction.openHireFragment();

                            } catch (Throwable e) {
                                Log.e(tag, e);

                                interaction.gameScreen.onCriticalError(e);
                            }
                        }
                    }, backAudio.duration / 1000f);

                } catch (Throwable e) {
                    Log.e(tag, e);

                    interaction.gameScreen.onCriticalError(e);
                }

            }
        });

        clearBtn = interaction.gameScreen.game.buttonBuilder.danger(interaction.t("servants_inter.discard_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    clearBtn.setDisabled(true);

                    root.setTouchable(Touchable.disabled);

                    playDiscard();

                    hiredServants.clear();
                    items.clearChildren();

                    for (String name : names) {
                        Inventory item = InventoryRepository.find(game.gameState, name);

                        if (game.inventoryService.isInInventory(item)) {
                            game.inventoryService.remove(item);
                        }
                    }

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            try {

                                interaction.openHireFragment();

                            } catch (Throwable e) {
                                Log.e(tag, e);

                                interaction.gameScreen.onCriticalError(e);
                            }
                        }
                    }, discardAudio.duration / 1000f);

                } catch (Throwable e) {
                    Log.e(tag, e);

                    interaction.gameScreen.onCriticalError(e);
                }
            }
        });

        progressLabel = new Label("", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.pad(10);
        uiTable.add(progressLabel).align(Align.topLeft).expandX().row();

        uiTable.add().expand().row();

        uiTable.add(backBtn)
                .left().expandX()
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);

        uiTable.add(clearBtn)
                .right().expandX()
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);

        fireContainer = new Table();

        items = new Group();
        items.setTouchable(Touchable.childrenOnly);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
        root.addContainer(items);
        root.addContainer(fireContainer);
        root.addContainer(uiTable);

        root.setName(tag);

        setBackground(
                interaction.assetManager.get("servants/inter_servants_fond.jpg", Texture.class),
                "servants/inter_servants_fond.jpg"
        );

        showHiredServants();
    }

    public void showHiredServants() {
        try {

            for (String name : names) {
                Inventory item = InventoryRepository.find(game.gameState, name);

                if (game.inventoryService.isInInventory(item)) {

                    final HiredServant hiredServant;
                    Texture on, off;

                    switch (item.name) {
                        case "CARPETENER":

                            interaction.assetManager.load("servants/inter_servants_carpenter_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_carpenter_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_carpenter_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_carpenter_2_off.png", Texture.class);

                            hiredServant = new CarpetenerServant(on, off);
                            hiredServant.init();

                            break;
                        case "BLOWER":

                            interaction.assetManager.load("servants/inter_servants_blower_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_blower_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_blower_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_blower_2_off.png", Texture.class);

                            hiredServant = new BlowerServant(on, off);
                            hiredServant.init();

                            break;
                        case "SHOOTER":

                            interaction.assetManager.load("servants/inter_servants_shooter_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_shooter_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_shooter_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_shooter_2_off.png", Texture.class);

                            hiredServant = new ShooterServant(on, off);
                            hiredServant.init();

                            break;
                        case "LISTENER":

                            interaction.assetManager.load("servants/inter_servants_listener_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_listener_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_listener_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_listener_2_off.png", Texture.class);

                            hiredServant = new ListenerServant(on, off);
                            hiredServant.init();

                            break;
                        case "VASILIY":

                            interaction.assetManager.load("servants/inter_servants_vasiliy_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_vasiliy_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_vasiliy_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_vasiliy_2_off.png", Texture.class);

                            hiredServant = new VasiliyServant(on, off);
                            hiredServant.init();

                            break;
                        case "RUNNER":
                            interaction.assetManager.load("servants/inter_servants_runner_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_runner_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_runner_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_runner_2_off.png", Texture.class);

                            hiredServant = new RunnerServant(on, off);
                            hiredServant.init();

                            break;
                        case "JUMPER":
                            interaction.assetManager.load("servants/inter_servants_jumper_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_jumper_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_jumper_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_jumper_2_off.png", Texture.class);

                            hiredServant = new JumperServant(on, off);
                            hiredServant.init();

                            break;
                        case "JOKER":
                            interaction.assetManager.load("servants/inter_servants_joker_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_joker_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_joker_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_joker_2_off.png", Texture.class);

                            hiredServant = new JokerServant(on, off);
                            hiredServant.init();

                            break;
                        case "USURER":
                            interaction.assetManager.load("servants/inter_servants_usurer_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_usurer_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_usurer_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_usurer_2_off.png", Texture.class);

                            hiredServant = new UsurperServant(on, off);
                            hiredServant.init();

                            break;
                        case "GIGANT":
                            interaction.assetManager.load("servants/inter_servants_gigant_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_gigant_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_gigant_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_gigant_2_off.png", Texture.class);

                            hiredServant = new GigantServant(on, off);
                            hiredServant.init();

                            break;
                        default:
                            continue;
                    }

                    hiredServant.setName(item.name);

                    hiredServants.add(hiredServant);

                }
            }

            items.clearChildren();

            for (HiredServant hiredServant : hiredServants) {
                items.addActor(hiredServant);
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }

    public void toggleFireDialog() {

        if (!fireDialog.isMounted()) return;

        if (!fireDialog.root.isVisible()) {
            fireDialog.fadeIn();
        } else {
            fireDialog.fadeOut();
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        progressLabel.setText(hiredServants.size() + "/" + servantLimit);

        if (dismissAudio != null) {
            interaction.gameScreen.audioService.updateVolume(dismissAudio);
        }

        if (backAudio != null) {
            interaction.gameScreen.audioService.updateVolume(backAudio);
        }

        if (discardAudio != null) {
            interaction.gameScreen.audioService.updateVolume(discardAudio);
        }

        if (fireDialog != null) {
            fireDialog.update();
        }

    }

    public void stopDismiss() {
        if (dismissAudio != null) {
            interaction.gameScreen.audioService.stop(dismissAudio);
            dismissAudio = null;
        }
    }

    public void playDismiss() {
        try {

            stopDismiss();

            dismissAudio = new StoryAudio();
            dismissAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_servants_dismiss_1",
                    "sfx_inter_servants_dismiss_2",
                    "sfx_inter_servants_dismiss_3",
                    "sfx_inter_servants_dismiss_4"
            });

            interaction.gameScreen.audioService.prepareAndPlay(dismissAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            //interaction.gameScreen.onCriticalError(e);
        }
    }

    public void stopBack() {
        if (backAudio != null) {
            interaction.gameScreen.audioService.stop(backAudio);
            backAudio = null;
        }
    }

    public void playBack() {
        try {

            stopBack();

            backAudio = new StoryAudio();
            backAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_servants_back_1",
                    "sfx_inter_servants_back_2",
                    "sfx_inter_servants_back_3",
                    "sfx_inter_servants_back_4"
            });

            interaction.gameScreen.audioService.prepareAndPlay(backAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            //interaction.gameScreen.onCriticalError(e);
        }
    }

    public void stopDiscard() {
        if (discardAudio != null) {
            interaction.gameScreen.audioService.stop(discardAudio);
            discardAudio = null;
        }
    }

    public void playDiscard() {
        try {

            stopDiscard();

            discardAudio = new StoryAudio();
            discardAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_servants_restart_1",
                    "sfx_inter_servants_restart_2"
            });

            interaction.gameScreen.audioService.prepareAndPlay(discardAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            //interaction.gameScreen.onCriticalError(e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (fireDialog != null) {
            fireDialog.destroy();
            fireDialog = null;
        }

        stopDismiss();

        stopBack();

        stopDiscard();
    }

    class CarpetenerServant extends HiredServant {

        public CarpetenerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    15.82f, 24.11f, 39.31f, 69.58f
            };
        }
    }

    class BlowerServant extends HiredServant {

        public BlowerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    9.16f, 22.14f, 4.63f, 64.78f
            };
        }
    }

    class ShooterServant extends HiredServant {

        public ShooterServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    16.07f, 23.47f, 57.27f, 72.46f
            };
        }
    }

    class ListenerServant extends HiredServant {

        public ListenerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    11.12f, 22.83f, 49.18f, 68.76f
            };
        }
    }

    class VasiliyServant extends HiredServant {

        public VasiliyServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    8.52f, 23.15f, 79.94f, 63.27f
            };
        }
    }

    class RunnerServant extends HiredServant {

        public RunnerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    11.15f, 20.59f, 34.25f, 67.38f
            };
        }
    }

    class JumperServant extends HiredServant {

        public JumperServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    12.87f, 28.13f, 28.19f, 61.12f
            };
        }
    }

    class JokerServant extends HiredServant {

        public JokerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    6.13f, 12.08f, 20.49f, 63.86f
            };
        }
    }

    class UsurperServant extends HiredServant {

        public UsurperServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    10.16f, 20.49f, 56.02f, 66.56f
            };
        }
    }

    class GigantServant extends HiredServant {

        public GigantServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public float[] getPercentBounds() {
            return new float[]{
                    10.58f, 27.49f, 50.5f, 59.52f
            };
        }
    }
}
