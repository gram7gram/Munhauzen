package ua.gram.munhauzen.service;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

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
import ua.gram.munhauzen.entity.Story;
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
import ua.gram.munhauzen.utils.JSON;
import ua.gram.munhauzen.utils.Log;

public class DatabaseManager {

    private final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public DatabaseManager(MunhauzenGame game) {
        this.game = game;
    }

    public synchronized ExpansionResponse loadExpansionInfo() {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);
        if (!file.exists()) return null;

        String raw = file.readString("UTF-8");
        if (raw == null || raw.equals("")) {
            Log.e(tag, "Expansion info exists but invalid");
            return null;
        }

        return loadExpansionInfo(raw);
    }

    public synchronized ExpansionResponse loadExpansionInfo(String raw) {

        try {

            if (raw != null && !raw.equals("")) {

                ExpansionResponse result = ua.gram.munhauzen.utils.JSON.parse(raw, ExpansionResponse.class);

                if (result != null) {
                    return result;
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        throw new GdxRuntimeException("Invalid expansion info");
    }

    public synchronized void loadExternal(GameState state) {

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

        try {
            state.expansionInfo = loadExpansionInfo();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            state.imageRegistry = loadExternalImages();
        } catch (Throwable e) {
            Log.e(tag, e);

            state.imageRegistry = new ArrayList<>();
        }

        try {
            state.audioRegistry = loadExternalAudio();
        } catch (Throwable e) {
            Log.e(tag, e);

            state.audioRegistry = new ArrayList<>();
        }

        try {
            state.audioFailRegistry = loadExternalAudioFails();
        } catch (Throwable e) {
            Log.e(tag, e);

            state.audioFailRegistry = new ArrayList<>();
        }

        try {
            state.inventoryRegistry = loadExternalInventory();
        } catch (Throwable e) {
            Log.e(tag, e);

            state.inventoryRegistry = new ArrayList<>();
        }

        try {
            state.scenarioRegistry = loadExternalScenario();
        } catch (Throwable e) {
            Log.e(tag, e);

            state.scenarioRegistry = new ArrayList<>();
        }

        try {
            state.chapterRegistry = loadExternalChapters();
        } catch (Throwable e) {
            Log.e(tag, e);

            state.chapterRegistry = new ArrayList<>();
        }
    }

    public synchronized void persistSync(final GameState gameState) {

//        try {
//            Log.i(tag, "STATE BEFORE\n"
//                    + JSON.stringifyAsString(gameState.activeSave.story));
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }

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

    public synchronized void persistExpansionInfo(ExpansionResponse state) {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);

        if (state != null)
            ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistAchievementState(AchievementState state) {

        FileHandle file = ExternalFiles.getAchievementStateFile(game.params);

        if (state != null)
            ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistGalleryState(GalleryState state) {

        FileHandle file = ExternalFiles.getGalleryStateFile(game.params);

        if (state != null)
            ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistMenuState(MenuState state) {
        FileHandle file = ExternalFiles.getMenuStateFile(game.params);

        if (state != null)
            ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistFailsState(FailsState state) {
        FileHandle file = ExternalFiles.getFailsStateFile(game.params);

        if (state != null)
            ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistHistory(History history) {
        FileHandle file = ExternalFiles.getHistoryFile(game.params);

        ua.gram.munhauzen.utils.JSON.stringify(file.file(), history);
    }

    public synchronized void persistPurchaseState(PurchaseState state) {
        FileHandle file = ExternalFiles.getPurchaseStateFile(game.params);

        ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistPreferences(GamePreferences state) {
        FileHandle file = ExternalFiles.getGamePreferencesFile(game.params);

        ua.gram.munhauzen.utils.JSON.stringify(file.file(), state);
    }

    public synchronized void persistSave(Save save) {
        FileHandle file = ExternalFiles.getSaveFile(game.params, save.id);

        if (save.story != null) {
            StoryScenario storyScenario = save.story.last();
            if (storyScenario != null) {
                save.chapter = storyScenario.scenario.chapter;
            }

            persistStory(save.story);
        }

        ua.gram.munhauzen.utils.JSON.stringify(file.file(), save);
    }

    public synchronized void persistStory(Story story) {
        FileHandle file = ExternalFiles.getStoryFile(game.params);

        if (story.isValid())
            ua.gram.munhauzen.utils.JSON.stringify(file.file(), story);
    }

    private synchronized History loadHistory() {
        FileHandle file = ExternalFiles.getHistoryFile(game.params);

        History state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
//            Log.e(tag, "history content\n" + content);

            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, History.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), History.class);
            }
        }

        if (state == null) {
            state = new History();
        }

        return state;
    }

    private synchronized void loadActiveSave(GameState state) {
        Save save = null;

        FileHandle file = ExternalFiles.getActiveSaveFile(game.params);
        if (file.exists()) {
            String content = file.readString("UTF-8");
//            Log.e(tag, "save content\n" + content);

            if (content != null && !content.equals("")) {
                save = ua.gram.munhauzen.utils.JSON.parse(content, Save.class);
            } else {
                save = ua.gram.munhauzen.utils.JSON.parse(file.file(), Save.class);
            }
        }

        if (save == null) {
            save = new Save();
        }

        state.setActiveSave(save);

        loadActiveStory(save);
    }

    private synchronized void loadActiveStory(Save save) {
        Story story = null;

        FileHandle file = ExternalFiles.getStoryFile(game.params);
        if (file.exists()) {
            String content = file.readString("UTF-8");

            if (content != null && !content.equals("")) {
                story = ua.gram.munhauzen.utils.JSON.parse(content, Story.class);
            } else {
                story = ua.gram.munhauzen.utils.JSON.parse(file.file(), Story.class);
            }
        }

        save.story = story;
    }

    private synchronized MenuState loadMenuState() {

        FileHandle file = ExternalFiles.getMenuStateFile(game.params);

        MenuState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, MenuState.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), MenuState.class);
            }
        }

        if (state == null) {
            state = new MenuState();
        }

        return state;
    }

    private synchronized FailsState loadFailsState() {

        FileHandle file = ExternalFiles.getFailsStateFile(game.params);

        FailsState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, FailsState.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), FailsState.class);
            }
        }

        if (state == null) {
            state = new FailsState();
        }

        return state;
    }

    private synchronized AchievementState loadAchievementState() {

        FileHandle file = ExternalFiles.getAchievementStateFile(game.params);

        AchievementState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, AchievementState.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), AchievementState.class);
            }
        }

        if (state == null) {
            state = new AchievementState();
        }

        return state;
    }

    private synchronized GalleryState loadGalleryState() {

        FileHandle file = ExternalFiles.getGalleryStateFile(game.params);

        GalleryState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, GalleryState.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), GalleryState.class);
            }
        }

        if (state == null) {
            state = new GalleryState();
        }

        return state;
    }

    private synchronized PurchaseState loadPurchaseState() {

        FileHandle file = ExternalFiles.getPurchaseStateFile(game.params);

        PurchaseState state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, PurchaseState.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), PurchaseState.class);
            }
        }

        if (state == null) {
            state = new PurchaseState();
        }

        return state;
    }

    private synchronized GamePreferences loadPreferences() {

        FileHandle file = ExternalFiles.getGamePreferencesFile(game.params);

        GamePreferences state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, GamePreferences.class);
            } else {
                state = ua.gram.munhauzen.utils.JSON.parse(file.file(), GamePreferences.class);
            }
        }

        if (state == null) {
            state = new GamePreferences();
        }

        return state;
    }

    public synchronized Save loadSave(String id) {

        FileHandle file = ExternalFiles.getSaveFile(game.params, id);

        Save state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
            if (content != null && !content.equals("")) {
                state = ua.gram.munhauzen.utils.JSON.parse(content, Save.class);
            } else {
                state = JSON.parse(file.file(), Save.class);
            }
        }

        if (state == null) {
            state = new Save(id);
        }

        return state;
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<TimerScenario> loadTimerScenario() {

        FileHandle file = Files.getTimerScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(TimerScenario.class, "decisions", Decision.class);
        json.setElementType(TimerScenario.class, "images", StoryImage.class);
        json.setElementType(TimerScenario.class, "audio", StoryAudio.class);
        json.setElementType(TimerScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, TimerScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<Timer2Scenario> loadTimer2Scenario() {

        FileHandle file = Files.getTimer2ScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Timer2Scenario.class, "decisions", Decision.class);
        json.setElementType(Timer2Scenario.class, "images", StoryImage.class);
        json.setElementType(Timer2Scenario.class, "audio", StoryAudio.class);
        json.setElementType(Timer2Scenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, Timer2Scenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<GeneralsScenario> loadGeneralsScenario() {

        FileHandle file = Files.getGeneralsScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(GeneralsScenario.class, "decisions", Decision.class);
        json.setElementType(GeneralsScenario.class, "images", GeneralsStoryImage.class);
        json.setElementType(GeneralsScenario.class, "audio", StoryAudio.class);
        json.setElementType(GeneralsScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, GeneralsScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<WauScenario> loadWauwauScenario() {

        FileHandle file = Files.getWauwauScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(WauScenario.class, "decisions", Decision.class);
        json.setElementType(WauScenario.class, "images", WauStoryImage.class);
        json.setElementType(WauScenario.class, "audio", StoryAudio.class);
        json.setElementType(WauScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, WauScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<CannonsScenario> loadCannonsScenario() {

        FileHandle file = Files.getCannonsScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(CannonsScenario.class, "decisions", Decision.class);
        json.setElementType(CannonsScenario.class, "images", CannonsStoryImage.class);
        json.setElementType(CannonsScenario.class, "audio", StoryAudio.class);
        json.setElementType(CannonsScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, CannonsScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<HareScenario> loadHareScenario() {

        FileHandle file = Files.getHareScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(HareScenario.class, "decisions", Decision.class);
        json.setElementType(HareScenario.class, "audio", StoryAudio.class);
        json.setElementType(HareScenario.class, "translations", Translation.class);

        return json.fromJson(ArrayList.class, HareScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<PictureScenario> loadPictureScenario() {

        FileHandle file = Files.getPictureScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(PictureScenario.class, "decisions", Decision.class);
        json.setElementType(PictureScenario.class, "images", StoryImage.class);
        json.setElementType(PictureScenario.class, "audio", StoryAudio.class);

        return json.fromJson(ArrayList.class, PictureScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<HireScenario> loadServantsHireScenario() {

        FileHandle file = Files.getServantsHireScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(HireScenario.class, "images", HireStoryImage.class);
        json.setElementType(HireScenario.class, "audio", StoryAudio.class);

        return json.fromJson(ArrayList.class, HireScenario.class, file);
    }

    @SuppressWarnings("unchecked")
    private synchronized ArrayList<Scenario> loadExternalScenario() {

        FileHandle file = Files.getScenarioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Scenario.class, "decisions", Decision.class);
        json.setElementType(Scenario.class, "images", StoryImage.class);
        json.setElementType(Scenario.class, "audio", StoryAudio.class);

        return json.fromJson(ArrayList.class, Scenario.class, file);
    }

    @SuppressWarnings("unchecked")
    private synchronized ArrayList<Chapter> loadExternalChapters() {

        FileHandle file = Files.getChaptersFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        return json.fromJson(ArrayList.class, Chapter.class, file);
    }

    @SuppressWarnings("unchecked")
    private synchronized ArrayList<Image> loadExternalImages() {

        FileHandle file = Files.getImagesFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        return json.fromJson(ArrayList.class, Image.class, file);
    }

    @SuppressWarnings("unchecked")
    private synchronized ArrayList<Audio> loadExternalAudio() {

        FileHandle file = Files.getAudioFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        return json.fromJson(ArrayList.class, Audio.class, file);
    }

    @SuppressWarnings("unchecked")
    private synchronized ArrayList<AudioFail> loadExternalAudioFails() {

        FileHandle file = Files.getAudioFailsFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        return json.fromJson(ArrayList.class, AudioFail.class, file);
    }

    @SuppressWarnings("unchecked")
    private synchronized ArrayList<Inventory> loadExternalInventory() {

        FileHandle file = Files.getInventoryFile();
        if (!file.exists()) return null;

        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        return json.fromJson(ArrayList.class, Inventory.class, file);
    }
}
