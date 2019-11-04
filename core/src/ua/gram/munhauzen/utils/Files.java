package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static void toFile(InputStream is, FileHandle file) throws IOException {

        file.parent().mkdirs();

        OutputStream os = file.write(false);

        byte[] bytes = new byte[1024];
        int count;
        while ((count = is.read(bytes, 0, bytes.length)) != -1) {
            os.write(bytes, 0, count);
        }

        is.close();
        os.close();
    }
}
