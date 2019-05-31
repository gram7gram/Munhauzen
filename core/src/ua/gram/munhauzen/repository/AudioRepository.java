package ua.gram.munhauzen.repository;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;

public class AudioRepository {

    public static Audio find(GameState gameState, String id) {
        if (id == null) return null;

        for (Audio o : gameState.audioRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing audio " + id);
    }
}
