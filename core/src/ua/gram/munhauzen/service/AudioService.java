package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioService implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    public ExpansionAssetManager assetManager;
    public final HashMap<String, StoryAudio> activeAudio;

    public AudioService(MunhauzenGame game) {
        this.game = game;
        assetManager = new ExpansionAssetManager(game);
        activeAudio = new HashMap<>();
    }

    public void prepare(StoryAudio item, Timer.Task onComplete) {
        if (item.isPrepared && item.player != null) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        Audio audio = AudioRepository.find(game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(game.params, audio);
        if (!file.exists()) {
            throw new GdxRuntimeException("Audio file does not exist " + audio.name + " at " + file.path());
        }

        item.resource = file.path();

        boolean isLoaded = assetManager.isLoaded(item.resource, Music.class);

        if (!isLoaded) {
            if (!item.isPreparing) {

                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                assetManager.load(item.resource, Music.class);
            }
        } else {

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();
            item.player = assetManager.get(item.resource, Music.class);

            Timer.post(onComplete);
        }

    }

    public void prepareAndPlay(final StoryAudio item) {

        Log.i(tag, "play " + item.audio);

        Audio audio = AudioRepository.find(game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(game.params, audio);
        if (!file.exists()) {
            throw new GdxRuntimeException("Audio file does not exist " + audio.name + " at " + file.path());
        }

        try {
            final String resource = file.path();

            assetManager.load(resource, Music.class);

            assetManager.finishLoading();

            item.player = assetManager.get(resource, Music.class);

            item.player.setVolume(GameState.isMute ? 0 : 1);

            item.player.play();

            activeAudio.put(item.audio, item);

        } catch (Throwable ignore) {
        }
    }

    public void stop(StoryAudio storyAudio) {
        try {

            Log.i(tag, "stop " + storyAudio.audio);

            if (storyAudio.player != null)
                storyAudio.player.stop();

            String resource = storyAudio.resource;
            if (resource == null) {
                Audio audio = AudioRepository.find(game.gameState, storyAudio.audio);

                FileHandle file = ExternalFiles.getExpansionAudio(game.params, audio);
                if (!file.exists()) {
                    throw new GdxRuntimeException("Audio file does not exist " + audio.name + " at " + file.path());
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

    public void pause() {
        try {
            for (StoryAudio audio : activeAudio.values()) {
                pause(audio);
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void pause(StoryAudio audio) {

        if (audio == null) return;
        try {
            audio.isActive = false;

            if (audio.player != null) {
                audio.player.pause();
            }

        } catch (Throwable ignore) {
        }
    }

    public void updateVolume(StoryAudio audio) {

        int volume = GameState.isMute ? 0 : 1;
        try {
            if (audio.player != null) {
                if (audio.player.getVolume() != volume) {
                    audio.player.setVolume(volume);
                }
            }

        } catch (Throwable ignore) {
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

        try {
            assetManager.update();
        } catch (Throwable ignore) {
        }

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
