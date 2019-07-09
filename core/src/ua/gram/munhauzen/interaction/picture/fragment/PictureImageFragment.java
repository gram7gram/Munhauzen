package ua.gram.munhauzen.interaction.picture.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageFragment extends Fragment {

    private final PictureInteraction interaction;
    public Stack root;
    Image background;
    Table backgroundTable;
    HashMap<String, Polygon> areas;
    float xFrom = 0, xTo = 800, yFrom = 0, yTo = 1022;

    public PictureImageFragment(PictureInteraction interaction) {
        this.interaction = interaction;
        areas = new HashMap<>();


        areas.put("a36_a", new Polygon(convertToPercent(new float[]{
                xFrom, 600,
                392, 600,
                456, 534,
                476, yFrom,
                xFrom, yFrom
        })));

        areas.put("a36_b", new Polygon(convertToPercent(new float[]{
                xFrom, yTo,
                440, 804,
                400, 684,
                608, 690,
                xTo, yTo
        })));

        areas.put("a36_c", new Polygon(convertToPercent(new float[]{
                xFrom, yTo,
                440, 804,
                392, 600,
                xFrom, 600
        })));

        areas.put("a36_d", new Polygon(convertToPercent(new float[]{
                456, 534,
                616, 536,
                xTo, yFrom,
                476, yFrom
        })));

        areas.put("a36_e", new Polygon(convertToPercent(new float[]{
                400, 684,
                608, 690,
                616, 536,
                456, 534,
                392, 600
        })));

    }


    private float[] convertToPercent(float[] vertices) {
        float[] percent = new float[vertices.length];

        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i], y = vertices[i + 1];

            percent[i] = x / xTo;
            percent[i + 1] = (yTo - y) / yTo;
        }

        return percent;
    }

    public void create() {

        Log.i(tag, "create");

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                float xPercent = x > xTo ? xTo / x : x / xTo;
                float yPercent = y > yTo ? yTo / y : y / yTo;

                Log.i(tag, "clicked % " + (int)(xPercent * 100) + "x" + (int)(yPercent * 100));

                for (String scenario : areas.keySet()) {
                    if (areas.get(scenario).contains(xPercent, yPercent)) {
                        Log.i(tag, "Match " + scenario);

                        complete(scenario);

                        break;
                    }
                }
            }
        });

        root = new Stack();
        root.setFillParent(true);
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(backgroundTable);

        setBackground(
                interaction.assetManager.get("picture/p35_what.jpg", Texture.class)
        );
    }

    private void complete(String scenario) {
        Log.i(tag, "complete " + scenario);

        try {

            interaction.gameScreen.interactionService.complete();

            Story newStory = interaction.gameScreen.storyManager.create(scenario);

            interaction.gameScreen.setStory(newStory);

            interaction.gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void setBackground(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * width / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * scale;

        backgroundTable.getCell(background)
                .width(width)
                .height(height);
    }

    public void update() {

    }

    @Override
    public void dispose() {
        super.dispose();


    }
}
