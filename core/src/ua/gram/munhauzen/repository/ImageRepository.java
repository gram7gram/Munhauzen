package ua.gram.munhauzen.repository;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;

public class ImageRepository {

    public static Image find(GameState gameState, final String id) {

        if ("Last".equals(id)) {
            if (gameState.lastImage != null) {
                return gameState.lastImage;
            }
        }

        for (Image o : gameState.imageRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing image " + id);
    }

}
