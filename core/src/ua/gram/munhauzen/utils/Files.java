package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Files {

    public static FileHandle getImagesFile() {
        return getInternal("game/images.json");
    }

    public static FileHandle getAudioFile() {
        return getInternal("game/audio.json");
    }

    public static FileHandle getAudioFailsFile() {
        return getInternal("game/audio-fails.json");
    }

    public static FileHandle getInventoryFile() {
        return getInternal("game/inventory.json");
    }

    public static FileHandle getChaptersFile() {
        return getInternal("game/chapters.json");
    }

    public static FileHandle getHareScenarioFile() {
        return getInternal("game/hare-scenario.json");
    }

    public static FileHandle getGeneralsScenarioFile() {
        return getInternal("game/generals-scenario.json");
    }

    public static FileHandle getTimerScenarioFile() {
        return getInternal("game/timer-scenario.json");
    }

    public static FileHandle getBalloonTrajectory1() {
        return getInternal("game/balloon-1.json");
    }

    public static FileHandle getBalloonTrajectory2() {
        return getInternal("game/balloon-2.json");
    }

    public static FileHandle getBalloonTrajectory3() {
        return getInternal("game/balloon-3.json");
    }

    public static FileHandle getScenarioFile() {
        return getInternal("game/scenario.json");
    }

    private static FileHandle getInternal(String name) {
        FileHandle file = Gdx.files.internal(name);
        if (!file.exists()) {
            throw new GdxRuntimeException(name + " does not exist");
        }
        return file;
    }
}
