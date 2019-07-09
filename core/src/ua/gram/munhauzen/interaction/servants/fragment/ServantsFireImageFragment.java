package ua.gram.munhauzen.interaction.servants.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;
import ua.gram.munhauzen.interaction.generals.animation.FireLeftAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FireRightAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FumesAnimation;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ServantsFireImageFragment extends Fragment {

    private final ServantsInteraction interaction;
    FumesAnimation fumes;
    FireLeftAnimation fireLeft;
    FireRightAnimation fireRight;
    public Group root, items;
    public Image background;
    public Table backgroundTable;
    float backgroundWidth;

    public ServantsFireImageFragment(ServantsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        Texture fumesTexture = interaction.assetManager.get("generals/an_general_1_sheet_3x1.png", Texture.class);
        Texture fireLeftTexture = interaction.assetManager.get("generals/an_general_2_sheet_3x1.png", Texture.class);
        Texture fireRightTexture = interaction.assetManager.get("generals/an_general_3_sheet_3x1.png", Texture.class);

        background = new FitImage();

        fumes = new FumesAnimation(fumesTexture, background);
        fireLeft = new FireLeftAnimation(fireLeftTexture, background);
        fireRight = new FireRightAnimation(fireRightTexture, background);

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).right().expand().fill();

        fumes.setVisible(false);
        fireLeft.setVisible(false);
        fireRight.setVisible(false);

        items = new Group();

        root = new Group();
        root.addActor(backgroundTable);

        root.addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                try {

                    root.clearActions();

                    float newX = background.getX() + deltaX;

                    float leftBound = -backgroundWidth + MunhauzenGame.WORLD_WIDTH;
                    float rightBound = 0;

                    if (leftBound < newX && newX < rightBound) {
                        background.setX(background.getX() + deltaX);
                    }

                    if (background.getX() > 0) background.setX(0);
                    if (background.getX() < leftBound) background.setX(leftBound);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }

            }
        });

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }
}
