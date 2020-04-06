package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Files {

    public static FileHandle getHareScenarioFile() {
        return getInternal("game/hare-scenario.json");
    }

    public static FileHandle getPictureScenarioFile() {
        return getInternal("game/picture-scenario.json");
    }

    public static FileHandle getGeneralsScenarioFile() {
        return getInternal("game/generals-scenario.json");
    }

    public static FileHandle getWauwauScenarioFile() {
        return getInternal("game/wauwau-scenario.json");
    }

    public static FileHandle getCannonsScenarioFile() {
        return getInternal("game/cannons-scenario.json");
    }

    public static FileHandle getTimerScenarioFile() {
        return getInternal("game/timer-scenario.json");
    }

    public static FileHandle getTimer2ScenarioFile() {
        return getInternal("game/timer2-scenario.json");
    }

    private static FileHandle getInternal(String name) {
        FileHandle file = Gdx.files.internal(name);
        if (!file.exists()) {
            throw new GdxRuntimeException(name + " does not exist");
        }
        return file;
    }

    public static FileHandle getServantsHireScenarioFile() {
        return getInternal("game/servants-hire-scenario.json");
    }

    public static void toFile(InputStream is, FileHandle file, FileWriter.ProgressListener listener) throws IOException {
        new FileWriter(listener).stream(is, file);
    }
}
