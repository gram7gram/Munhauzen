package ua.gram.munhauzen.repository;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.GameState;

public class AudioFailRepository {

    public static AudioFail find(GameState gameState, String id) {
        if (id == null) return null;

        for (AudioFail o : gameState.audioFailRegistry) {
            if (o.name.equals(id)) {
                return o;
            }
        }

        throw new GdxRuntimeException("Missing fail " + id);
    }
}
