package ua.gram.munhauzen.interaction.servants.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.ServantsState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.CompleteDialog;
import ua.gram.munhauzen.interaction.servants.HireDialog;
import ua.gram.munhauzen.interaction.servants.ServantImage;
import ua.gram.munhauzen.interaction.servants.hire.HireStory;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.ui.ProgressIconButton;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class ServantsHireImageFragment extends InteractionFragment {

    private final ServantsInteraction interaction;
    final ServantsState state;
    public FragmentRoot root;
    PrimaryButton servantsBtn;
    public int servantCount;
    public final int servantLimit = 5;
    ImageButton prevBtn, nextBtn;
    Table hireContainer;
    Label progressLabel;
    public HireDialog hireDialog;
    CompleteDialog completeDialog;
    public ServantImage backgroundImage;
    StoryAudio egyptAudio;

    public ServantsHireImageFragment(ServantsInteraction interaction, ServantsState state) {
        this.interaction = interaction;
        this.state = state;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new ServantImage(interaction.gameScreen);

        interaction.gameScreen.hideImageFragment();

        servantsBtn = interaction.gameScreen.game.buttonBuilder.primary(interaction.t("servants_inter.goto_servants_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                servantsBtn.setDisabled(true);

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

        hireContainer = new Table();

        Table btnTable = new Table();
        btnTable.add(servantsBtn).padRight(10).left()
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);
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

    public void updateServantCount() {
        servantCount = 0;
        for (String name : ServantsFireImageFragment.names) {
            if (hasServant(name)) {
                servantCount += 1;
            }
        }
    }

    public void start() {

        updateServantCount();

        showCurrent();
    }

    public void showCurrent() {

        try {
            Log.i(tag, "showCurrent " + state.hirePage);

            HireStory story = interaction.storyManager.story;
            if (story != null)
                interaction.gameScreen.audioService.dispose(story);

            stopEgypt();

            switch (state.hirePage) {
                case 1:
                    showServant1();
                    break;
                case 2:
                    showServant2();
                    break;
                case 3:
                    showServant3();
                    break;
                case 4:
                    showServant4();
                    break;
                case 5:
                    showServant5();
                    break;
                case 6:
                    showServant6();
                    break;
                case 7:
                    showServant7();
                    break;
                case 8:
                    showServant8();
                    break;
                case 9:
                    showServant9();
                    break;
                case 10:
                    showServant10();
                    break;
                case 11:
                    showEgypt();
                    break;
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void next() {

        state.hirePage += 1;

        hireDialog.destroy();

        showCurrent();
    }

    public void prev() {

        state.hirePage -= 1;

        hireDialog.destroy();

        showCurrent();
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

    private void showServant10() {

        showServant("CARPETENER");

    }

    private void showServant7() {

        showServant("BLOWER");
    }

    private void showServant5() {

        showServant("SHOOTER");

    }

    private void showServant2() {
        showServant("LISTENER");

    }

    private void showServant3() {

        showServant("VASILIY");

    }

    private void showServant1() {

        showServant("RUNNER");
    }

    private void showServant9() {
        showServant("JUMPER");
    }

    private void showServant6() {

        showServant("JOKER");

    }

    private void showServant4() {

        showServant("USURER");

    }

    private void showServant8() {

        showServant("GIGANT");

    }

    private void showServant(String name) {

        Log.i(tag, "showServant " + name);

        GameState.unpause(tag);

        interaction.storyManager.story = interaction.storyManager.create(name);

        hireDialog.create(name);

        interaction.storyManager.startLoadingResources();

        hireContainer.clearChildren();
        hireContainer.add(hireDialog.getRoot());

        interaction.progressBarFragment.fadeIn();

    }

    private void showEgypt() {

        interaction.progressBarFragment.root.setVisible(false);

        interaction.storyManager.reset();
        interaction.storyManager.story = null;

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

        playEgypt();
    }

    public void update() {

        servantsBtn.setTouchable(Touchable.enabled); //always touchable

        ServantsState state = interaction.gameScreen.getActiveSave().servantsInteractionState;

        boolean isVisited = hireDialog != null && state.viewedServants.contains(hireDialog.servantName);

        prevBtn.setDisabled(state.hirePage == 1);
        nextBtn.setDisabled(false);

        if (!isVisited) {
            nextBtn.setDisabled(true);
        }

        if (hireDialog != null && hasServant(hireDialog.servantName)) {
            nextBtn.setDisabled(false);
        }

        prevBtn.setVisible(state.hirePage != 1);
        nextBtn.setVisible(state.hirePage != 11);

        progressLabel.setText(servantCount + "/" + servantLimit);

        if (hireDialog != null) {
            hireDialog.update();
        }

        if (egyptAudio != null) {
            interaction.gameScreen.audioService.updateVolume(egyptAudio);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture, String file) {
        setBackground(new SpriteDrawable(new Sprite(texture)), file);
    }

    public void setBackground(SpriteDrawable texture, String file) {

        interaction.gameScreen.hideImageFragment();

        backgroundImage.setBackgroundDrawable(texture);

        interaction.gameScreen.setLastBackground(file);
    }

    @Override
    public void dispose() {
        super.dispose();

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
        Texture img = interaction.assetManager.get("ui/playbar_skip_backward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(img));
        style.down = new SpriteDrawable(new Sprite(img));
        style.disabled = new SpriteDrawable(new Sprite(img));

        ImageButton btn = new ProgressIconButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                prev();
            }
        });

        return btn;
    }


    private ImageButton getNext() {
        Texture img = interaction.assetManager.get("ui/playbar_skip_forward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(img));
        style.down = new SpriteDrawable(new Sprite(img));
        style.disabled = new SpriteDrawable(new Sprite(img));

        ImageButton btn = new ProgressIconButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                next();
            }
        });

        return btn;
    }
}
