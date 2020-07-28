package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import ua.gram.munhauzen.PlatformParams;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.expansion.response.Part;

public class ExternalFiles {

    public static FileHandle getExpansionPartFile(PlatformParams params, Part part) {
        return getExternal(params, "part" + part.part + ".zip");
    }

    public static void updateNomedia(PlatformParams params) {
        try {
            String[] dirs = {
                    "expansion/gallery",
                    "expansion/menu",
                    "expansion/GameScreen",
                    "expansion/ui",
                    "expansion/saves",
                    "expansion/fails",
                    "expansion/victory",

                    "expansion/images",
                    "expansion/audio",

                    "expansion/balloons",
                    "expansion/cannons",
                    "expansion/chapter",
                    "expansion/continue",
                    "expansion/date",
                    "expansion/generals",
                    "expansion/hare",
                    "expansion/horn",
                    "expansion/lions",
                    "expansion/picture",
                    "expansion/puzzle",
                    "expansion/servants",
                    "expansion/slap",
                    "expansion/swamp",
                    "expansion/timer",
                    "expansion/timer2",
                    "expansion/wau",

                    "expansion/statues",
            };
            for (String dir : dirs) {
                getExternal(params, dir).mkdirs();
                getExternal(params, dir + "/.nomedia").write(false);
            }
            getExternal(params, "expansion/.nomedia").write(false);
        } catch (Throwable ignore) {

        }
    }

    public static FileHandle getExpansionInfoFile(PlatformParams params) {
        return getExternal(params, params.expansionVersion + "-expansion.json");
    }

    public static FileHandle getExpansionDir(PlatformParams params) {
        return getExternal(params, "expansion");
    }

    public static FileHandle getExpansionAudio(PlatformParams params, Audio audio) {
        return Gdx.files.external(getExpansionDir(params).path() + "/" + audio.file);
    }

    public static FileHandle getExpansionAudio(PlatformParams params, AudioFail audio) {
        return Gdx.files.external(getExpansionDir(params).path() + "/" + audio.file);
    }

    public static FileHandle getExpansionImage(PlatformParams params, Image image) {
        return Gdx.files.external(getExpansionDir(params).path() + "/" + image.file);
    }

    public static FileHandle getExpansionFile(PlatformParams params, String file) {
        return Gdx.files.external(getExpansionDir(params).path() + "/" + file);
    }

    public static FileHandle getHistoryFile(PlatformParams params) {
        return getExternal(params, "history.json");
    }

    public static FileHandle getMenuStateFile(PlatformParams params) {
        return getExternal(params, "menu-state.json");
    }

    public static FileHandle getPurchaseStateFile(PlatformParams params) {
        return getExternal(params, "purchases.json");
    }

    public static FileHandle getGamePreferencesFile(PlatformParams params) {
        return getExternal(params, "preferences.json");
    }

    public static FileHandle getGalleryStateFile(PlatformParams params) {
        return getExternal(params, "gallery-state.json");
    }

    public static FileHandle getAchievementStateFile(PlatformParams params) {
        return getExternal(params, "achievement-state.json");
    }

    public static FileHandle getFailsStateFile(PlatformParams params) {
        return getExternal(params, "fails-state.json");
    }

    public static FileHandle getSaveFile(PlatformParams params, String id) {
        return getExternal(params, "save-" + id + ".json");
    }

    public static FileHandle getActiveSaveFile(PlatformParams params) {
        return getExternal(params, "save-active.json");
    }

    public static FileHandle getStoryFile(PlatformParams params) {
        return getExternal(params, "story.json");
    }

    public static FileHandle getActiveSaveBackupFile(PlatformParams params) {
        return getExternal(params, "save-active.old");
    }

    public static FileHandle getExternal(PlatformParams params, String name) {
        return Gdx.files.external(params.storageDirectory + "/" + name);
    }

    public static void createActiveSaveBackup(PlatformParams params) {
        FileHandle save = ExternalFiles.getActiveSaveFile(params);

        if (!save.exists()) return;

        save.moveTo(
                ExternalFiles.getActiveSaveBackupFile(params)
        );

    }
}
