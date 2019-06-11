package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import ua.gram.munhauzen.PlatformParams;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExternalFiles {

    public static FileHandle getGameStateFile() {
        return getExternal("state.json");
    }

    public static FileHandle getExpansionFile(PlatformParams params) {
        return getExternal(params.versionCode + "-expansion.obb");
    }

    public static FileHandle getExpansionDir() {
        return getExternal("expansion");
    }

    public static FileHandle getExpansionImagesDir() {
        return getExternal("expansion/images");
    }

    public static FileHandle getExpansionAudioDir() {
        return getExternal("expansion/audio");
    }

    public static FileHandle getGameArchiveFile() {
        return getExternal("game.zip");
    }

    public static FileHandle getHistoryFile() {
        return getExternal("history.json");
    }

    public static FileHandle getImagesFile() {
        return getExternal("game/images.json");
    }

    public static FileHandle getAudioFile() {
        return getExternal("game/audio.json");
    }

    public static FileHandle getAudioFailsFile() {
        return getExternal("game/audio-fails.json");
    }

    public static FileHandle getInventoryFile() {
        return getExternal("game/inventory.json");
    }

    public static FileHandle getChaptersFile() {
        return getExternal("game/chapters.json");
    }

    public static FileHandle getScenarioFile() {
        return getExternal("game/scenario.json");
    }

    private static FileHandle getExternal(String name) {
        return Gdx.files.external("Munhauzen/ua.gram.munhauzen.any/" + name);
    }
}
