package ua.gram.munhauzen.interaction.puzzle.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleImageFragment extends Fragment {

    private final PuzzleInteraction interaction;
    public Group root;
    public Dropzone dropzone;

    /**
     * 800x1000
     */
    public Image background;
    public float backgroundScale;

    public PuzzleItem stick, spoon, shoes, peas, key, hair, clocks, arrows, powder, rope;
    public Foot foot;
    public Table backgroundTable;

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

        backgroundScale = 1f * MunhauzenGame.WORLD_WIDTH / tex11.getWidth();
        float height = tex11.getHeight() * backgroundScale;

        background = new FitImage(tex11);

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
        dropzone = new Dropzone();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center()
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(height);

        root = new Group();
        root.addActor(backgroundTable);
        root.addActor(stick);
        root.addActor(spoon);
        root.addActor(shoes);
        root.addActor(peas);
        root.addActor(key);
        root.addActor(hair);
        root.addActor(clocks);
        root.addActor(arrows);
        root.addActor(powder);
        root.addActor(rope);
        root.addActor(foot);
        root.addActor(dropzone);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {
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

    private abstract class PuzzleItem extends FitImage {

        public PuzzleItem(Texture texture) {
            super(texture);
        }

        public void init() {

            setSizeRelativeToBackground(this, getDrawable());

            Random r = new Random();
            addAction(Actions.forever(
                    Actions.sequence(
                            Actions.delay(r.between(0, 1) / 10f),
                            Actions.moveBy(1, 0, .2f),
                            Actions.delay(r.between(0, 1) / 10f),
                            Actions.moveBy(0, 1, .2f),
                            Actions.delay(r.between(0, 1) / 10f),
                            Actions.moveBy(-1, 0, .2f),
                            Actions.delay(r.between(0, 1) / 10f),
                            Actions.moveBy(0, -1, .2f)
                    )
            ));

            final String name = getClass().getSimpleName().toLowerCase();

            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Log.i(tag, "clicked on " + name);

                    try {
                        if (interaction.decisionManager.items.contains(name)) {
                            interaction.decisionManager.items.remove(name);
                        } else {
                            interaction.decisionManager.items.add(name);
                        }

                        interaction.decisionManager.decide();
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                }
            });
        }
    }

    private class Powder extends PuzzleItem {
        public Powder(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 10, 700);
        }
    }

    private class Rope extends PuzzleItem {
        public Rope(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 130, 675);
        }
    }

    private class Arrows extends PuzzleItem {
        public Arrows(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 250, 480);
        }
    }

    private class Spoon extends PuzzleItem {
        public Spoon(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 625, 570);
        }
    }

    private class Stick extends PuzzleItem {
        public Stick(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 580, 150);
        }
    }

    private class Shoes extends PuzzleItem {
        public Shoes(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 70, 550);
        }
    }

    private class Peas extends PuzzleItem {
        public Peas(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 510, 430);
        }
    }

    private class Key extends PuzzleItem {
        public Key(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 60, 600);
        }
    }

    private class Hair extends PuzzleItem {
        public Hair(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 490, 480);
        }
    }

    private class Clocks extends PuzzleItem {
        public Clocks(Texture texture) {
            super(texture);
        }

        @Override
        public void init() {
            super.init();

            setPositionRelativeToBackground(this, 190, 565);
        }
    }

    private class Dropzone extends Actor {

        public Dropzone() {

        }

        public void init() {

            float scale = .85f;
            float width = 150;
            float height = width * (1 / scale);

            setSize(
                    width * backgroundScale,
                    height * backgroundScale
            );

            setPositionRelativeToBackground(this, 330, 530);
        }
    }

    private class Foot extends FitImage {

        public Foot(Texture texture) {
            super(texture);

            setTouchable(Touchable.disabled);
        }

        public void init() {

            setSizeRelativeToBackground(this, getDrawable());

            setPositionRelativeToBackground(this, 625, 620);
        }

    }

    public void setPositionRelativeToBackground(Actor actor, float x, float y) {
        actor.setPosition(
                background.getWidth() * (x / 800f),
                background.getHeight() * ((1000 - y) / 1000f)
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
}
