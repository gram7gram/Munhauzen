package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.OptionAudio;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioOption;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioService {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;

    public AudioService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void prepare(OptionAudio item, Timer.Task onComplete) {
        if (item.isPrepared) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        String resource = item.getResource();

        if (!item.isPreparing) {

            if (!gameScreen.assetManager.isLoaded(resource, Music.class)) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                gameScreen.assetManager.load(resource, Music.class);
            }

        } else {

            if (gameScreen.assetManager.isLoaded(resource, Music.class)) {

                item.isPreparing = false;
                item.isPrepared = true;
                item.prepareCompletedAt = new Date();
                item.player = gameScreen.assetManager.get(resource, Music.class);

                Timer.post(onComplete);
            }
        }
    }

    public void onPrepared(OptionAudio item) {

        if (!item.isLocked) return;
        if (GameState.isPaused) return;

        Log.i(tag, "onPrepared " + item.id
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        playAudio(item);
    }

    public void playAudio(OptionAudio item) {

        if (GameState.isPaused) return;

        float delay = Math.max(0, (item.progress - item.startsAt) / 1000);

        item.player.setPosition(delay);
        item.player.setVolume(GameState.isMute ? 0 : 1);

        Log.i(tag, "playAudio " + item.id
                + " with delay=" + item.player.getPosition() + "s"
                + " duration=" + (item.duration / 1000) + "s"
                + " volume=" + item.player.getVolume());

        item.isActive = true;
        item.player.play();
    }

    public void stop() {
        Scenario scenario = gameScreen.getScenario();

        for (ScenarioOption option : scenario.options) {
            for (OptionAudio audio : option.option.audio) {
                if (audio.player != null) {
                    audio.isActive = false;
                    audio.player.pause();
                }
            }
        }
    }

    public void updateVolume() {
        Scenario scenario = gameScreen.getScenario();

        for (ScenarioOption option : scenario.options) {
            for (OptionAudio audio : option.option.audio) {
                if (audio.player != null) {
                    audio.player.setVolume(GameState.isMute ? 0 : 1);
                }
            }
        }
    }

    public void updateMusicState() {
        Scenario scenario = gameScreen.getScenario();

        if (scenario.isCompleted) {
            stop();
            return;
        }

        for (ScenarioOption option : scenario.options) {
            for (OptionAudio audio : option.option.audio) {
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
