package ua.gram.munhauzen.interaction.horn.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.HornInteraction;
import ua.gram.munhauzen.interaction.horn.ui.Note1;
import ua.gram.munhauzen.interaction.horn.ui.Note2;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HornImageFragment extends InteractionFragment {

    private final HornInteraction interaction;
    public FragmentRoot root;
    BackgroundImage horn;
    Note2 note2;
    Note1 note1;
    Container<Note1> note1Table;
    Container<Note2> note2Table;

    public HornImageFragment(HornInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        interaction.gameScreen.hideImageFragment();

        PrimaryButton button = interaction.gameScreen.game.buttonBuilder.primary("Continue", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                complete();
            }
        });

        horn = new BackgroundImage(interaction.gameScreen);
        horn.setOrigin(Align.center);
        horn.setTouchable(Touchable.disabled);

        note1 = new Note1();

        note2 = new Note2();

        note1Table = new Container<>(note1);
        note1Table.setClip(true);
        note1Table.setTouchable(Touchable.disabled);

        note2Table = new Container<>(note2);
        note2Table.setClip(true);
        note2Table.setTouchable(Touchable.disabled);

        Table btnTable = new Table();
        btnTable.setFillParent(true);
        btnTable.padTop(MunhauzenGame.WORLD_HEIGHT / 4f);
        btnTable.add(button).top().expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(note2Table);
        root.addContainer(note1Table);
        root.addContainer(horn);
        root.addContainer(btnTable);

        setHornBackground(
                interaction.assetManager.get("horn/int_horn_horn.png", Texture.class),
                "horn/int_horn_horn.png"
        );

        setNote1Background(
                interaction.assetManager.get("horn/int_horn_note_1.png", Texture.class)
        );

        setNote2Background(
                interaction.assetManager.get("horn/int_horn_note_2.png", Texture.class)
        );

        start();
    }

    private void start() {

        note1.start();
        note2.start();
    }

    public void update() {

        note1Table.setBounds(
                horn.background.getX(),
                horn.background.getY(),
                horn.background.getWidth(),
                horn.background.getHeight()
        );

        note2Table.setBounds(
                horn.background.getX(),
                horn.background.getY(),
                horn.background.getWidth(),
                horn.background.getHeight()
        );
    }

    private void complete() {
        Log.i(tag, "complete");

        try {

            interaction.gameScreen.interactionService.complete();

            interaction.gameScreen.interactionService.findStoryAfterInteraction();

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

    public void setHornBackground(Texture texture, String file) {

        horn.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }

    public void setNote1Background(Texture texture) {
        note1.setBackground(texture);
    }

    public void setNote2Background(Texture texture) {
        note2.setBackground(texture);
    }
}
