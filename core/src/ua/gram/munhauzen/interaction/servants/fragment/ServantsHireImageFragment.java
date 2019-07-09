package ua.gram.munhauzen.interaction.servants.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.CompleteDialog;
import ua.gram.munhauzen.interaction.servants.HireDialog;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ServantsHireImageFragment extends Fragment {

    private final ServantsInteraction interaction;
    public Stack root;
    Image background;
    Table backgroundTable;
    PrimaryButton servantsBtn;
    int page;
    public int servantCount;
    public int servantLimit = 5;
    ImageButton prevBtn, nextBtn;
    Table hireContainer;
    Label progressLabel;
    HireDialog hireDialog;
    CompleteDialog completeDialog;

    public ServantsHireImageFragment(ServantsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        interaction.gameScreen.hideImageFragment();

        servantsBtn = interaction.gameScreen.game.buttonBuilder.primary("Servants", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                interaction.openFireFragment();
            }
        });

        PrimaryButton departBtn = interaction.gameScreen.game.buttonBuilder.danger("Depart", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                showEgypt();
            }
        });

        progressLabel = new Label("", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        prevBtn = getPrev();
        nextBtn = getNext();

        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                next();
            }
        });

        prevBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                prev();
            }
        });

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).bottom().expand();

        hireContainer = new Table();

        Table btnTable = new Table();
        btnTable.add(servantsBtn).padRight(10).left()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);
        btnTable.add(progressLabel).left().padRight(10);
        btnTable.add(departBtn).left()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);

        Table hireTable = new Table();
        hireTable.setFillParent(true);
        hireTable.pad(10);
        hireTable.add(prevBtn).left();
        hireTable.add().center().grow();
        hireTable.add(nextBtn).right();

        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.pad(10);
        uiTable.add(btnTable).align(Align.topLeft).expand();

        hireDialog = new HireDialog(interaction);
        completeDialog = new CompleteDialog(interaction);

        root = new Stack();
        root.setFillParent(true);
        root.addActor(backgroundTable);
        root.addActor(hireTable);
        root.addActor(uiTable);
        root.addActor(hireContainer);

        root.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                toggleHireDialog();
            }
        });

        start();
    }

    private void toggleHireDialog() {
        if (completeDialog.isMounted()) return;

        if (!hireDialog.isMounted()) return;

        if (!hireDialog.root.isVisible()) {
            hireDialog.fadeIn();
        } else {
            hireDialog.fadeOut();
        }
    }

    public void start() {

        showServant1();

    }

    public void next() {
        switch (page) {
            case 1:
                showServant2();
                break;
            case 2:
                showServant3();
                break;
            case 3:
                showServant4();
                break;
            case 4:
                showServant5();
                break;
            case 5:
                showServant6();
                break;
            case 6:
                showServant7();
                break;
            case 7:
                showServant8();
                break;
            case 8:
                showServant9();
                break;
            case 9:
                showServant10();
                break;
            case 10:
                showEgypt();
                break;
        }
    }

    public void prev() {
        switch (page) {
            case 2:
                showServant1();
                break;
            case 3:
                showServant2();
                break;
            case 4:
                showServant3();
                break;
            case 5:
                showServant4();
                break;
            case 6:
                showServant5();
                break;
            case 7:
                showServant6();
                break;
            case 8:
                showServant7();
                break;
            case 9:
                showServant8();
                break;
            case 10:
                showServant9();
                break;
            case 11:
                showServant10();
                break;
        }
    }

    public boolean hasServant(String name) {
        try {
            Inventory item = InventoryRepository.find(interaction.gameScreen.game.gameState, name);

            return interaction.gameScreen.game.inventoryService.isInInventory(item);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return false;
    }

    private void showServant1() {

        unload();

        page = 1;

        boolean hasServant = hasServant("CARPETENER");

        String res = hasServant ? "servants/inter_servants_carpenter_0.jpg" : "servants/inter_servants_carpenter_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("CARPETENER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());
    }

    private void showServant2() {

        unload();

        page = 2;

        boolean hasServant = hasServant("BLOWER");

        String res = hasServant ? "servants/inter_servants_blower_0.jpg" : "servants/inter_servants_blower_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("BLOWER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());
    }

    private void showServant3() {

        unload();

        page = 3;

        boolean hasServant = hasServant("SHOOTER");

        String res = hasServant ? "servants/inter_servants_shooter_0.jpg" : "servants/inter_servants_shooter_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("SHOOTER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showServant4() {

        unload();

        page = 4;

        boolean hasServant = hasServant("LISTENER");

        String res = hasServant ? "servants/inter_servants_listener_0.jpg" : "servants/inter_servants_listener_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("LISTENER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showServant5() {

        unload();

        page = 5;

        boolean hasServant = hasServant("VASILIY");

        String res = hasServant ? "servants/inter_servants_vasiliy_0.jpg" : "servants/inter_servants_vasiliy_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("VASILIY");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showServant6() {

        unload();

        page = 6;

        boolean hasServant = hasServant("RUNNER");

        String res = hasServant ? "servants/inter_servants_runner_0.jpg" : "servants/inter_servants_runner_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("RUNNER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showServant7() {

        unload();

        page = 7;

        boolean hasServant = hasServant("JUMPER");

        String res = hasServant ? "servants/inter_servants_jumper_0.jpg" : "servants/inter_servants_jumper_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );


        hireDialog.create("JUMPER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());
    }

    private void showServant8() {

        unload();

        page = 8;

        boolean hasServant = hasServant("JOKER");

        String res = hasServant ? "servants/inter_servants_joker_0.jpg" : "servants/inter_servants_joker_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("JOKER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showServant9() {

        unload();

        page = 9;

        boolean hasServant = hasServant("USURER");

        String res = hasServant ? "servants/inter_servants_usurer_0.jpg" : "servants/inter_servants_usurer_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("USURER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showServant10() {

        unload();

        page = 10;

        boolean hasServant = hasServant("GIGANT");

        String res = hasServant ? "servants/inter_servants_gigant_0.jpg" : "servants/inter_servants_gigant_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class)
        );

        hireDialog.create("GIGANT");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

    }

    private void showEgypt() {

        page = 11;

        completeDialog.create();

        hireContainer.clearChildren();
        hireContainer.add(completeDialog.getRoot());

        completeDialog.fadeIn();

    }

    public void update() {

        servantsBtn.setTouchable(Touchable.enabled); //always touchable

        prevBtn.setDisabled(page == 1);
        nextBtn.setDisabled(page == 11);

        progressLabel.setText(servantCount + "/" + servantLimit);
    }

    private void unload() {

        String[] resources = {
                "servants/inter_servants_carpenter_0.jpg", "servants/inter_servants_carpenter_1.jpg",
                "servants/inter_servants_blower_0.jpg", "servants/inter_servants_blower_1.jpg",
                "servants/inter_servants_shooter_0.jpg", "servants/inter_servants_shooter_1.jpg",
                "servants/inter_servants_listener_0.jpg", "servants/inter_servants_listener_1.jpg",
                "servants/inter_servants_vasiliy_0.jpg", "servants/inter_servants_vasiliy_1.jpg",
                "servants/inter_servants_runner_0.jpg", "servants/inter_servants_runner_1.jpg",
                "servants/inter_servants_jumper_0.jpg", "servants/inter_servants_jumper_1.jpg",
                "servants/inter_servants_joker_0.jpg", "servants/inter_servants_joker_1.jpg",
                "servants/inter_servants_usurer_0.jpg", "servants/inter_servants_usurer_1.jpg",
                "servants/inter_servants_gigant_0.jpg", "servants/inter_servants_gigant_1.jpg",
        };

        for (String resource : resources) {
            if (interaction.assetManager.isLoaded(resource)) {
                interaction.assetManager.unload(resource);
            }
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture) {

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        background.clearListeners();

        if (background.getDrawable().getMinWidth() > background.getDrawable().getMinHeight()) {

            float height = MunhauzenGame.WORLD_HEIGHT;
            float scale = 1f * height / background.getDrawable().getMinHeight();
            final float width = 1f * background.getDrawable().getMinWidth() * scale;

            background.addListener(new ActorGestureListener() {

                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);

                    toggleHireDialog();
                }

                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    super.pan(event, x, y, deltaX, deltaY);

                    Log.i(tag, "pan");

                    try {
                        float newX = background.getX() + deltaX;

                        float leftBound = -width + MunhauzenGame.WORLD_WIDTH;
                        float rightBound = 0;

                        if (leftBound < newX && newX < rightBound) {
                            background.setX(background.getX() + deltaX);
                        }

                        if (background.getX() > 0) background.setX(0);
                        if (background.getX() < leftBound) background.setX(leftBound);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                }
            });

            backgroundTable.getCell(background)
                    .width(width)
                    .height(height);

        } else {

            float width = MunhauzenGame.WORLD_WIDTH;
            float scale = 1f * width / background.getDrawable().getMinWidth();
            float height = 1f * background.getDrawable().getMinHeight() * scale;

            backgroundTable.getCell(background)
                    .width(width)
                    .height(height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        page = 1;
        servantCount = 0;

        if (hireDialog != null) {
            hireDialog.destroy();
            hireDialog = null;
        }
        if (completeDialog != null) {
            completeDialog.destroy();
            completeDialog = null;
        }

    }

    private ImageButton getPrev() {
        Texture skipBack = interaction.assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = interaction.assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }


    private ImageButton getNext() {
        Texture skipBack = interaction.assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipBackOff = interaction.assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }
}
