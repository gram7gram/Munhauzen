package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.balloons.fragment.BalloonsImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class BalloonsInteraction extends AbstractInteraction {

    public BalloonsImageFragment imageFragment;
    boolean isLoaded;
    public Array<Vector2> trajectoryPoints;

    public BalloonsInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        trajectoryPoints = gameScreen.game.databaseManager.loadBalloonTrajectory(
                MathUtils.random(new FileHandle[]{
                        Files.getBalloonTrajectory1(),
                        Files.getBalloonTrajectory2(),
                        Files.getBalloonTrajectory3(),
                })
        );

        assetManager.load("LoadingScreen/lv_cloud_1.png", Texture.class);
        assetManager.load("LoadingScreen/lv_cloud_2.png", Texture.class);
        assetManager.load("LoadingScreen/lv_cloud_3.png", Texture.class);
        assetManager.load("balloons/ducks_sheet_1x5.png", Texture.class);
        assetManager.load("balloons/inter_balloons_1.png", Texture.class);
        assetManager.load("balloons/inter_balloons_2.png", Texture.class);
        assetManager.load("balloons/inter_balloons_3.png", Texture.class);
        assetManager.load("balloons/inter_balloons_4.png", Texture.class);
        assetManager.load("balloons/inter_balloons_fond.jpg", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new BalloonsImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        gameScreen.ui.addListener(new ActorGestureListener() {

            ArrayList<Vector2> points = new ArrayList<>();

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                points.add(new Vector2(x, y));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                String log = "[";
                for (Vector2 point : points) {
                    int px = (int) (100 * point.x / MunhauzenGame.WORLD_WIDTH);
                    int py = (int) (100 * point.y / MunhauzenGame.WORLD_HEIGHT);
                    log += "{\"x\":" + px + ",\"y\":" + py + "},";
                }
                log += "]";

                points.clear();

                Gdx.files.external("balloon-3.json").writeString(log, false);
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                points.clear();
            }
        });

    }

    @Override
    public void update() {
        super.update();

        gameScreen.hideProgressBar();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        if (imageFragment != null)
            imageFragment.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        trajectoryPoints.clear();
        trajectoryPoints = null;

        isLoaded = false;
    }
}
