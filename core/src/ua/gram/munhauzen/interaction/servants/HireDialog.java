package ua.gram.munhauzen.interaction.servants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.ServantsState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryImage;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.ui.WrapLabel;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HireDialog extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final ServantsInteraction interaction;
    private final MunhauzenGame game;
    public final GameScreen gameScreen;
    public Table root;
    private final float headerSize, buttonSize;
    PrimaryButton yesBtn, noBtn;
    StoryAudio muchAudio, confirmAudio, cancelAudio;
    public String servantName;
    public boolean isFadeIn, isDecisionActive;
    Timer.Task nextTask;

    public HireDialog(ServantsInteraction interaction) {
        this.game = interaction.gameScreen.game;
        this.gameScreen = interaction.gameScreen;
        this.interaction = interaction;

        headerSize = MunhauzenGame.WORLD_HEIGHT / 20f;
        buttonSize = MunhauzenGame.WORLD_WIDTH * 3 / 4f;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create(String name) {

        if (name.equals(this.servantName)) return;

        isDecisionActive = false;
        this.servantName = name;

        Log.i(tag, "create " + servantName);

        yesBtn = game.buttonBuilder.danger(game.t("servants_inter.hire_banner_yes_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    Log.i(tag, "yes clicked");

                    yesBtn.setDisabled(true);
                    root.setTouchable(Touchable.disabled);

                    isDecisionActive = true;

                    ServantsState state = interaction.gameScreen.getActiveSave().servantsInteractionState;

                    state.viewedServants.add(servantName);

                    stopAllAudio();

                    gameScreen.audioService.stop(tag);

                    boolean hasServant = interaction.hireFragment.hasServant(servantName);

                    if (interaction.isLimitReached() && !hasServant) {

                        root.setTouchable(Touchable.enabled);
                        playToMuch();

                    } else {

                        playConfirm();

                        Inventory item = InventoryRepository.find(interaction.gameScreen.game.gameState, servantName);

                        interaction.gameScreen.onInventoryAdded(item);

                        interaction.hireFragment.updateServantCount();

                        fadeOut();

                        triggerBackgroundUpdate();

                        nextTask = Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                destroy();

                                interaction.hireFragment.next();
                            }
                        }, confirmAudio.duration / 1000f);
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                    interaction.gameScreen.onCriticalError(e);
                }
            }
        });

        noBtn = game.buttonBuilder.danger(game.t("servants_inter.hire_banner_no_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    Log.i(tag, "no clicked");

                    noBtn.setDisabled(true);
                    root.setTouchable(Touchable.disabled);

                    isDecisionActive = true;

                    ServantsState state = interaction.gameScreen.getActiveSave().servantsInteractionState;

                    state.viewedServants.add(servantName);

                    stopAllAudio();

                    gameScreen.audioService.stop(tag);

                    playCancel();

                    Inventory item = InventoryRepository.find(interaction.gameScreen.game.gameState, servantName);

                    interaction.gameScreen.game.inventoryService.remove(item);

                    interaction.hireFragment.updateServantCount();

                    fadeOut();

                    triggerBackgroundUpdate();

                    nextTask = Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {

                            destroy();

                            interaction.hireFragment.next();
                        }
                    }, cancelAudio.duration / 1000f);

                } catch (Throwable e) {
                    Log.e(tag, e);

                    interaction.gameScreen.onCriticalError(e);
                }
            }
        });

        Actor button = primaryDecision(game.t("servants_inter.hire_banner_title"), buttonSize);

        Table table = new Table();
        table.add(yesBtn).left().expandX().padRight(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);
        table.add(noBtn).right().expandX().padLeft(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);

        root = new Table();
        root.add(button).width(buttonSize)
                .padBottom(10).row();
        root.add(table).width(buttonSize);

        root.setName(tag);

        root.setVisible(false);
    }

    private void playToMuch() {
        try {

            muchAudio = new StoryAudio();
            muchAudio.audio = MathUtils.random(new String[]{
                    "s41_too_much_1",
                    "s41_too_much_2",
                    "s41_too_much_3",
                    "s41_too_much_4"
            });

            gameScreen.audioService.prepareAndPlay(muchAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    private void playCancel() {
        try {
            cancelAudio = new StoryAudio();

            switch (servantName) {
                case "CARPETENER":
                    cancelAudio.audio = "s41_8_b";
                    break;
                case "BLOWER":
                    cancelAudio.audio = "s41_6_b";
                    break;
                case "SHOOTER":
                    cancelAudio.audio = "s41_4_b";
                    break;
                case "LISTENER":
                    cancelAudio.audio = "s41_3_b";
                    break;
                case "VASILIY":
                    cancelAudio.audio = "s41_11_b";
                    break;
                case "RUNNER":
                    cancelAudio.audio = "s41_2_b";
                    break;
                case "JUMPER":
                    cancelAudio.audio = "s41_7_b";
                    break;
                case "JOKER":
                    cancelAudio.audio = "s41_9_b";
                    break;
                case "USURER":
                    cancelAudio.audio = "s41_10_b";
                    break;
                case "GIGANT":
                    cancelAudio.audio = "s41_5_b";
                    break;
            }

            Log.i(tag, "playCancel " + servantName + " " + cancelAudio.audio);

            gameScreen.audioService.prepareAndPlay(cancelAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    private void playConfirm() {
        try {
            confirmAudio = new StoryAudio();

            switch (servantName) {
                case "CARPETENER":
                    confirmAudio.audio = "s41_8_a";
                    break;
                case "BLOWER":
                    confirmAudio.audio = "s41_6_a";
                    break;
                case "SHOOTER":
                    confirmAudio.audio = "s41_4_a";
                    break;
                case "LISTENER":
                    confirmAudio.audio = "s41_3_a";
                    break;
                case "VASILIY":
                    confirmAudio.audio = "s41_11_a";
                    break;
                case "RUNNER":
                    confirmAudio.audio = "s41_2_a";
                    break;
                case "JUMPER":
                    confirmAudio.audio = "s41_7_a";
                    break;
                case "JOKER":
                    confirmAudio.audio = "s41_9_a";
                    break;
                case "USURER":
                    confirmAudio.audio = "s41_10_a";
                    break;
                case "GIGANT":
                    confirmAudio.audio = "s41_5_a";
                    break;
            }

            Log.i(tag, "playConfirm " + servantName + " " + confirmAudio.audio);

            gameScreen.audioService.prepareAndPlay(confirmAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void fadeIn() {

        if (isFadeIn) return;

        GameState.pause(tag);

        isFadeIn = true;

        root.clearActions();

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(0, 20),
                Actions.parallel(
                        Actions.alpha(1, .3f),
                        Actions.moveBy(0, -20, .3f)
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                    }
                })
        ));

        noBtn.setDisabled(false);
        yesBtn.setDisabled(false);
    }

    public void fadeOut() {

        root.clearActions();

        root.addAction(Actions.sequence(
                Actions.alpha(0, .3f),
                Actions.visible(false)
        ));
    }

    private Actor primaryDecision(String text, float buttonBounds) {

        Texture bottom = interaction.assetManager.get("GameScreen/b_decision_last_line.png", Texture.class);
        Texture middle = interaction.assetManager.get("GameScreen/b_decision_add_line.png", Texture.class);
        Texture top = interaction.assetManager.get("GameScreen/b_decision_first_line.png", Texture.class);

        final NinePatchDrawable middleBackground = new NinePatchDrawable(new NinePatch(
                middle, 0, 0, 5, 5
        ));

        Image backMiddle = new Image(middleBackground);
        Image backBottom = new Image(bottom);
        Image backTop = new Image(top);

        BitmapFont font = game.fontProvider.getFont(FontProvider.h2);

        Label label = new WrapLabel(text,
                new Label.LabelStyle(font, Color.BLACK),
                buttonBounds);
        label.setAlignment(Align.center);

        Table labelContainer = new Table();
        labelContainer.add(label).center().fillX().expand()
                .padTop(5).padBottom(5)
                .padLeft(headerSize / 5f).padRight(headerSize / 5f);

        Stack stackMiddle = new Stack();
        stackMiddle.addActor(backMiddle);
        stackMiddle.addActor(labelContainer);

        final Table table = new Table();

        table.add(backTop)
                .expandX().height(headerSize).row();
        table.add(stackMiddle).row();
        table.add(backBottom)
                .expandX().height(50).row();

        final Stack header = createDefaultHeader();

        final Stack stack = new Stack();
        stack.addActorAt(0, table);
        stack.addActorAt(1, header);

        return stack;
    }

    private Stack createDefaultHeader() {

        Texture cannon = interaction.assetManager.get("GameScreen/an_cannons_main.png", Texture.class);
        Texture eagle = interaction.assetManager.get("servants/icon_eagle.png", Texture.class);

        SpriteDrawable cannonDrawable = new SpriteDrawable(new Sprite(cannon));
        SpriteDrawable cannonDrawableRight = new SpriteDrawable(new Sprite(cannon));
        cannonDrawableRight.getSprite().setFlip(true, false);

        FitImage left = new FitImage(cannonDrawable);
        FitImage right = new FitImage(cannonDrawableRight);
        FitImage center = new FitImage(eagle);

        Table layer1 = new Table();
        layer1.add(left).expand()
                .align(Align.topLeft)
                .padLeft(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Table layer2 = new Table();
        layer2.add(center).expand()
                .align(Align.top)
                .size(headerSize);

        Table layer3 = new Table();
        layer3.add(right).expand()
                .align(Align.topRight)
                .padRight(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Stack root = new Stack();
        root.setFillParent(true);
        root.add(layer1);
        root.add(layer2);
        root.add(layer3);

        return root;
    }

    public void triggerBackgroundUpdate() {

        HireStoryImage currentImage = interaction.storyManager.story.currentScenario.currentImage;
        currentImage.isActive = true;
        currentImage.isCompleted = false;
        currentImage.isPrepared = false;
        currentImage.drawable = null;

        interaction.imageService.prepareAndDisplay(currentImage);
    }

    public void update() {

        if (!isMounted()) return;

        if (muchAudio != null) {
            gameScreen.audioService.updateVolume(muchAudio);
        }
        if (confirmAudio != null) {
            gameScreen.audioService.updateVolume(confirmAudio);
        }
        if (cancelAudio != null) {
            gameScreen.audioService.updateVolume(cancelAudio);
        }
    }

    public void stopAllAudio() {
        if (muchAudio != null) {
            gameScreen.audioService.stop(muchAudio);
            muchAudio = null;
        }
        if (confirmAudio != null) {
            gameScreen.audioService.stop(confirmAudio);
            confirmAudio = null;
        }
        if (cancelAudio != null) {
            gameScreen.audioService.stop(cancelAudio);
            cancelAudio = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        stopAllAudio();

        if (nextTask != null) {
            nextTask.cancel();
            nextTask = null;
        }

    }
}