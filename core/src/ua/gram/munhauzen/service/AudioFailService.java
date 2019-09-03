package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioFailRepository;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

public class AudioFailService implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    public ExpansionAssetManager assetManager;
    public final HashMap<String, StoryAudio> activeAudio;

    public AudioFailService(MunhauzenGame game) {
        this.game = game;
        assetManager = new ExpansionAssetManager();
        activeAudio = new HashMap<>();
    }

    public void prepareAndPlay(final StoryAudio item) {

        Log.i(tag, "play " + item.audio);

        AudioFail audio = AudioFailRepository.find(game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(audio);
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

        activeAudio.put(item.audio, item);
    }

    public void stop(StoryAudio storyAudio) {
        try {

//            Log.i(tag, "stop " + storyAudio.audio);

            if (storyAudio.player != null)
                storyAudio.player.stop();

            String resource = storyAudio.resource;
            if (resource == null) {
                AudioFail audio = AudioFailRepository.find(game.gameState, storyAudio.audio);

                FileHandle file = ExternalFiles.getExpansionAudio(audio);
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

    public void stop() {
        try {
            for (StoryAudio audio : activeAudio.values()) {
                stop(audio);
            }

            activeAudio.clear();

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

            for (StoryAudio audio : activeAudio.values()) {
                if (audio.player != null) {
                    if (audio.player.getVolume() != volume) {
                        audio.player.setVolume(volume);
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void update() {

        if (assetManager == null) return;

        assetManager.update();

        updateVolume();
    }

    @Override
    public void dispose() {
        stop();

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }
    }
}
