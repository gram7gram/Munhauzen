package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioService implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    public final AssetManager assetManager;
    public final HashMap<String, StoryAudio> activeAudio;

    public AudioService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new AssetManager(new ExternalFileHandleResolver());
        activeAudio = new HashMap<>();
    }

    public void prepare(StoryAudio item, Timer.Task onComplete) {
        if (item.isPrepared && item.player != null) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        Audio audio = AudioRepository.find(gameScreen.game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(audio);
        if (!file.exists()) {
            throw new GdxRuntimeException("Audio file does not exist " + audio.name + " at " + file.path());
        }

        String resource = file.path();

        boolean isLoaded = assetManager.isLoaded(resource, Music.class);

        if (!item.isPreparing) {

            if (!isLoaded) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                assetManager.load(resource, Music.class);
                return;
            }

        }

        if (isLoaded) {

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();
            item.player = assetManager.get(resource, Music.class);

            Timer.post(onComplete);
        }

    }

    public void prepareAndPlay(final StoryAudio item) {

        Log.i(tag, "play " + item.audio);

        Audio audio = AudioRepository.find(gameScreen.game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        final String resource = ExternalFiles.getExpansionAudioDir().path() + "/" + audio.file;

        assetManager.load(resource, Music.class);

        assetManager.finishLoading();

        item.player = assetManager.get(resource, Music.class);

        item.player.setVolume(GameState.isMute ? 0 : 1);

        item.player.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                try {
                    stop(item);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        item.player.play();

        activeAudio.put(item.audio, item);
    }

    public void onPrepared(StoryAudio item) {
        try {
            if (!item.isLocked) return;
            if (GameState.isPaused) return;
            if (item.isActive) return;

            Log.i(tag, "onPrepared " + item.audio
                    + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

            item.prepareCompletedAt = null;
            item.prepareStartedAt = null;

            playAudio(item);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void playAudio(StoryAudio item) {

        try {
            if (GameState.isPaused) return;

            float delay = Math.max(0, (item.progress - item.startsAt) / 1000);

            item.player.setPosition(delay);
            item.player.setVolume(GameState.isMute ? 0 : 1);

            Log.i(tag, "playAudio " + item.audio
                    + " with delay=" + item.player.getPosition() + "s"
                    + " duration=" + (item.duration / 1000) + "s"
                    + " volume=" + item.player.getVolume());

            item.isActive = true;
            item.player.play();

            activeAudio.put(item.audio, item);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void stop(StoryAudio storyAudio) {
        try {

            if (storyAudio.player != null)
                storyAudio.player.stop();

            Audio audio = AudioRepository.find(gameScreen.game.gameState, storyAudio.audio);

            final String resource = ExternalFiles.getExpansionAudioDir().path() + "/" + audio.file;

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
            Story story = gameScreen.getStory();

            for (StoryScenario option : story.scenarios) {
                for (StoryAudio audio : option.scenario.audio) {
                    if (audio.player != null) {
                        audio.isActive = false;
                        audio.player.pause();
                    }
                }
            }

            for (StoryAudio audio : activeAudio.values()) {
                if (audio.player != null) {
                    audio.isActive = false;
                    audio.player.pause();
                }
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
            Story story = gameScreen.getStory();
            if (story == null) return;

            int volume = GameState.isMute ? 0 : 1;

            for (StoryScenario option : story.scenarios) {
                for (StoryAudio audio : option.scenario.audio) {
                    if (audio.player != null) {
                        if (audio.player.getVolume() != volume) {
                            audio.player.setVolume(volume);
                        }
                    }
                }
            }

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

    public void updateMusicState() {
        try {
            Story story = gameScreen.getStory();
            if (story == null) return;

            if (story.isCompleted) {
                stop();
                return;
            }

            for (StoryScenario option : story.scenarios) {
                for (StoryAudio audio : option.scenario.audio) {
                    if (audio.player != null) {
                        if (GameState.isPaused) {
                            audio.isActive = false;
                            audio.player.pause();
                        } else {
                            if (audio.isActive && !audio.isLocked) {
                                audio.isActive = false;
                                audio.player.pause();
                            }
                        }
                    }
                }
            }

            for (StoryAudio audio : activeAudio.values()) {
                if (audio.player != null) {
                    if (GameState.isPaused) {
                        audio.isActive = false;
                        audio.player.pause();
                    } else {
                        if (audio.isActive && !audio.isLocked) {
                            audio.isActive = false;
                            audio.player.pause();
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void update() {
        assetManager.update();
    }

    @Override
    public void dispose() {
        try {
            assetManager.clear();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
