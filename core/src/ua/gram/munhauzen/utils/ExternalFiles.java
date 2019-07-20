package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.PlatformParams;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.expansion.response.Part;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExternalFiles {

    public static FileHandle getExpansionPartFile(Part part) {
        return getExternal("part" + part.part + ".zip");
    }

    public static void updateNomedia() {
        getExternal(getExpansionAudioDir() + "/.nomedia").write(false);
        getExternal(getExpansionImagesDir() + "/.nomedia").write(false);
    }

    public static FileHandle getExpansionInfoFile(PlatformParams params) {
        return getExternal(params.versionCode + "-expansion.json");
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

    public static FileHandle getExpansionAudio(Audio audio) {
        return Gdx.files.external(getExpansionDir().path() + "/" + audio.file);
    }

    public static FileHandle getExpansionImage(Image image) {
        return Gdx.files.external(getExpansionDir().path() + "/" + image.file);
    }

    public static FileHandle getExpansionFile(String file) {
        return Gdx.files.external(getExpansionDir().path() + "/" + file);
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

        if (MunhauzenGame.IS_EXPANSION_HIDDEN) {
            FileHandle dir = Gdx.files.external("Munhauzen");
            if (dir.isDirectory()) {
                dir.moveTo(Gdx.files.external(".Munhauzen"));
            }

            return Gdx.files.external(".Munhauzen/ua.gram.munhauzen.any/" + name);
        } else {
            FileHandle dir = Gdx.files.external(".Munhauzen");
            if (dir.isDirectory()) {
                dir.moveTo(Gdx.files.external("Munhauzen"));
            }

            return Gdx.files.external("Munhauzen/ua.gram.munhauzen.any/" + name);
        }
    }
}
