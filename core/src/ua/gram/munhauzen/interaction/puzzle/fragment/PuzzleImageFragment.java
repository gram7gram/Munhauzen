package ua.gram.munhauzen.interaction.puzzle.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.Dropzone;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleImageFragment extends Fragment {

    private final PuzzleInteraction interaction;
    public Group root, resultGroup, sourceGroup;
    public Dropzone dropzone;

    /**
     * 800x1000
     */
    public Image background;
    public float backgroundScale;

    public PuzzleItem stick, spoon, shoes, peas, key, hair, clocks, arrows, powder, rope;
    public Foot foot;
    public Table backgroundTable;
    public PrimaryButton resetButton;

    boolean areActorsInitialized;

    public PuzzleImageFragment(PuzzleInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        Texture tex1 = interaction.assetManager.get("puzzle/inter_puzzle_stick_1.png", Texture.class);
        Texture tex2 = interaction.assetManager.get("puzzle/inter_puzzle_spoon_1.png", Texture.class);
        Texture tex3 = interaction.assetManager.get("puzzle/inter_puzzle_shoes_1.png", Texture.class);
        Texture tex4 = interaction.assetManager.get("puzzle/inter_puzzle_peas_1.png", Texture.class);
        Texture tex5 = interaction.assetManager.get("puzzle/inter_puzzle_key_1.png", Texture.class);
        Texture tex6 = interaction.assetManager.get("puzzle/inter_puzzle_hair_1.png", Texture.class);
        Texture tex7 = interaction.assetManager.get("puzzle/inter_puzzle_clocks_1.png", Texture.class);
        Texture tex8 = interaction.assetManager.get("puzzle/inter_puzzle_arrows_1.png", Texture.class);
        Texture tex9 = interaction.assetManager.get("puzzle/inter_puzzle_powder_1.png", Texture.class);
        Texture tex10 = interaction.assetManager.get("puzzle/inter_puzzle_rope_1.png", Texture.class);
        Texture tex11 = interaction.assetManager.get("puzzle/inter_puzzle_fond_1.png", Texture.class);
        Texture tex12 = interaction.assetManager.get("puzzle/inter_puzzle_fond_2.png", Texture.class);

        resetButton = interaction.gameScreen.game.buttonBuilder.primary("Again", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    interaction.decisionManager.reset();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        resetButton.setWidth(MunhauzenGame.WORLD_WIDTH / 3f);
        resetButton .setHeight(MunhauzenGame.WORLD_HEIGHT / 12f);
        resetButton.setPosition(10, 10);

        background = new FitImage();

        stick = new Stick(tex1);
        spoon = new Spoon(tex2);
        shoes = new Shoes(tex3);
        peas = new Peas(tex4);
        key = new Key(tex5);
        hair = new Hair(tex6);
        clocks = new Clocks(tex7);
        arrows = new Arrows(tex8);
        powder = new Powder(tex9);
        rope = new Rope(tex10);
        foot = new Foot(tex12);
        dropzone = new Dropzone(interaction);

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center();

        setBackground(tex11);

        resultGroup = new Group();

        sourceGroup = new Group();
        sourceGroup.addActor(stick);
        sourceGroup.addActor(spoon);
        sourceGroup.addActor(shoes);
        sourceGroup.addActor(peas);
        sourceGroup.addActor(key);
        sourceGroup.addActor(hair);
        sourceGroup.addActor(clocks);
        sourceGroup.addActor(arrows);
        sourceGroup.addActor(powder);
        sourceGroup.addActor(rope);
        sourceGroup.addActor(foot);

        root = new Group();
        root.setTouchable(Touchable.enabled);
        root.addActor(backgroundTable);
        root.addActor(sourceGroup);
        root.addActor(dropzone);
        root.addActor(resultGroup);
        root.addActor(resetButton);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        resetButton.setDisabled(root.getTouchable() == Touchable.disabled || interaction.decisionManager.items.isEmpty());

        if (!areActorsInitialized) {

            areActorsInitialized = true;
            stick.init();
            spoon.init();
            shoes.init();
            peas.init();
            key.init();
            hair.init();
            clocks.init();
            arrows.init();
            powder.init();
            foot.init();
            rope.init();
            dropzone.init();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        areActorsInitialized = false;

    }

    private class Powder extends PuzzleItem {
        public Powder(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 10, 800);
        }
    }

    private class Rope extends PuzzleItem {
        public Rope(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 130, 800);
        }
    }

    private class Arrows extends PuzzleItem {
        public Arrows(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 250, 600);
        }
    }

    private class Spoon extends PuzzleItem {
        public Spoon(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 625, 695);
        }
    }

    private class Stick extends PuzzleItem {
        public Stick(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 580, 200);
        }
    }

    private class Shoes extends PuzzleItem {
        public Shoes(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 70, 650);
        }
    }

    private class Peas extends PuzzleItem {
        public Peas(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 510, 550);
        }
    }

    private class Key extends PuzzleItem {
        public Key(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 60, 690);
        }
    }

    private class Hair extends PuzzleItem {
        public Hair(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 490, 600);
        }
    }

    private class Clocks extends PuzzleItem {
        public Clocks(Texture texture) {
            super(interaction, texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 190, 700);
        }
    }


    private class Foot extends FitImage {

        public Foot(Texture texture) {
            super(texture);

            setTouchable(Touchable.disabled);
        }

        public void init() {

            setSizeRelativeToBackground(this, getDrawable());

            setPositionRelativeToBackground(this, 630, 750);
        }

    }

    public void setPositionRelativeToBackground(Actor actor, float x, float y) {
        actor.setPosition(
                background.getX() + background.getWidth() * (x / 800f),
                background.getY() + background.getHeight() * ((1000 - y) / 1000f)
        );
        actor.setBounds(
                actor.getX(),
                actor.getY(),
                Math.max(60, actor.getWidth()),
                Math.max(60, actor.getHeight())
        );
    }

    public void setSizeRelativeToBackground(Actor actor, Drawable texture) {
        actor.setSize(
                texture.getMinWidth() * backgroundScale * .6f,
                texture.getMinHeight() * backgroundScale * .6f
        );
    }

    public void setBackground(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        backgroundScale = 1f * MunhauzenGame.WORLD_WIDTH / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * backgroundScale;

        backgroundTable.getCell(background)
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(height);
    }
}
