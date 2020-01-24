package ua.gram.munhauzen.service;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AchievementState;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.FailsState;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.GamePreferences;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.History;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.entity.Translation;
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

        String raw = file.readString("UTF-8");
        if (raw == null || raw.equals("")) {
            Log.e(tag, "Expansion info exists but invalid");
            return null;
        }

        return loadExpansionInfo(raw);
    }

    public ExpansionResponse loadExpansionInfo(String raw) {

        try {

            if (raw != null && !raw.equals("")) {

                ExpansionResponse result = om.readValue(raw, ExpansionResponse.class);

                if (result != null) {
                    if (result.version == game.params.expansionVersion) {
                        return result;
                    } else {
                        Log.e(tag, "Obsolete expansion info v" + result.version);
                        return null;
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        throw new GdxRuntimeException("Invalid expansion info");
    }

    public void loadExternal(GameState state) {

        Log.i(tag, "loadExternal");

        try {
            state.history = loadHistory();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getHistoryFile(game.params).delete();

            state.history = new History();
        }

        try {
            loadActiveSave(state);
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getActiveSaveFile(game.params).delete();

            state.setActiveSave(new Save());
        }

        try {
            state.menuState = loadMenuState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getMenuStateFile(game.params).delete();

            state.menuState = new MenuState();
        }

        try {
            state.galleryState = loadGalleryState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getGalleryStateFile(game.params).delete();

            state.galleryState = new GalleryState();
        }

        try {
            state.purchaseState = loadPurchaseState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getPurchaseStateFile(game.params).delete();

            state.purchaseState = new PurchaseState();
        }

        try {
            state.preferences = loadPreferences();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getGamePreferencesFile(game.params).delete();

            state.preferences = new GamePreferences();
        }

        try {
            state.failsState = loadFailsState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getFailsStateFile(game.params).delete();

            state.failsState = new FailsState();
        }

        try {
            state.achievementState = loadAchievementState();
        } catch (Throwable e) {
            Log.e(tag, e);

            ExternalFiles.getFailsStateFile(game.params).delete();

            state.failsState = new FailsState();
        }

        if (state.expansionInfo == null) {
            state.expansionInfo = loadExpansionInfo();

            if (state.expansionInfo == null) {
                Log.e(tag, "No expansion info. Load canceled");
                return;
            }
        }

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
    }

    public void persist(final GameState gameState) {

        //Log.i(tag, "persist");

        if (gameState == null) return;

        if (gameState.purchaseState != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        persistPurchaseState(gameState.purchaseState);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }).start();

        if (gameState.preferences != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        persistPreferences(gameState.preferences);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }).start();

        if (gameState.history != null)
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

        if (gameState.activeSave != null)
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

        if (gameState.menuState != null)
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

        if (gameState.galleryState != null)
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

        if (gameState.failsState != null)
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

        if (gameState.expansionInfo != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        persistExpansionInfo(gameState.expansionInfo);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }).start();

        if (gameState.achievementState != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        persistAchievementState(gameState.achievementState);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }).start();
    }

    public void persistSync(final GameState gameState) {

        //Log.i(tag, "persist");

        if (gameState == null) return;

        if (gameState.purchaseState != null)
            try {
                persistPurchaseState(gameState.purchaseState);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.preferences != null)
            try {
                persistPreferences(gameState.preferences);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.history != null)
            try {
                persistHistory(gameState.history);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.activeSave != null)
            try {
                persistSave(gameState.activeSave);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.menuState != null)
            try {
                persistMenuState(gameState.menuState);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.galleryState != null)
            try {
                persistGalleryState(gameState.galleryState);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.failsState != null)
            try {
                persistFailsState(gameState.failsState);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.expansionInfo != null)
            try {
                persistExpansionInfo(gameState.expansionInfo);
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        if (gameState.achievementState != null)
            try {
                persistAchievementState(gameState.achievementState);
            } catch (Throwable e) {
                Log.e(tag, e);
            }
    }

    public void persistExpansionInfo(ExpansionResponse state) throws IOException {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public void persistAchievementState(AchievementState state) throws IOException {

        FileHandle file = ExternalFiles.getAchievementStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public void persistGalleryState(GalleryState state) throws IOException {

        FileHandle file = ExternalFiles.getGalleryStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public void persistMenuState(MenuState state) throws IOException {
        FileHandle file = ExternalFiles.getMenuStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public void persistFailsState(FailsState state) throws IOException {
        FileHandle file = ExternalFiles.getFailsStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public void persistHistory(History history) throws IOException {
        FileHandle file = ExternalFiles.getHistoryFile(game.params);

        om.writeValue(file.file(), history);
    }

    public void persistPurchaseState(PurchaseState state) throws IOException {
        FileHandle file = ExternalFiles.getPurchaseStateFile(game.params);

        om.writeValue(file.file(), state);
    }

    public void persistPreferences(GamePreferences state) throws IOException {
        FileHandle file = ExternalFiles.getGamePreferencesFile(game.params);

        om.writeValue(file.file(), state);
    }

    public void persistSave(Save save) throws IOException {
        FileHandle file = ExternalFiles.getSaveFile(game.params, save.id);

        if (save.story != null) {
            StoryScenario storyScenario = save.story.last();
            if (storyScenario != null) {
                save.chapter = storyScenario.scenario.chapter;
            }
        }

        om.writeValue(file.file(), save);
    }

    private History loadHistory() throws IOException {
        FileHandle file = ExternalFiles.getHistoryFile(game.params);

        History state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, History.class);
            } else {
                state = om.readValue(file.file(), History.class);
            }
        }

        if (state == null) {
            state = new History();
        }

        return state;
    }

    private MenuState loadMenuState() throws IOException {

        FileHandle file = ExternalFiles.getMenuStateFile(game.params);

        MenuState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, MenuState.class);
            } else {
                state = om.readValue(file.file(), MenuState.class);
            }
        }

        if (state == null) {
            state = new MenuState();
        }

        return state;
    }

    private FailsState loadFailsState() throws IOException {

        FileHandle file = ExternalFiles.getFailsStateFile(game.params);

        FailsState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, FailsState.class);
            } else {
                state = om.readValue(file.file(), FailsState.class);
            }
        }

        if (state == null) {
            state = new FailsState();
        }

        return state;
    }

    private AchievementState loadAchievementState() throws IOException {

        FileHandle file = ExternalFiles.getAchievementStateFile(game.params);

        AchievementState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, AchievementState.class);
            } else {
                state = om.readValue(file.file(), AchievementState.class);
            }
        }

        if (state == null) {
            state = new AchievementState();
        }

        return state;
    }

    private GalleryState loadGalleryState() throws IOException {

        FileHandle file = ExternalFiles.getGalleryStateFile(game.params);

        GalleryState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, GalleryState.class);
            } else {
                state = om.readValue(file.file(), GalleryState.class);
            }
        }

        if (state == null) {
            state = new GalleryState();
        }

        return state;
    }

    private PurchaseState loadPurchaseState() throws IOException {

        FileHandle file = ExternalFiles.getPurchaseStateFile(game.params);

        PurchaseState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, PurchaseState.class);
            } else {
                state = om.readValue(file.file(), PurchaseState.class);
            }
        }

        if (state == null) {
            state = new PurchaseState();
        }

        return state;
    }

    private GamePreferences loadPreferences() throws IOException {

        FileHandle file = ExternalFiles.getGamePreferencesFile(game.params);

        GamePreferences state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, GamePreferences.class);
            } else {
                state = om.readValue(file.file(), GamePreferences.class);
            }
        }

        if (state == null) {
            state = new GamePreferences();
        }

        return state;
    }

    private void loadActiveSave(GameState state) throws IOException {
        Save save = null;

        FileHandle file = ExternalFiles.getActiveSaveFile(game.params);
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                save = om.readValue(content, Save.class);
            } else {
                save = om.readValue(file.file(), Save.class);
            }
        }

        if (save == null) {
            save = new Save();
        }

        state.setActiveSave(save);
    }

    public Save loadSave(String id) throws IOException {

        FileHandle file = ExternalFiles.getSaveFile(game.params, id);

        Save state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = om.readValue(content, Save.class);
            } else {
                state = om.readValue(file.file(), Save.class);
            }
        }

        if (state == null) {
            state = new Save(id);
        }

        return state;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<TimerScenario> loadTimerScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(TimerScenario.class, "decisions", Decision.class);
        json.setElementType(TimerScenario.class, "images", StoryImage.class);
        json.setElementType(TimerScenario.class, "audio", StoryAudio.class);
        json.setElementType(TimerScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, TimerScenario.class, Files.getTimerScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Timer2Scenario> loadTimer2Scenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Timer2Scenario.class, "decisions", Decision.class);
        json.setElementType(Timer2Scenario.class, "images", StoryImage.class);
        json.setElementType(Timer2Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Timer2Scenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, Timer2Scenario.class, Files.getTimer2ScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GeneralsScenario> loadGeneralsScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(GeneralsScenario.class, "decisions", Decision.class);
        json.setElementType(GeneralsScenario.class, "images", GeneralsStoryImage.class);
        json.setElementType(GeneralsScenario.class, "audio", StoryAudio.class);
        json.setElementType(GeneralsScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, GeneralsScenario.class, Files.getGeneralsScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<WauScenario> loadWauwauScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(WauScenario.class, "decisions", Decision.class);
        json.setElementType(WauScenario.class, "images", WauStoryImage.class);
        json.setElementType(WauScenario.class, "audio", StoryAudio.class);
        json.setElementType(WauScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, WauScenario.class, Files.getWauwauScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<CannonsScenario> loadCannonsScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(CannonsScenario.class, "decisions", Decision.class);
        json.setElementType(CannonsScenario.class, "images", CannonsStoryImage.class);
        json.setElementType(CannonsScenario.class, "audio", StoryAudio.class);
        json.setElementType(CannonsScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, CannonsScenario.class, Files.getCannonsScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HareScenario> loadHareScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(HareScenario.class, "decisions", Decision.class);
        json.setElementType(HareScenario.class, "audio", StoryAudio.class);
        json.setElementType(HareScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, HareScenario.class, Files.getHareScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PictureScenario> loadPictureScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(PictureScenario.class, "decisions", Decision.class);
        json.setElementType(PictureScenario.class, "images", StoryImage.class);
        json.setElementType(PictureScenario.class, "audio", StoryAudio.class);

        return json.fromJson(ArrayList.class, PictureScenario.class, Files.getPictureScenarioFile());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HireScenario> loadServantsHireScenario() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(HireScenario.class, "images", HireStoryImage.class);
        json.setElementType(HireScenario.class, "audio", StoryAudio.class);

        return json.fromJson(ArrayList.class, HireScenario.class, Files.getServantsHireScenarioFile());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Scenario> loadExternalScenario() {

        FileHandle file = ExternalFiles.getScenarioFile(game.params);
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);

        return json.fromJson(ArrayList.class, Scenario.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Chapter> loadExternalChapters() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getChaptersFile(game.params);
        if (!file.exists()) return null;

        return json.fromJson(ArrayList.class, Chapter.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Image> loadExternalImages() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getImagesFile(game.params);
        if (!file.exists()) return null;

        return json.fromJson(ArrayList.class, Image.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Audio> loadExternalAudio() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getAudioFile(game.params);
        if (!file.exists()) return null;

        return json.fromJson(ArrayList.class, Audio.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<AudioFail> loadExternalAudioFails() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getAudioFailsFile(game.params);
        if (!file.exists()) return null;

        return json.fromJson(ArrayList.class, AudioFail.class, file);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Inventory> loadExternalInventory() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = ExternalFiles.getInventoryFile(game.params);
        if (!file.exists()) return null;

        return json.fromJson(ArrayList.class, Inventory.class, file);
    }
}
