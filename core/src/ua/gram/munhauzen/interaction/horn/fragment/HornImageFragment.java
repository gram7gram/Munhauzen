package ua.gram.munhauzen.interaction.horn.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.HornInteraction;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HornImageFragment extends InteractionFragment {

    private final HornInteraction interaction;
    public Stack root;
    Image horn, note1, note2;
    Table hornTable, note1Table, note2Table;
    StoryAudio introAudio;
    float note1Height, note1Width, note2Height, note2Width, hornHeight;

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

        horn = new FitImage();

        hornTable = new Table();
        hornTable.setFillParent(true);
        hornTable.add(horn).bottom().expand();

        note1 = new FitImage();

        note2 = new FitImage();

        note1Table = new Table();
        note1Table.setFillParent(true);
        note1Table.add(note1);

        note2Table = new Table();
        note2Table.setFillParent(true);
        note2Table.add(note2);

        Table btnTable = new Table();
        btnTable.setFillParent(true);
        btnTable.padTop(MunhauzenGame.WORLD_HEIGHT / 4f);
        btnTable.add(button).top().expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f);

        root = new Stack();
        root.setFillParent(true);
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(note2Table);
        root.addActor(note1Table);
        root.addActor(hornTable);
        root.addActor(btnTable);

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
        playIntro();

        note1.addAction(Actions.forever(
                Actions.sequence(
                        Actions.moveTo(MunhauzenGame.WORLD_WIDTH + note1Width * 1.2f, -note1Height),
                        Actions.moveTo(-MunhauzenGame.WORLD_WIDTH * .3f, MunhauzenGame.WORLD_HEIGHT, 5)
                )
        ));

        note2.addAction(Actions.forever(
                Actions.sequence(
                        Actions.moveTo(MunhauzenGame.WORLD_WIDTH + note2Width * .2f, -note2Height),
                        Actions.delay(1),
                        Actions.moveTo(-MunhauzenGame.WORLD_WIDTH * .3f - note2Width, MunhauzenGame.WORLD_HEIGHT, 6),
                        Actions.delay(1)
                )
        ));
    }

    public void update() {
        note1.setOrigin(Align.bottom);
        note2.setOrigin(Align.bottom);
        horn.setOrigin(Align.center);

        note1.setRotation(30);
        note2.setRotation(30);

        note1.setPosition(MunhauzenGame.WORLD_WIDTH + note1Width * 1.2f, -note1Height);
        note2.setPosition(MunhauzenGame.WORLD_WIDTH + note2Width * .2f, -note2Height);

        if (introAudio != null) {
            if (introAudio.player != null) {
                interaction.gameScreen.audioService.updateVolume(introAudio);
            }
        }
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

    private void playIntro() {
        try {
            introAudio = new StoryAudio();
            introAudio.audio = "a34_music";

            interaction.gameScreen.audioService.prepareAndPlay(introAudio);

            introAudio.player.setLooping(true);

        } catch (Throwable e) {
            Log.e(tag, e);

            introAudio.duration = 1000;
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setHornBackground(Texture texture, String file) {

        horn.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * width / horn.getDrawable().getMinWidth();
        hornHeight = 1f * horn.getDrawable().getMinHeight() * scale;

        hornTable.getCell(horn)
                .width(width)
                .height(hornHeight);

        interaction.gameScreen.setLastBackground(file);
    }

    public void setNote1Background(Texture texture) {

        note1.setDrawable(new SpriteDrawable(new Sprite(texture)));

        note1Height = MunhauzenGame.WORLD_HEIGHT * .75f;
        float scale = 1f * note1Height / note1.getDrawable().getMinHeight();
        note1Width = 1f * note1.getDrawable().getMinWidth() * scale;

        note1Table.getCell(note1)
                .width(note1Width)
                .height(note1Height);
    }

    public void setNote2Background(Texture texture) {

        note2.setDrawable(new SpriteDrawable(new Sprite(texture)));

        note2Height = MunhauzenGame.WORLD_HEIGHT * .75f;
        float scale = 1f * note2Height / note2.getDrawable().getMinHeight();
        note2Width = 1f * note2.getDrawable().getMinWidth() * scale;

        note2Table.getCell(note2)
                .width(note2Width)
                .height(note2Height);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (introAudio != null) {
            interaction.gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

    }
}
