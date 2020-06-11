package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.animation.CannonAnimation;
import ua.gram.munhauzen.animation.LetterAAnimation;
import ua.gram.munhauzen.animation.LetterBAnimation;
import ua.gram.munhauzen.animation.LetterCAnimation;
import ua.gram.munhauzen.animation.LetterDAnimation;
import ua.gram.munhauzen.animation.LetterEAnimation;
import ua.gram.munhauzen.animation.LetterFAnimation;
import ua.gram.munhauzen.animation.LetterGAnimation;
import ua.gram.munhauzen.ui.ScenarioFragment;
import ua.gram.munhauzen.ui.WrapLabel;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.Log;

public class PrimaryDecision extends Stack {

    final String tag = getClass().getSimpleName();
    final ScenarioFragment fragment;
    final MunhauzenGame game;
    final ExpansionAssetManager assetManager;

    boolean visited;
    String text;
    int index;
    ClickListener onClick;
    HashMap<Integer, String> animatedMap;

    AnimatedImage letterInCenter, cannonLeft, cannonRight;
    final int headerSize, letterWidth = 180;
    public final int buttonSize;

    public PrimaryDecision(ScenarioFragment fragment, ExpansionAssetManager assetManager) {
        this.fragment = fragment;
        this.game = fragment.screen.game;
        this.assetManager = assetManager;

        headerSize = (int) Math.min(200, MunhauzenGame.WORLD_HEIGHT * .075f);
        buttonSize = (int) (MunhauzenGame.WORLD_WIDTH * .75f);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setOnClick(ClickListener onClick) {
        this.onClick = onClick;
    }

    public void setAnimatedMap(HashMap<Integer, String> animatedMap) {
        this.animatedMap = animatedMap;
    }

    public void init() {
        Texture bottom = assetManager.get("GameScreen/b_decision_last_line.png", Texture.class);
        Texture middle = assetManager.get("GameScreen/b_decision_add_line.png", Texture.class);
        Texture top = assetManager.get("GameScreen/b_decision_first_line.png", Texture.class);

        final NinePatchDrawable middleBackground = new NinePatchDrawable(new NinePatch(
                middle, 0, 0, 5, 5
        ));

        Image backMiddle = new Image(middleBackground);
        Image backBottom = new Image(bottom);
        Image backTop = new Image(top);

        BitmapFont font = game.fontProvider.getFont(FontProvider.h2);

        Label label = new WrapLabel(text,
                new Label.LabelStyle(font, Color.BLACK),
                buttonSize);
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
                .expandX().height(headerSize / 2f).row();

        final Stack header = createDefaultHeader(index);

        addActorAt(0, table);
        addActorAt(1, header);

        addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);

                try {

                    game.sfxService.onDecisionClicked();

                    cannonLeft.start();
                    cannonRight.start();

                    if (letterInCenter != null)
                        letterInCenter.start();

                    fragment.bgContainer.setVisible(false);

                    onClick.clicked(event, x, y);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        getColor().a = visited ? .65f : 1;
    }

    private Stack createDefaultHeader(int index) {

        Texture sheet = assetManager.get("GameScreen/an_cannons_sheet.png", Texture.class);
        Texture sheetLeft = assetManager.get("GameScreen/an_cannons_left_sheet.png", Texture.class);

        String letterResource = animatedMap.get(index);

        if (assetManager.isLoaded(letterResource, Texture.class)) {
            Texture letter = assetManager.get(letterResource, Texture.class);

            switch (index) {
                case 0:
                    letterInCenter = new LetterAAnimation(letter);
                    break;
                case 1:
                    letterInCenter = new LetterBAnimation(letter);
                    break;
                case 2:
                    letterInCenter = new LetterCAnimation(letter);
                    break;
                case 3:
                    letterInCenter = new LetterDAnimation(letter);
                    break;
                case 4:
                    letterInCenter = new LetterEAnimation(letter);
                    break;
                case 5:
                    letterInCenter = new LetterFAnimation(letter);
                    break;
                default:
                    letterInCenter = new LetterGAnimation(letter);
            }
        }

        cannonLeft = new CannonAnimation(sheet);
        cannonRight = new CannonAnimation(sheetLeft);

        float pad = buttonSize * .15f;

        float width1 = letterWidth * game.params.scaleFactor;

        float width2 = (buttonSize - pad * 2 - width1 * .2f) / 2f;
        float scale2 = 1f * width2 / cannonLeft.getCurrentDrawable().getMinWidth();
        float height2 = 1f * cannonLeft.getCurrentDrawable().getMinHeight() * scale2;

        Table layer1 = new Table();
        layer1.add(cannonLeft)
                .padTop(10)
                .height(height2).width(width2)
                .padLeft(pad)
                .expand().align(Align.topLeft);

        Table layer2 = new Table();

        if (letterInCenter != null) {

            float scale1 = 1f * width1 / letterInCenter.getCurrentDrawable().getMinWidth();
            float height1 = 1f * letterInCenter.getCurrentDrawable().getMinHeight() * scale1;

            layer2.add(letterInCenter)
                    .padLeft(width1 * .25f)
                    .width(width1).height(height1)
                    .expand().align(Align.top);
        }

        Table layer3 = new Table();
        layer3.add(cannonRight)
                .padTop(10)
                .height(height2).width(width2)
                .padRight(pad)
                .expand().align(Align.topRight);

        Stack root = new Stack();
        root.setFillParent(true);
        root.add(layer1);
        root.add(layer2);
        root.add(layer3);

        return root;
    }
}
