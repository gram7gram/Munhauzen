package ua.gram.munhauzen.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * Copy of ExtendViewport
 */
public class GameViewport extends Viewport {

    public float minWorldWidth, minWorldHeight;
    public float maxWorldWidth, maxWorldHeight;

    public GameViewport(MunhauzenGame game) {

        float fixedWidth = MunhauzenGame.WORLD_WIDTH;
        float fixedHeight = MunhauzenGame.WORLD_HEIGHT;
        float notchSize = 0;

        this.minWorldWidth = fixedWidth;
        this.minWorldHeight = fixedHeight - notchSize;
        this.maxWorldWidth = fixedWidth;
        this.maxWorldHeight = fixedHeight - notchSize;

        setCamera(game.camera);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        // Fit min size to the screen.
        float worldWidth = minWorldWidth;
        float worldHeight = minWorldHeight;
        Vector2 scaled = Scaling.fit.apply(worldWidth, worldHeight, screenWidth, screenHeight);

        // Extend in the short direction.
        int viewportWidth = Math.round(scaled.x);
        int viewportHeight = Math.round(scaled.y);
        if (viewportWidth < screenWidth) {
            float toViewportSpace = viewportHeight / worldHeight;
            float toWorldSpace = worldHeight / viewportHeight;
            float lengthen = (screenWidth - viewportWidth) * toWorldSpace;
            if (maxWorldWidth > 0) lengthen = Math.min(lengthen, maxWorldWidth - minWorldWidth);
            worldWidth += lengthen;
            viewportWidth += Math.round(lengthen * toViewportSpace);
        } else if (viewportHeight < screenHeight) {
            float toViewportSpace = viewportWidth / worldWidth;
            float toWorldSpace = worldWidth / viewportWidth;
            float lengthen = (screenHeight - viewportHeight) * toWorldSpace;
            if (maxWorldHeight > 0) lengthen = Math.min(lengthen, maxWorldHeight - minWorldHeight);
            worldHeight += lengthen;
            viewportHeight += Math.round(lengthen * toViewportSpace);
        }

        setWorldSize(worldWidth, worldHeight);

        // Bottom.
        setScreenBounds(0, 0, viewportWidth, viewportHeight);

        apply(centerCamera);
    }
}
