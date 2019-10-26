package ua.gram.munhauzen.interaction.puzzle.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.Dropzone;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;
import ua.gram.munhauzen.interaction.puzzle.ui.Arrows;
import ua.gram.munhauzen.interaction.puzzle.ui.Clocks;
import ua.gram.munhauzen.interaction.puzzle.ui.Foot;
import ua.gram.munhauzen.interaction.puzzle.ui.Hair;
import ua.gram.munhauzen.interaction.puzzle.ui.Key;
import ua.gram.munhauzen.interaction.puzzle.ui.Peas;
import ua.gram.munhauzen.interaction.puzzle.ui.Powder;
import ua.gram.munhauzen.interaction.puzzle.ui.Rope;
import ua.gram.munhauzen.interaction.puzzle.ui.Shoes;
import ua.gram.munhauzen.interaction.puzzle.ui.Spoon;
import ua.gram.munhauzen.interaction.puzzle.ui.Stick;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleImageFragment extends InteractionFragment {

    private final PuzzleInteraction interaction;
    public FragmentRoot root;
    public Group resultGroup, sourceGroup;
    public Dropzone dropzone;

    public BackgroundImage backgroundImage;

    public PuzzleItem foot, stick, spoon, shoes, peas, key, hair, clocks, arrows, powder, rope;
    public PrimaryButton resetButton;

    public PuzzleImageFragment(PuzzleInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

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
        Texture tex12 = interaction.assetManager.get("puzzle/inter_puzzle_fond_2.png", Texture.class);

        resetButton = interaction.gameScreen.game.buttonBuilder.primary(interaction.t("puzzle_inter.retry_btn"), new ClickListener() {
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

        stick = new Stick(interaction, tex1);
        spoon = new Spoon(interaction, tex2);
        shoes = new Shoes(interaction, tex3);
        peas = new Peas(interaction, tex4);
        key = new Key(interaction, tex5);
        hair = new Hair(interaction, tex6);
        clocks = new Clocks(interaction, tex7);
        arrows = new Arrows(interaction, tex8);
        powder = new Powder(interaction, tex9);
        rope = new Rope(interaction, tex10);
        foot = new Foot(interaction, tex12);
        dropzone = new Dropzone(interaction);

        setBackground(
                interaction.assetManager.get("puzzle/inter_puzzle_fond_1.jpg", Texture.class),
                "puzzle/inter_puzzle_fond_1.jpg"
        );

        resultGroup = new Group();
        resultGroup.setTouchable(Touchable.disabled);

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

        Table resetTable = new Table();
        resetTable.pad(10);
        resetTable.add(resetButton)
                .align(Align.bottomLeft).expand()
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);

        Container dropzoneContainer = new Container<>(dropzone);
        dropzoneContainer.setTouchable(Touchable.childrenOnly);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
        root.addContainer(dropzoneContainer);
        root.addContainer(sourceGroup);
        root.addContainer(resultGroup);
        root.addContainer(resetTable);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        resetButton.setDisabled(root.getTouchable() == Touchable.disabled || interaction.decisionManager.items.isEmpty());

    }

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }
}
