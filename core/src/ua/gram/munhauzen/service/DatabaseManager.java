package ua.gram.munhauzen.service;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.ChapterTranslation;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.ImageTranslation;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StatueTranslation;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.interaction.cannons.CannonsScenario;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryImage;
import ua.gram.munhauzen.interaction.generals.GeneralsScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;
import ua.gram.munhauzen.interaction.hare.HareScenario;
import ua.gram.munhauzen.interaction.picture.PictureScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryImage;
import ua.gram.munhauzen.interaction.timer.TimerScenario;
import ua.gram.munhauzen.interaction.timer2.Timer2Scenario;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.interaction.wauwau.WauStoryImage;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

public class DatabaseManager {

    private final String tag = getClass().getSimpleName();

    public void loadExternal(GameState state) {

        Log.i(tag, "loadExternal");

        try {
            state.imageRegistry = loadExternalImages();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.audioRegistry = loadExternalAudio();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.audioFailRegistry = loadExternalAudioFails();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.inventoryRegistry = loadExternalInventory();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.scenarioRegistry = loadExternalScenario();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.chapterRegistry = loadExternalChapters();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.history = loadHistory();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.menuState = loadMenuState();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.galleryState = loadGalleryState();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void persist(GameState gameState) {

        //Log.i(tag, "persist");

        try {
            persistHistory(gameState.history);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            persistMenuState(gameState.menuState);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            persistGalleryState(gameState.galleryState);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void persistGalleryState(GalleryState state) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getGalleryStateFile();

        json.toJson(state, file);
    }

    public void persistMenuState(MenuState state) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getMenuStateFile();

        json.toJson(state, file);
    }

    public void persistHistory(History history) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getHistoryFile();

        json.toJson(history, file);
    }

    private History loadHistory() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getHistoryFile();

        History history;
        if (!file.exists()) {
            history = new History();
        } else {
            history = json.fromJson(History.class, file);
        }

        loadActiveSave(history);

        return history;
    }

    private MenuState loadMenuState() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getMenuStateFile();

        MenuState state;
        if (!file.exists()) {
            state = new MenuState();
        } else {
            state = json.fromJson(MenuState.class, file);
        }

        return state;
    }

    private GalleryState loadGalleryState() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getGalleryStateFile();

        GalleryState state;
        if (!file.exists()) {
            state = new GalleryState();
        } else {
            state = json.fromJson(GalleryState.class, file);
        }

        return state;
    }

    private void loadActiveSave(History history) {
        Save save = loadSave(history.activeSaveId);

        history.activeSave = save;
        history.activeSaveId = save.id;
    }

    public Save loadSave(String id) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        Save save;

        FileHandle saveFile = ExternalFiles.getSaveFile(id);
        if (saveFile.exists()) {
            save = json.fromJson(Save.class, saveFile);
        } else {
            save = new Save();
        }

        return save;
    }

    public void persistSave(Save save, FileHandle saveFile) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        json.toJson(save, saveFile);
    }

    @SuppressWarnings("unchecked")
    public Array<TimerScenario> loadTimerScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, TimerScenario.class, Files.getTimerScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<Timer2Scenario> loadTimer2Scenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, Timer2Scenario.class, Files.getTimer2ScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<GeneralsScenario> loadGeneralsScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", GeneralsStoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, GeneralsScenario.class, Files.getGeneralsScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<WauScenario> loadWauwauScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", WauStoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, WauScenario.class, Files.getWauwauScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<CannonsScenario> loadCannonsScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", CannonsStoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, CannonsScenario.class, Files.getCannonsScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<HareScenario> loadHareScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, HareScenario.class, Files.getHareScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<PictureScenario> loadPictureScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(Array.class, PictureScenario.class, Files.getPictureScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public Array<HireScenario> loadServantsHireScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "images", HireStoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);

        return json.fromJson(Array.class, HireScenario.class, Files.getServantsHireScenarioFile());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Scenario> loadExternalScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, Scenario.class, ExternalFiles.getScenarioFile());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Chapter> loadExternalChapters() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Chapter.class, "translations", ChapterTranslation.class);

        FileHandle file = ExternalFiles.getChaptersFile();

        return json.fromJson(ArrayList.class, Chapter.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Image> loadExternalImages() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Image.class, "translations", ImageTranslation.class);

        FileHandle file = ExternalFiles.getImagesFile();

        return json.fromJson(ArrayList.class, Image.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Audio> loadExternalAudio() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getAudioFile();

        return json.fromJson(ArrayList.class, Audio.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<AudioFail> loadExternalAudioFails() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getAudioFailsFile();

        return json.fromJson(ArrayList.class, AudioFail.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Inventory> loadExternalInventory() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Inventory.class, "statueTranslations", StatueTranslation.class);

        FileHandle file = ExternalFiles.getInventoryFile();

        return json.fromJson(ArrayList.class, Inventory.class, file);
    }
}
