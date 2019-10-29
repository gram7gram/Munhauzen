package ua.gram.munhauzen.interaction.continye.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.ContinueInteraction;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ContinueImageFragment extends InteractionFragment {

    private final ContinueInteraction interaction;
    public FragmentRoot root;
    public boolean isFadeIn, isFadeOut;
    StoryImage lastImage;

    public ContinueImageFragment(ContinueInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        PrimaryButton button = button(interaction.t("continue_inter.btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                root.setTouchable(Touchable.disabled);

                interaction.gameScreen.game.sfxService.onAnyBtnClicked();

                interaction.complete();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);
        table.add(button).center()
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT);

        root = new FragmentRoot();
        root.addContainer(table);

        lastImage = new StoryImage();
        lastImage.image = ImageRepository.LAST;
    }

    public void update() {
        interaction.gameScreen.imageService.prepare(lastImage, new Timer.Task() {
            @Override
            public void run() {
                interaction.gameScreen.imageService.onPrepared(lastImage);
            }
        });
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void fadeIn() {

        if (root.isVisible()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);
        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.fadeIn(.3f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        })
                )
        );
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.fadeOut(.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                root.setVisible(false);

                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        })
                )
        );
    }


    public boolean canFadeOut() {
        return root.isVisible() && !isFadeOut;
    }

    public PrimaryButton button(String text, final ClickListener onClick) {
        Texture dangerEnabled = interaction.assetManager.get("continue/btn_enabled.png", Texture.class);
        Texture dangerDisabled = interaction.assetManager.get("continue/btn_disabled.png", Texture.class);

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(dangerEnabled, 90, 90, 0, 0));
        NinePatchDrawable background2 = new NinePatchDrawable(new NinePatch(dangerDisabled, 90, 90, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = interaction.gameScreen.game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background2;
        style.fontColor = Color.BLACK;

        PrimaryButton button = new PrimaryButton(text, style);

        button.addListener(onClick);

        return button;
    }
}
