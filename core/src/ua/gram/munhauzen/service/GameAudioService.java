package ua.gram.munhauzen.service;

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
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.cannons.CannonsStory;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryScenario;
import ua.gram.munhauzen.interaction.hare.HareStory;
import ua.gram.munhauzen.interaction.hare.HareStoryScenario;
import ua.gram.munhauzen.interaction.picture.PictureStory;
import ua.gram.munhauzen.interaction.picture.PictureStoryScenario;
import ua.gram.munhauzen.interaction.timer.TimerStory;
import ua.gram.munhauzen.interaction.timer.TimerStoryScenario;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.interaction.wauwau.WauStoryScenario;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameAudioService implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    public final ExpansionAssetManager assetManager;
    public final HashMap<String, StoryAudio> activeAudio;

    public GameAudioService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new ExpansionAssetManager();
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

        Audio audio = AudioRepository.find(gameScreen.game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(audio);
        if (!file.exists()) {
            throw new GdxRuntimeException("Audio file does not exist " + audio.name + " at " + file.path());
        }

        final String resource = file.path();

        assetManager.load(resource, Music.class);

        assetManager.finishLoading();

        item.player = assetManager.get(resource, Music.class);

        item.player.setVolume(GameState.isMute ? 0 : 1);

//        item.player.setOnCompletionListener(new Music.OnCompletionListener() {
//            @Override
//            public void onCompletion(Music music) {
//                Log.i(tag, "onCompletion " + resource);
//                try {
//                    stop(item);
//                } catch (Throwable e) {
//                    Log.e(tag, e);
//                }
//            }
//        });

        item.player.play();

        activeAudio.put(item.audio, item);
    }

    public void onPrepared(StoryAudio item) {
        try {
            if (!item.isLocked) return;
            if (GameState.isPaused) return;
            if (item.isActive) return;

            long diff = DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS);

            Log.i(tag, "onPrepared " + item.audio + " in " + diff + "ms");

            item.prepareCompletedAt = null;
            item.prepareStartedAt = null;

            playAudio(item);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void playAudio(StoryAudio item) {

        try {
            if (item == null) return;
            if (GameState.isPaused) return;

            float delay = Math.max(0, (item.progress - item.startsAt) / 1000);

            item.player.setPosition(delay);
            item.player.setVolume(GameState.isMute ? 0 : 1);

            Log.i(tag, "playAudio " + item.audio + " duration=" + (item.duration / 1000) + "s");

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

            String resource = storyAudio.resource;
            if (resource == null) {
                Audio audio = AudioRepository.find(gameScreen.game.gameState, storyAudio.audio);

                FileHandle file = ExternalFiles.getExpansionAudio(audio);
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
            Story story = gameScreen.getStory();
            if (story != null) {

                for (StoryScenario option : story.scenarios) {
                    for (StoryAudio audio : option.scenario.audio) {
                        stop(audio);
                    }
                }
            }

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
            Story story = gameScreen.getStory();
            if (story != null) {

                for (StoryScenario option : story.scenarios) {
                    for (StoryAudio audio : option.scenario.audio) {
                        pause(audio);
                    }
                }
            }

            for (StoryAudio audio : activeAudio.values()) {
                pause(audio);
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void pause(StoryAudio audio) {

        if (audio == null) return;

        audio.isActive = false;

        if (audio.player != null) {
            audio.player.pause();
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

            Story story = gameScreen.getStory();
            if (story != null) {

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

    public void updateMusicState(Story story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (StoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (StoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.player != null) {

                                        if (!audio.player.isPlaying()) {
                                            playAudio(audio);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateMusicState(HareStory story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (HareStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (HareStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.isPrepared
                                            && !audio.player.isPlaying()) {
                                        audio.player.play();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateMusicState(CannonsStory story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (CannonsStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (CannonsStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.isPrepared
                                            && !audio.player.isPlaying()) {
                                        audio.player.play();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateMusicState(PictureStory story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (PictureStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (PictureStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.isPrepared
                                            && !audio.player.isPlaying()) {
                                        audio.player.play();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateMusicState(TimerStory story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (TimerStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (TimerStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.isPrepared
                                            && !audio.player.isPlaying()) {
                                        audio.player.play();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateMusicState(GeneralsStory story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (GeneralsStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (GeneralsStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.isPrepared
                                            && !audio.player.isPlaying()) {
                                        audio.player.play();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateMusicState(WauStory story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop();
                } else {

                    if (GameState.isPaused) {
                        for (WauStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (WauStoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    if (audio.isCompleted) {
                                        audio.player.pause();
                                    }

                                    if (audio.isActive && !audio.isLocked) {
                                        audio.isActive = false;
                                        audio.player.pause();
                                    }

                                    if (!audio.isCompleted && audio.isActive
                                            && audio.isLocked && audio.isPrepared
                                            && !audio.player.isPlaying()) {
                                        audio.player.play();
                                    }
                                }
                            }
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

        updateVolume();

        Story story = gameScreen.getStory();
        if (story != null) {

            updateMusicState(story);

            if (story.currentInteraction != null) {
                if (story.currentInteraction.interaction instanceof TimerInteraction) {
                    TimerStory interactionStory = ((TimerInteraction) story.currentInteraction.interaction)
                            .storyManager.timerStory;

                    updateMusicState(interactionStory);
                }

                if (story.currentInteraction.interaction instanceof WauInteraction) {
                    WauStory interactionStory = ((WauInteraction) story.currentInteraction.interaction)
                            .storyManager.story;

                    updateMusicState(interactionStory);
                }

                if (story.currentInteraction.interaction instanceof GeneralsInteraction) {
                    GeneralsStory interactionStory = ((GeneralsInteraction) story.currentInteraction.interaction)
                            .storyManager.generalsStory;

                    updateMusicState(interactionStory);
                }

                if (story.currentInteraction.interaction instanceof PictureInteraction) {
                    PictureStory interactionStory = ((PictureInteraction) story.currentInteraction.interaction)
                            .storyManager.pictureStory;

                    updateMusicState(interactionStory);
                }

                if (story.currentInteraction.interaction instanceof HareInteraction) {
                    HareStory interactionStory = ((HareInteraction) story.currentInteraction.interaction)
                            .storyManager.hareStory;

                    updateMusicState(interactionStory);
                }

                if (story.currentInteraction.interaction instanceof CannonsInteraction) {
                    CannonsStory interactionStory = ((CannonsInteraction) story.currentInteraction.interaction)
                            .storyManager.story;

                    updateMusicState(interactionStory);
                }
            }
        }
    }

    @Override
    public void dispose() {
        try {
            stop();

            assetManager.clear();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}