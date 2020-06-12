package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioFailRepository;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

public class AudioFailService {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    final ExpansionAssetManager assetManager;
    public final HashMap<String, Music> activeAudio;

    public AudioFailService(MunhauzenGame game) {
        this.game = game;
        assetManager = new ExpansionAssetManager(game);
        activeAudio = new HashMap<>();
    }

    public void prepareAndPlay(final StoryAudio item) {

        Log.i(tag, "play " + item.audio);

        AudioFail audio = AudioFailRepository.find(game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(game.params, audio);
        if (!file.exists()) {
            Log.e(tag, "Audio file does not exist " + audio.name + " at " + file.path());
            return;
        }

        final String resource = file.path();

        assetManager.load(resource, Music.class);

        assetManager.finishLoading();

        item.player = assetManager.get(resource, Music.class);

        item.player.setVolume(GameState.isMute ? 0 : 1);

        item.player.play();

        activeAudio.put(item.audio, item.player);
    }

    public void stop(StoryAudio storyAudio) {
        try {

//            Log.i(tag, "stop " + storyAudio.audio);

            if (storyAudio.player != null)
                storyAudio.player.stop();

            String resource = storyAudio.resource;
            if (resource == null) {
                AudioFail audio = AudioFailRepository.find(game.gameState, storyAudio.audio);

                FileHandle file = ExternalFiles.getExpansionAudio(game.params, audio);
                if (!file.exists()) {
                    Log.e(tag, "Audio file does not exist " + audio.name + " at " + file.path());
                    return;
                }

                resource = file.path();
            }

            if (assetManager.isLoaded(resource)) {
                assetManager.unload(resource);
            }

            storyAudio.isPrepared = false;
            storyAudio.isPreparing = false;
            storyAudio.isActive = false;
            storyAudio.isLocked = false;
            storyAudio.player = null;

            activeAudio.remove(storyAudio.audio);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void stop(String resource, Music audio) {
        try {

            if (audio == null) return;

            audio.stop();

            if (assetManager.isLoaded(resource)) {
                assetManager.unload(resource);
            }

            activeAudio.remove(resource);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void stop() {
        try {
            for (String audio : activeAudio.keySet()) {
                stop(audio, activeAudio.get(audio));
            }

            activeAudio.clear();

            assetManager.clear();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateVolume(StoryAudio audio) {

        int volume = GameState.isMute ? 0 : 1;

        if (audio.player != null) {
            if (audio.player.getVolume() != volume) {
                audio.player.setVolume(volume);
            }
        }
    }

    public void updateVolume() {
        try {

            int volume = GameState.isMute ? 0 : 1;

            for (Music audio : activeAudio.values()) {
                if (audio != null) {
                    if (audio.getVolume() != volume) {
                        audio.setVolume(volume);
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void update() {

        if (assetManager == null) return;

        try {
            assetManager.update();
        } catch (Throwable ignore) {
        }

        updateVolume();
    }

    public void dispose() {
        stop();

        assetManager.dispose();
    }
}
