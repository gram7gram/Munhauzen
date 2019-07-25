package ua.gram.munhauzen.interaction.picture.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.picture.Area;
import ua.gram.munhauzen.interaction.picture.PictureStory;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureScenarioFragment extends Fragment {

    private final PictureInteraction interaction;
    public Stack root;
    HashMap<String, Area> areas;

    public Array<PolygonSprite> sprites = new Array<>();
    public PolygonSpriteBatch polyBatch;
    Actor actor;

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

    private float[] convertToPixels(float[] vertices) {
        float[] pixels = new float[vertices.length];

        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i], y = vertices[i + 1];

            pixels[i] = interaction.imageFragment.backgroundImage.background.getX()
                    + x * interaction.imageFragment.backgroundImage.backgroundWidth / 100;

            pixels[i + 1] = interaction.imageFragment.backgroundImage.background.getY()
                    + y * interaction.imageFragment.backgroundImage.backgroundHeight / 100;
        }

        return pixels;
    }

    public void create() {

        Log.i(tag, "create");

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

        root = new Stack();
        root.setTouchable(Touchable.childrenOnly);
        root.setFillParent(true);
        root.addActor(actor);

        root.setName(tag);
    }

    private void makeDecision(String scenario) {

        try {

            interaction.scenarioFragment.destroy();

            PictureStory newStory = interaction.storyManager.create(scenario);

            interaction.storyManager.pictureStory = newStory;

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

        //drawPolygons();
    }

    private void drawPolygons() {

        if (isMounted() && sprites.size == 0) {

            Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.CYAN};

            int i = 0;
            for (String scenario : areas.keySet()) {
                Area polygon = areas.get(scenario);

                Color color = colors[i];

                ++i;

                Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pix.setColor(color.r, color.g, color.b, color.a / 3);
                pix.fill();

                PolygonRegion polyReg = new PolygonRegion(new TextureRegion(new Texture(pix)),
                        convertToPixels(polygon.getVertices()),
                        polygon.triangles);

                sprites.add(new PolygonSprite(polyReg));
            }
        }

    }

    public void draw() {
        if (isMounted() && sprites.size > 0) {
            polyBatch.begin();
            for (PolygonSprite sprite : sprites) {
                sprite.draw(polyBatch);
            }
            polyBatch.end();
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
