package ua.gram.munhauzen.interaction.servants.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.FireDialog;
import ua.gram.munhauzen.interaction.servants.HiredServant;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.ui.BackgroundImage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ServantsFireImageFragment extends Fragment {

    private final MunhauzenGame game;
    private final ServantsInteraction interaction;
    public BackgroundImage backgroundImage;
    public Stack root;
    public Group items;
    public Table fireContainer;
    public FireDialog fireDialog;
    public ArrayList<HiredServant> hiredServants;
    final int servantLimit = 5;
    Label progressLabel;
    StoryAudio fireAudio;


    final String[] names = {
            "CARPETENER",
            "BLOWER",
            "SHOOTER",
            "LISTENER",
            "VASILIY",
            "RUNNER",
            "JUMPER",
            "JOKER",
            "USURER",
            "GIGANT",
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

        PrimaryButton backBtn = interaction.gameScreen.game.buttonBuilder.primary("Back", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    interaction.openHireFragment();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        PrimaryButton clearBtn = interaction.gameScreen.game.buttonBuilder.danger("Discard", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    for (String name : names) {
                        try {
                            Inventory item = InventoryRepository.find(game.gameState, name);

                            if (game.inventoryService.isInInventory(item)) {
                                game.inventoryService.remove(item);
                            }
                        } catch (Throwable e) {
                            Log.e(tag, e);
                        }
                    }

                    interaction.openHireFragment();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        progressLabel = new Label("", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        final Table btnTable = new Table();

        btnTable.add(backBtn).padRight(10).left()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);

        btnTable.add(progressLabel).left().padRight(10);

        btnTable.add(clearBtn).left()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);


        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.pad(10);
        uiTable.add(btnTable).align(Align.topLeft).expand();

        fireContainer = new Table();

        items = new Group();

        root = new Stack();
        root.setFillParent(true);
        root.addActor(backgroundImage);
        root.addActor(items);
        root.addActor(fireContainer);
        root.addActor(uiTable);

        root.setName(tag);

        setBackground(
                interaction.assetManager.get("servants/inter_servants_fond.jpg", Texture.class),
                "servants/inter_servants_fond.jpg"
        );

        showHiredServants();
    }

    public void showHiredServants() {

        for (String name : names) {
            try {
                Inventory item = InventoryRepository.find(game.gameState, name);

                if (game.inventoryService.isInInventory(item)) {

                    final HiredServant hiredServant;
                    Texture on, off;

                    switch (name) {
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
                            interaction.assetManager.load("servants/inter_servants_usurper_2.png", Texture.class);
                            interaction.assetManager.load("servants/inter_servants_usurper_2_off.png", Texture.class);

                            interaction.assetManager.finishLoading();

                            on = interaction.assetManager.get("servants/inter_servants_usurper_2.png", Texture.class);
                            off = interaction.assetManager.get("servants/inter_servants_usurper_2_off.png", Texture.class);

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
            } catch (Throwable e) {
                Log.e(tag, e);
            }
        }

        items.clearChildren();

        for (HiredServant hiredServant : hiredServants) {
            items.addActor(hiredServant);
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

        if (fireAudio != null) {
            interaction.gameScreen.audioService.updateVolume(fireAudio);
        }

    }

    public void stopFired() {
        if (fireAudio != null) {
            interaction.gameScreen.audioService.stop(fireAudio);
            fireAudio = null;
        }
    }

    public void playFired() {
        try {

            stopFired();

            fireAudio = new StoryAudio();
            fireAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_servants_fire_1",
                    "sfx_inter_servants_fire_2",
                    "sfx_inter_servants_fire_3",
                    "sfx_inter_servants_fire_4",
                    "sfx_inter_servants_fire_4"
            });

            interaction.gameScreen.audioService.prepareAndPlay(fireAudio);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (fireDialog != null) {
            fireDialog.destroy();
            fireDialog = null;
        }

        stopFired();
    }

    class CarpetenerServant extends HiredServant {

        public CarpetenerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (30 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (100 / 1200f));
        }
    }

    class BlowerServant extends HiredServant {

        public BlowerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (1100 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (80 / 1200f));
        }
    }

    class ShooterServant extends HiredServant {

        public ShooterServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (910 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (100 / 1200f));
        }
    }

    class ListenerServant extends HiredServant {

        public ListenerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (660 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (100 / 1200f));
        }
    }

    class VasiliyServant extends HiredServant {

        public VasiliyServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (600 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (100 / 1200f));
        }
    }

    class RunnerServant extends HiredServant {

        public RunnerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (150 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (100 / 1200f));
        }
    }

    class JumperServant extends HiredServant {

        public JumperServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (300 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (200 / 1200f));
        }
    }

    class JokerServant extends HiredServant {

        public JokerServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (400 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (100 / 1200f));
        }
    }

    class UsurperServant extends HiredServant {

        public UsurperServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (1200 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (50 / 1200f));
        }
    }

    class GigantServant extends HiredServant {

        public GigantServant(Texture on, Texture off) {
            super(on, off, ServantsFireImageFragment.this);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            setPosition(backgroundImage.background.getX() + backgroundImage.backgroundWidth * (1000 / 1520f),
                    backgroundImage.background.getY() + backgroundImage.backgroundHeight * (80 / 1200f));
        }
    }
}
