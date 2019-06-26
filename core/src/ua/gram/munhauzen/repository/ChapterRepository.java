package ua.gram.munhauzen.repository;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.GameState;

public class ChapterRepository {

    public static Chapter find(GameState gameState, String id) {

        for (Chapter o : gameState.chapterRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing chapter " + id);
    }
}
