package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.ChapterTranslation;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.ImageTranslation;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StatueTranslation;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DatabaseManager {

    private final String tag = getClass().getSimpleName();

    private String listInternalAssets(String path) {

        String msg = "";
        for (FileHandle fileHandle : Gdx.files.internal(path).list()) {

            msg += "\r\n |-- " + fileHandle.path();
            if (fileHandle.isDirectory()) {
                msg += "\r\n" + listInternalAssets(fileHandle.path());
            }
        }

        return msg;
    }

    public void load(GameState state) {

        Log.i(tag, "Internal assets:" + listInternalAssets(""));

        try {
            state.imageRegistry = loadImages();
            Log.i(tag, "Loaded images x" + state.imageRegistry.size);
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }

        try {
            state.audioRegistry = loadAudio();
            Log.i(tag, "Loaded audio x" + state.audioRegistry.size);
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }
        try {
            state.audioFailRegistry = loadAudioFails();
            Log.i(tag, "Loaded audio-fails x" + state.audioFailRegistry.size);
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }
        try {
            state.inventoryRegistry = loadInventory();
            Log.i(tag, "Loaded inventory x" + state.inventoryRegistry.size);
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }
        try {
            state.scenarioRegistry = loadOptions();
            Log.i(tag, "Loaded story x" + state.scenarioRegistry.size);
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }
        try {
            state.chapterRegistry = loadChapters();
            Log.i(tag, "Loaded chapters x" + state.chapterRegistry.size);
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }
        try {
            state.history = loadHistory();
            Log.i(tag, "Loaded history");
        } catch (Throwable e) {
            Log.e(tag, e);
            Log.e(tag, e.getCause());
        }
    }

    private History loadHistory() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = Files.getHistoryFile();

        History history;
        if (!file.exists()) {
            history = new History();
        } else {
            history = json.fromJson(History.class, file);
        }

        return history;
    }

    @SuppressWarnings("unchecked")
    private Array<Scenario> loadOptions() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        FileHandle file = Files.getScenarioFile();

        return json.fromJson(Array.class, Scenario.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Chapter> loadChapters() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Chapter.class, "translations", ChapterTranslation.class);

        FileHandle file = Files.getChaptersFile();

        return json.fromJson(Array.class, Chapter.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Image> loadImages() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Image.class, "translations", ImageTranslation.class);

        FileHandle file = Files.getImagesFile();

        return json.fromJson(Array.class, Image.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Audio> loadAudio() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = Files.getAudioFile();

        return json.fromJson(Array.class, Audio.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<AudioFail> loadAudioFails() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = Files.getAudioFailsFile();

        return json.fromJson(Array.class, AudioFail.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Inventory> loadInventory() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Inventory.class, "statueTranslations", StatueTranslation.class);

        FileHandle file = Files.getInventoryFile();

        return json.fromJson(Array.class, Inventory.class, file);
    }
}
