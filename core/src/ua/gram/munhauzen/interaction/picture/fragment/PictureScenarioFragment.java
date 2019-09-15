package ua.gram.munhauzen.interaction.picture.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.picture.Area;
import ua.gram.munhauzen.interaction.picture.PictureStory;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureScenarioFragment extends Fragment {

    private final PictureInteraction interaction;
    public Stack root;
    HashMap<String, Area> areas;

    public ArrayList<PolygonSprite> sprites = new ArrayList<>();
    public PolygonSpriteBatch polyBatch;
    Actor actor;
    FitImage imgLeft, imgRight, imgTop;
    Table decorLeft, decorRight, decorTop;

    public PictureScenarioFragment(PictureInteraction interaction) {
        this.interaction = interaction;
        areas = new HashMap<>();

        polyBatch = new PolygonSpriteBatch();

        areas.put("a36_a", new Area(new float[]{
                0, 46, //A
                49, 46, //B
                57, 50, //C
                57, 100, //D
                0, 100, //E
        }, new short[]{
                0, 1, 2, //A-B-C
                2, 3, 4, //C-D-E
                4, 2, 0, //E-B-A
        }));

        areas.put("a36_b", new Area(new float[]{
                0, 0, //I
                55, 27, //J
                55, 38, //K
                76, 38, //M,
                76, 50, //N
                100, 50, //F
                100, 0 //O
        }, new short[]{
                0, 1, 6, //I-J-O
                1, 2, 3, //J-K-M
                1, 3, 6, //J-M-O
                3, 4, 6, //M-N-O
                4, 5, 6, //N-F-O
        }));

        areas.put("a36_c", new Area(new float[]{
                0, 0, //I
                55, 27, //J
                55, 38, //K
                50, 38, //L
                49, 46, //B
                0, 46, //A
        }, new short[]{
                0, 1, 2, //I-J-K
                2, 3, 0, //K-L-I
                3, 4, 5, //L-B-A
                5, 3, 0, //A-L-I
        }));

        areas.put("a36_d", new Area(new float[]{
                57, 50, //C
                100, 50, //F
                100, 100, //G
                57, 100, //H
        }, new short[]{
                0, 1, 2, //C-F-G
                2, 3, 0, //G-H-C
        }));

        areas.put("a36_e", new Area(new float[]{
                50, 38, //L
                76, 38, //M
                76, 50, //N
                57, 50, //C
                49, 46, //B
        }, new short[]{
                0, 1, 2, //L-M-N
                2, 3, 4, //N-C-B
                4, 2, 0, //B-N-L
        }));

    }

    public void create() {

        Log.i(tag, "create");

        interaction.assetManager.load("GameScreen/b_star_game.png", Texture.class);
        interaction.assetManager.load("GameScreen/b_tulip_1.png", Texture.class);

        interaction.assetManager.finishLoading();

        actor = new Actor();

        actor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                int xPercent = (int) (100 * x / interaction.imageFragment.backgroundImage.backgroundWidth);
                int yPercent = (int) (100 * y / interaction.imageFragment.backgroundImage.backgroundHeight);

                Log.i(tag, "clicked % " + xPercent + "x" + yPercent);

                for (String scenario : areas.keySet()) {
                    if (areas.get(scenario).contains(xPercent, yPercent)) {
                        Log.i(tag, "Match " + scenario);

                        makeDecision(scenario);

                        break;
                    }
                }
            }
        });

        Texture borders = interaction.assetManager.get("GameScreen/b_tulip_1.png", Texture.class);
        Texture drawableTop = interaction.assetManager.get("GameScreen/b_star_game.png", Texture.class);

        Sprite drawableLeft = new Sprite(borders);

        Sprite drawableRight = new Sprite(borders);
        drawableRight.setFlip(true, false);

        imgLeft = new FitImage(new SpriteDrawable(drawableLeft));
        imgRight = new FitImage(new SpriteDrawable(drawableRight));
        imgTop = new FitImage(drawableTop);

        decorLeft = new Table();
        decorLeft.setTouchable(Touchable.disabled);
        decorLeft.add(imgLeft).align(Align.topLeft).expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 4f);

        decorTop = new Table();
        decorTop.setTouchable(Touchable.disabled);
        decorTop.add(imgTop).align(Align.top).expand()
                .width(MunhauzenGame.WORLD_WIDTH / 5f)
                .height(MunhauzenGame.WORLD_HEIGHT / 13f);

        decorRight = new Table();
        decorRight.setTouchable(Touchable.disabled);
        decorRight.add(imgRight).align(Align.topRight).expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 4f);

        root = new Stack();
        root.setTouchable(Touchable.childrenOnly);
        root.setFillParent(true);
        root.addActor(actor);
        root.add(decorLeft);
        root.add(decorTop);
        root.add(decorRight);

        root.setName(tag);
        root.setVisible(false);
    }

    public void fadeIn() {

        if (!isMounted()) return;

        root.setVisible(true);

        fadeInDecoration();
    }

    private void fadeInDecoration() {

        if (!isMounted()) return;

        float duration = .3f;

        decorLeft.addAction(Actions.moveBy(-imgLeft.getWidth(), 0));
        decorLeft.addAction(Actions.alpha(0));
        decorLeft.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );

        decorTop.addAction(Actions.moveBy(0, imgTop.getHeight()));
        decorTop.addAction(Actions.alpha(0));
        decorTop.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );

        decorRight.addAction(Actions.moveBy(imgRight.getWidth(), 0));
        decorRight.addAction(Actions.alpha(0));
        decorRight.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );
    }

    private void makeDecision(String scenario) {

        try {

            interaction.scenarioFragment.destroy();

            PictureStory newStory = interaction.storyManager.create(scenario);

            interaction.storyManager.story = newStory;

            interaction.storyManager.startLoadingResources();
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        if (interaction.imageFragment != null) {

            BackgroundImage backgroundImage = interaction.imageFragment.backgroundImage;

            actor.setPosition(
                    backgroundImage.background.getX(),
                    backgroundImage.background.getY()
            );

            actor.setSize(
                    backgroundImage.backgroundWidth,
                    backgroundImage.backgroundHeight
            );
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        for (PolygonSprite sprite : sprites) {
            sprite.getRegion().getRegion().getTexture().dispose();
        }
        sprites.clear();
    }
}
