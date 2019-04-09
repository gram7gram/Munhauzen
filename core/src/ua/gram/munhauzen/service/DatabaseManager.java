package ua.gram.munhauzen.service;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Item;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionAudio;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DatabaseManager {

    private final String tag = getClass().getSimpleName();

    public void load(GameState state) {

        state.imageRegistry = loadImages();
        Log.i(tag, "Loaded images x" + state.imageRegistry.size);

        state.audioRegistry = loadAudio();
        Log.i(tag, "Loaded audio x" + state.audioRegistry.size);

        state.itemRegistry = loadItems();
        Log.i(tag, "Loaded items x" + state.itemRegistry.size);

        state.optionRegistry = loadOptions();
        Log.i(tag, "Loaded options x" + state.optionRegistry.size);

        state.history = loadHistory();
        Log.i(tag, "Loaded history");
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
    private Array<Option> loadOptions() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setElementType(Option.class, "decisions", Decision.class);
        json.setElementType(Option.class, "images", OptionImage.class);
        json.setElementType(Option.class, "audio", OptionAudio.class);
        json.setElementType(Option.class, "backgroundAudio", OptionAudio.class);

        FileHandle file = Files.getOptionsFile();

        return json.fromJson(Array.class, Option.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Image> loadImages() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = Files.getImagesFile();

        return json.fromJson(Array.class, Audio.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Audio> loadAudio() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = Files.getAudioFile();

        return json.fromJson(Array.class, Audio.class, file);
    }

    @SuppressWarnings("unchecked")
    private Array<Item> loadItems() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        FileHandle file = Files.getItemsFile();

        return json.fromJson(Array.class, Item.class, file);
    }
}
