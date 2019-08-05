package ua.gram.munhauzen.interaction.servants.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.ServantsState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.CompleteDialog;
import ua.gram.munhauzen.interaction.servants.HireDialog;
import ua.gram.munhauzen.interaction.servants.ServantImage;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class ServantsHireImageFragment extends InteractionFragment {

    private final ServantsInteraction interaction;
    public FragmentRoot root;
    PrimaryButton servantsBtn;
    public int page;
    public int servantCount;
    public final int servantLimit = 5;
    ImageButton prevBtn, nextBtn;
    Table hireContainer;
    Label progressLabel;
    HireDialog hireDialog;
    CompleteDialog completeDialog;
    public ServantImage backgroundImage;
    StoryAudio egyptAudio;

    public ServantsHireImageFragment(ServantsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new ServantImage(interaction.gameScreen);

        interaction.gameScreen.hideImageFragment();

        servantsBtn = interaction.gameScreen.game.buttonBuilder.primary("Servants", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    interaction.openFireFragment();
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

        hireContainer = new Table();

        Table btnTable = new Table();
        btnTable.add(servantsBtn).padRight(10).left()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);
        btnTable.add(progressLabel).left().padRight(10);

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

        root = new FragmentRoot();
        root.addContainer(backgroundImage);
        root.addContainer(hireTable);
        root.addContainer(uiTable);
        root.addContainer(hireContainer);

        start();
    }

    public void toggleHireDialog() {
        if (completeDialog.isMounted()) return;

        if (!hireDialog.isMounted()) return;

        if (!hireDialog.root.isVisible()) {
            hireDialog.fadeIn();
        } else {
            hireDialog.fadeOut();
        }
    }

    public void updateServantCount() {
        servantCount = 0;
        for (String name : ServantsFireImageFragment.names) {
            if (hasServant(name)) {
                servantCount += 1;
            }
        }
    }

    public void start() {

        page = 1;

        updateServantCount();

        showCurrent(true);
    }

    public void showCurrent(boolean withAudio) {

        try {
            Log.i(tag, "showCurrent " + page);

            interaction.progressBarFragment.stop();

            stopEgypt();

            switch (page) {
                case 1:
                    showServant1(withAudio);
                    break;
                case 2:
                    showServant2(withAudio);
                    break;
                case 3:
                    showServant3(withAudio);
                    break;
                case 4:
                    showServant4(withAudio);
                    break;
                case 5:
                    showServant5(withAudio);
                    break;
                case 6:
                    showServant6(withAudio);
                    break;
                case 7:
                    showServant7(withAudio);
                    break;
                case 8:
                    showServant8(withAudio);
                    break;
                case 9:
                    showServant9(withAudio);
                    break;
                case 10:
                    showServant10(withAudio);
                    break;
                case 11:
                    showEgypt(withAudio);
                    break;
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void next() {

        page += 1;

        hireDialog.destroy();

        showCurrent(true);
    }

    public void prev() {

        page -= 1;

        hireDialog.destroy();

        showCurrent(true);
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

    private void showServant10(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("CARPETENER");

        String res = hasServant ? "servants/inter_servants_carpenter_0.jpg" : "servants/inter_servants_carpenter_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("CARPETENER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_8");
    }

    private void showServant7(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("BLOWER");

        String res = hasServant ? "servants/inter_servants_blower_0.jpg" : "servants/inter_servants_blower_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("BLOWER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_6");
    }

    private void showServant5(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("SHOOTER");

        String res = hasServant ? "servants/inter_servants_shooter_0.jpg" : "servants/inter_servants_shooter_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("SHOOTER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_4");

    }

    private void showServant2(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("LISTENER");

        String res = hasServant ? "servants/inter_servants_listener_0.jpg" : "servants/inter_servants_listener_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("LISTENER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_3");

    }

    private void showServant3(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("VASILIY");

        String res = hasServant ? "servants/inter_servants_vasiliy_0.jpg" : "servants/inter_servants_vasiliy_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("VASILIY");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_11");

    }

    private void showServant1(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("RUNNER");

        String res = hasServant ? "servants/inter_servants_runner_0.jpg" : "servants/inter_servants_runner_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("RUNNER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_2");

    }

    private void showServant9(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("JUMPER");

        String res = hasServant ? "servants/inter_servants_jumper_0.jpg" : "servants/inter_servants_jumper_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );


        hireDialog.create("JUMPER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_7");
    }

    private void showServant6(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("JOKER");

        String res = hasServant ? "servants/inter_servants_joker_0.jpg" : "servants/inter_servants_joker_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("JOKER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_9");

    }

    private void showServant4(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("USURER");

        String res = hasServant ? "servants/inter_servants_usurer_0.jpg" : "servants/inter_servants_usurer_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("USURER");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_10");

    }

    private void showServant8(boolean withAudio) {

        unload();

        boolean hasServant = hasServant("GIGANT");

        String res = hasServant ? "servants/inter_servants_gigant_0.jpg" : "servants/inter_servants_gigant_1.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        hireDialog.create("GIGANT");

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        if (withAudio && !hasServant) interaction.progressBarFragment.play("s41_5");

    }

    private void showEgypt(boolean withAudio) {

        unload();

        String res = "images/p41_egypt.jpg";

        interaction.assetManager.load(res, Texture.class);

        interaction.assetManager.finishLoading();

        setBackground(
                interaction.assetManager.get(res, Texture.class),
                res
        );

        completeDialog.create();

        hireContainer.clearChildren();
        hireContainer.add(completeDialog.getRoot());

        completeDialog.fadeIn();

        interaction.progressBarFragment.fadeOut();

        if (withAudio) playEgypt();
    }

    public void update() {

        servantsBtn.setTouchable(Touchable.enabled); //always touchable

        ServantsState state = interaction.gameScreen.getActiveSave().servantsInteractionState;

        boolean isVisited = hireDialog != null && state.viewedServants.contains(hireDialog.servantName);

        prevBtn.setDisabled(page == 1);
        nextBtn.setDisabled(false);

        if (!isVisited) {
            nextBtn.setDisabled(true);
        }

        if (hireDialog != null && hasServant(hireDialog.servantName)) {
            nextBtn.setDisabled(false);
        }

        prevBtn.setVisible(page != 1);
        nextBtn.setVisible(page != 11);

        progressLabel.setText(servantCount + "/" + servantLimit);

        if (hireDialog != null) {
            hireDialog.update();
        }

        if (egyptAudio != null) {
            interaction.gameScreen.audioService.updateVolume(egyptAudio);
        }
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
                "images/p41_egypt.jpg"
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

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }

    @Override
    public void dispose() {
        super.dispose();

        page = 1;
        servantCount = 0;

        stopEgypt();

        hireDialog.destroy();

        completeDialog.destroy();

    }

    private void stopEgypt() {
        if (egyptAudio != null) {
            interaction.gameScreen.audioService.stop(egyptAudio);
            egyptAudio = null;
        }
    }

    private void playEgypt() {
        try {

            egyptAudio = new StoryAudio();
            egyptAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_servants_start_1",
                    "sfx_inter_servants_start_2",
                    "sfx_inter_servants_start_3",
                    "sfx_inter_servants_start_4"
            });

            interaction.gameScreen.audioService.prepareAndPlay(egyptAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    private ImageButton getPrev() {
        Texture skipBack = interaction.assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = interaction.assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        final ImageButton btn = new ImageButton(style);

        btn.addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (btn.isDisabled()) {
                    event.cancel();
                    return true;
                }
                return false;
            }
        });

        return btn;
    }


    private ImageButton getNext() {
        Texture skipBack = interaction.assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipBackOff = interaction.assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        final ImageButton btn = new ImageButton(style);

        btn.addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (btn.isDisabled()) {
                    event.cancel();
                    return true;
                }
                return false;
            }
        });

        return btn;
    }
}
