package ua.gram.munhauzen.service;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.ChapterTranslation;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.FailsState;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.History;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.ImageTranslation;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StatueTranslation;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
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
    final MunhauzenGame game;
    final ObjectMapper om;

    public DatabaseManager(MunhauzenGame game) {
        this.game = game;
        om = new ObjectMapper();
    }

    public ExpansionResponse loadExpansionInfo() {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);
        if (!file.exists()) return null;

        try {
            ExpansionResponse result = om.readValue(file.file(), ExpansionResponse.class);

            if (result != null && result.version == game.params.versionCode) {
                return result;
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        file.delete();

        return null;
    }

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

            ExternalFiles.getHistoryFile().delete();

            state.history = new History();
        }

        try {
            state.menuState = loadMenuState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getMenuStateFile().delete();

            state.menuState = new MenuState();
        }

        try {
            state.galleryState = loadGalleryState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getGalleryStateFile().delete();

            state.galleryState = new GalleryState();
        }

        try {
            state.failsState = loadFailsState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getFailsStateFile().delete();

            state.failsState = new FailsState();
        }

        try {
            loadActiveSave(state);
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getSaveFile(state.history.activeSaveId).delete();

            state.setActiveSave(new Save("1"));
        }
    }

    public void persist(final GameState gameState) {

        //Log.i(tag, "persist");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    persistHistory(gameState.history);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    persistSave(gameState.activeSave);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    persistMenuState(gameState.menuState);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    persistGalleryState(gameState.galleryState);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    persistFailsState(gameState.failsState);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }).start();
    }

    public void persistGalleryState(GalleryState state) throws IOException {

        FileHandle file = ExternalFiles.getGalleryStateFile();

        om.writeValue(file.file(), state);
    }

    public void persistMenuState(MenuState state) throws IOException {
        FileHandle file = ExternalFiles.getMenuStateFile();

        om.writeValue(file.file(), state);
    }

    public void persistFailsState(FailsState state) throws IOException {
        FileHandle file = ExternalFiles.getFailsStateFile();

        om.writeValue(file.file(), state);
    }

    public void persistHistory(History history) throws IOException {
        FileHandle file = ExternalFiles.getHistoryFile();

        om.writeValue(file.file(), history);
    }

    public void persistSave(Save save) throws IOException {
        FileHandle file = ExternalFiles.getSaveFile(save.id);

        if (save.story != null) {
            StoryScenario storyScenario = save.story.last();
            if (storyScenario != null) {
                save.chapter = storyScenario.scenario.chapter;
            }
        }

        om.writeValue(file.file(), save);
    }

    private History loadHistory() throws IOException {
        FileHandle file = ExternalFiles.getHistoryFile();

        History history;
        if (!file.exists()) {
            history = new History();
        } else {
            history = om.readValue(file.file(), History.class);
        }

        return history;
    }

    private MenuState loadMenuState() throws IOException {

        FileHandle file = ExternalFiles.getMenuStateFile();

        MenuState state;
        if (!file.exists()) {
            state = new MenuState();
        } else {
            state = om.readValue(file.file(), MenuState.class);
        }

        return state;
    }

    private FailsState loadFailsState() throws IOException {

        FileHandle file = ExternalFiles.getFailsStateFile();

        FailsState state;
        if (!file.exists()) {
            state = new FailsState();
        } else {
            state = om.readValue(file.file(), FailsState.class);
        }

        return state;
    }

    private GalleryState loadGalleryState() throws IOException {

        FileHandle file = ExternalFiles.getGalleryStateFile();

        GalleryState state;
        if (!file.exists()) {
            state = new GalleryState();
        } else {
            state = om.readValue(file.file(), GalleryState.class);
        }

        return state;
    }

    private void loadActiveSave(GameState state) throws IOException {
        Save save = loadSave(state.history.activeSaveId);

        state.setActiveSave(save);
    }

    public Save loadSave(String id) throws IOException {
        Save save;

        FileHandle saveFile = ExternalFiles.getSaveFile(id);
        if (saveFile.exists()) {
            save = om.readValue(saveFile.file(), Save.class);
        } else {
            save = new Save(id);
        }

        return save;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<TimerScenario> loadTimerScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, TimerScenario.class, Files.getTimerScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Timer2Scenario> loadTimer2Scenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, Timer2Scenario.class, Files.getTimer2ScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GeneralsScenario> loadGeneralsScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", GeneralsStoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, GeneralsScenario.class, Files.getGeneralsScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<WauScenario> loadWauwauScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", WauStoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, WauScenario.class, Files.getWauwauScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<CannonsScenario> loadCannonsScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", CannonsStoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, CannonsScenario.class, Files.getCannonsScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HareScenario> loadHareScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, HareScenario.class, Files.getHareScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PictureScenario> loadPictureScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
        json.setElementType(Scenario.class, "translations", ScenarioTranslation.class);

        return json.fromJson(ArrayList.class, PictureScenario.class, Files.getPictureScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HireScenario> loadServantsHireScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "images", HireStoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);

        return json.fromJson(ArrayList.class, HireScenario.class, Files.getServantsHireScenarioFile());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Scenario> loadExternalScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "name", StoryAudio.class);
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
