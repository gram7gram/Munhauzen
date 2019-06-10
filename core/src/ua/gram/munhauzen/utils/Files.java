package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Files {

    public static FileHandle getGameStateFile() {
        return Gdx.files.external(".Munhauzen/ua.gram.munhauzen.any/state.json");
    }

    public static FileHandle getHistoryFile() {
        return Gdx.files.external(".Munhauzen/ua.gram.munhauzen.any/history.json");
    }

    public static FileHandle getImagesFile() {
        return getInternalConfig("game/images.json");
    }

    public static FileHandle getAudioFile() {
        return getInternalConfig("game/audio.json");
    }

    public static FileHandle getAudioFailsFile() {
        return getInternalConfig("game/audio-fails.json");
    }

    public static FileHandle getInventoryFile() {
        return getInternalConfig("game/inventory.json");
    }

    public static FileHandle getChaptersFile() {
        return getInternalConfig("game/chapters.json");
    }

    public static FileHandle getHareScenarioFile() {
        return getInternalConfig("game/hare-scenario.json");
    }
    public static FileHandle getGeneralsScenarioFile() {
        return getInternalConfig("game/generals-scenario.json");
    }

    public static FileHandle getScenarioFile() {
        return getInternalConfig("game/scenario.json");
    }

    private static FileHandle getInternalConfig(String name) {
        FileHandle file = Gdx.files.internal(name);
        if (!file.exists()) {
            throw new GdxRuntimeException(name + " does not exist");
        }
        return file;
    }
}
