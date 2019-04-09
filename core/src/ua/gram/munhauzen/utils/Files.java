package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

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
        return Gdx.files.internal("images.json");
    }

    public static FileHandle getAudioFile() {
        return Gdx.files.internal("audio.json");
    }

    public static FileHandle getItemsFile() {
        return Gdx.files.internal("items.json");
    }

    public static FileHandle getOptionsFile() {
        return Gdx.files.internal("options.json");
    }
}
