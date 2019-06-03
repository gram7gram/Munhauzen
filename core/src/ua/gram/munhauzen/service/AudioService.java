package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioService {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;

    public AudioService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void prepare(StoryAudio item, Timer.Task onComplete) {
        if (item.isPrepared) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        ArrayList<String> randomResources = new ArrayList<>();
        randomResources.add("audio/audio1.jpg");
        randomResources.add("audio/audio2.jpg");
        randomResources.add("audio/audio3.jpg");
        randomResources.add("audio/audio4.jpg");
        randomResources.add("audio/audio5.jpg");
        randomResources.add("audio/audio6.jpg");
        randomResources.add("audio/audio7.jpg");

        String resource = MathUtils.random(randomResources); //item.getResource();

        boolean isLoaded = gameScreen.assetManager.isLoaded(resource, Music.class);

        if (!item.isPreparing) {

            if (!isLoaded) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                gameScreen.assetManager.load(resource, Music.class);
                return;
            }

        }

        if (isLoaded) {

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();
            item.player = gameScreen.assetManager.get(resource, Music.class);

            Timer.post(onComplete);
        }

    }

    public void onPrepared(StoryAudio item) {

        if (!item.isLocked) return;
        if (GameState.isPaused) return;

        Log.i(tag, "onPrepared " + item.audio
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        playAudio(item);
    }

    public void playAudio(StoryAudio item) {

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
    }

    public void stop() {
        Story story = gameScreen.getStory();

        for (StoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                if (audio.player != null) {
                    audio.isActive = false;
                    audio.player.pause();
                }
            }
        }
    }

    public void updateVolume() {
        Story story = gameScreen.getStory();

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
    }

    public void updateMusicState() {
        Story story = gameScreen.getStory();

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
    }
}
