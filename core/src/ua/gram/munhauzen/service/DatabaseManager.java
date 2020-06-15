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
import ua.gram.munhauzen.utils.Log;

public class DatabaseManager {

    private final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    public final ObjectMapper om;

    public DatabaseManager(MunhauzenGame game) {
        this.game = game;
        om = new ObjectMapper();
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

                ExpansionResponse result = om.readValue(raw, ExpansionResponse.class);

                if (result != null) {

                    return result;

//                    if (result.version == game.params.expansionVersion) {
//                        return result;
//                    } else {
//                        Log.e(tag, "Obsolete expansion info v" + result.version);
//                        return null;
//                    }
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        throw new GdxRuntimeException("Invalid expansion info");
    }

    public synchronized void loadExternal(GameState state) {

//        try {
//            Log.i(tag, "STATE AFTER\n"
//                    + om.writeValueAsString(game.inventoryService.getAllInventory()));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

//        Log.i(tag, "loadExternal");

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
//                    + om.writeValueAsString(gameState.activeSave.story));
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

    public synchronized void persistExpansionInfo(ExpansionResponse state) throws IOException {

        FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public synchronized void persistAchievementState(AchievementState state) throws IOException {

        FileHandle file = ExternalFiles.getAchievementStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public synchronized void persistGalleryState(GalleryState state) throws IOException {

        FileHandle file = ExternalFiles.getGalleryStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public synchronized void persistMenuState(MenuState state) throws IOException {
        FileHandle file = ExternalFiles.getMenuStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public synchronized void persistFailsState(FailsState state) throws IOException {
        FileHandle file = ExternalFiles.getFailsStateFile(game.params);

        if (state != null)
            om.writeValue(file.file(), state);
    }

    public synchronized void persistHistory(History history) throws IOException {
        FileHandle file = ExternalFiles.getHistoryFile(game.params);

        om.writeValue(file.file(), history);
    }

    public synchronized void persistPurchaseState(PurchaseState state) throws IOException {
        FileHandle file = ExternalFiles.getPurchaseStateFile(game.params);

        om.writeValue(file.file(), state);
    }

    public synchronized void persistPreferences(GamePreferences state) throws IOException {
        FileHandle file = ExternalFiles.getGamePreferencesFile(game.params);

        om.writeValue(file.file(), state);
    }

    public synchronized void persistSave(Save save) throws IOException {
        FileHandle file = ExternalFiles.getSaveFile(game.params, save.id);

        if (save.story != null) {
            StoryScenario storyScenario = save.story.last();
            if (storyScenario != null) {
                save.chapter = storyScenario.scenario.chapter;
            }

            persistStory(save.story);
        }

        om.writeValue(file.file(), save);
    }

    public synchronized void persistStory(Story story) throws IOException {
        FileHandle file = ExternalFiles.getStoryFile(game.params);

        if (story.isValid())
            om.writeValue(file.file(), story);
    }

    private synchronized History loadHistory() throws IOException {
        FileHandle file = ExternalFiles.getHistoryFile(game.params);

        History state = null;
        if (file.exists()) {
            String content = file.readString("UTF-8");
//            Log.e(tag, "history content\n" + content);

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

    private synchronized void loadActiveSave(GameState state) throws IOException {
        Save save = null;

        FileHandle file = ExternalFiles.getActiveSaveFile(game.params);
        if (file.exists()) {
            String content = file.readString("UTF-8");
//            Log.e(tag, "save content\n" + content);

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

        loadActiveStory(save);
    }

    private synchronized void loadActiveStory(Save save) throws IOException {
        Story story = null;

        FileHandle file = ExternalFiles.getStoryFile(game.params);
        if (file.exists()) {
            String content = file.readString("UTF-8");

            if (content != null && !content.equals("")) {
                story = om.readValue(content, Story.class);
            } else {
                story = om.readValue(file.file(), Story.class);
            }
        }

        save.story = story;
    }

    private synchronized MenuState loadMenuState() throws IOException {

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

    private synchronized FailsState loadFailsState() throws IOException {

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

    private synchronized AchievementState loadAchievementState() throws IOException {

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

    private synchronized GalleryState loadGalleryState() throws IOException {

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

    private synchronized PurchaseState loadPurchaseState() throws IOException {

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

    private synchronized GamePreferences loadPreferences() throws IOException {

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

    public synchronized Save loadSave(String id) throws IOException {

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
