package ua.gram.munhauzen.repository;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;

public class ImageRepository {

    public static final String LAST = "Last";

    public static Image find(GameState gameState, String id) {

        if (LAST.equals(id)) {
            return gameState.activeSave.lastImage;
        }

        for (Image o : gameState.imageRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing image " + id);
    }

}
