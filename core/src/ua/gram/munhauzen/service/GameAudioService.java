package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.HashMap;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.Timer2Interaction;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.cannons.CannonsStory;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryManager;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryManager;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryScenario;
import ua.gram.munhauzen.interaction.hare.HareStory;
import ua.gram.munhauzen.interaction.hare.HareStoryManager;
import ua.gram.munhauzen.interaction.hare.HareStoryScenario;
import ua.gram.munhauzen.interaction.picture.PictureStory;
import ua.gram.munhauzen.interaction.picture.PictureStoryManager;
import ua.gram.munhauzen.interaction.picture.PictureStoryScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireStory;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryManager;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryScenario;
import ua.gram.munhauzen.interaction.timer.TimerStory;
import ua.gram.munhauzen.interaction.timer.TimerStoryManager;
import ua.gram.munhauzen.interaction.timer.TimerStoryScenario;
import ua.gram.munhauzen.interaction.timer2.Timer2Story;
import ua.gram.munhauzen.interaction.timer2.Timer2StoryManager;
import ua.gram.munhauzen.interaction.timer2.Timer2StoryScenario;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.interaction.wauwau.WauStoryManager;
import ua.gram.munhauzen.interaction.wauwau.WauStoryScenario;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

public class GameAudioService implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    public ExpansionAssetManager assetManager;
    public final HashMap<String, StoryAudio> activeAudio;

    public GameAudioService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new ExpansionAssetManager(gameScreen.game);
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

        FileHandle file = ExternalFiles.getExpansionAudio(gameScreen.game.params, audio);
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

    public void prepareAndPlay(StoryAudio item) {

        Log.i(tag, "play " + item.audio);

        Audio audio = AudioRepository.find(gameScreen.game.gameState, item.audio);
        if (item.duration == 0) {
            item.duration = audio.duration;
        }

        FileHandle file = ExternalFiles.getExpansionAudio(gameScreen.game.params, audio);
        if (!file.exists()) {
            throw new GdxRuntimeException("Audio file does not exist " + audio.name + " at " + file.path());
        }

        try {
            String resource = file.path();

            assetManager.load(resource, Music.class);

            assetManager.finishLoading();

            item.player = assetManager.get(resource, Music.class);

            item.player.setVolume(GameState.isMute ? 0 : 1);
            item.player.play();

            synchronized (activeAudio) {
                activeAudio.put(item.audio, item);
            }

        } catch (Throwable ignore) {
        }

        try {

            gameScreen.game.achievementService.onAudioListened(audio);

        } catch (GdxRuntimeException ignore) {
        }
    }

    public void playAudio(StoryAudio item) {

        if (item == null) return;
        if (!item.isLocked) return;
        if (item.isActive) return;

        playAudioImmediately(item);
    }

    public void playAudioImmediately(StoryAudio item) {

        try {
            if (item == null) return;

            if (item.player == null) {
                Log.e(tag, "Missing player in audio " + item.audio);
                return;
            }

            float delay = Math.max(0, (item.progress - item.startsAt) / 1000);

            item.player.setPosition(delay);
            item.player.setVolume(GameState.isMute ? 0 : 1);

            Log.i(tag, "playAudio " + item.audio + " duration=" + (item.duration / 1000) + "s");

            item.isActive = true;
            item.player.play();

            synchronized (activeAudio) {
                activeAudio.put(item.audio, item);
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {

            Audio audio = AudioRepository.find(gameScreen.game.gameState, item.audio);

            gameScreen.game.achievementService.onAudioListened(audio);

        } catch (GdxRuntimeException ignore) {
        }
    }

    public void stop(StoryAudio storyAudio) {
        try {

            if (storyAudio.player != null) {

                Log.i(tag, "stop " + storyAudio.audio);

                storyAudio.player.stop();
            }

            String resource = storyAudio.resource;
            if (resource == null) {
                Audio audio = AudioRepository.find(gameScreen.game.gameState, storyAudio.audio);

                FileHandle file = ExternalFiles.getExpansionAudio(gameScreen.game.params, audio);
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

            synchronized (activeAudio) {
                activeAudio.remove(storyAudio.audio);
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void stop(String referer) {

        Log.i(tag, "stop " + referer);

        try {
            Story story = gameScreen.getStory();
            if (story != null) {
                stop(story);
            }

            for (StoryAudio storyAudio : activeAudio.values()) {
                stop(storyAudio);
            }

            synchronized (activeAudio) {
                activeAudio.clear();
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void stop(Story story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (StoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }

    }

    public void stop(HireStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (HireStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(GeneralsStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (GeneralsStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(HareStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (HareStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(TimerStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (TimerStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(Timer2Story story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (Timer2StoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(PictureStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (PictureStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(CannonsStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (CannonsStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
        }
    }

    public void stop(WauStory story) {
        if (story == null) return;

//        Log.i(tag, "stop story " + story.id);

        for (WauStoryScenario option : story.scenarios) {
            for (StoryAudio audio : option.scenario.audio) {
                stop(audio);
            }
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

            synchronized (activeAudio) {
                for (StoryAudio audio : activeAudio.values()) {
                    pause(audio);
                }
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

            synchronized (activeAudio) {
                for (StoryAudio audio : activeAudio.values()) {
                    if (audio.player != null) {
                        if (audio.player.getVolume() != volume) {
                            audio.player.setVolume(volume);
                        }
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
                    stop(story);
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
                    stop(story);
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
                    stop(story);
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
                    stop(story);
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
                    stop(story);
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

    public void updateMusicState(Timer2Story story) {
        try {
            if (story != null) {

                if (story.isCompleted) {
                    stop(story);
                } else {

                    if (GameState.isPaused) {
                        for (Timer2StoryScenario option : story.scenarios) {
                            for (StoryAudio audio : option.scenario.audio) {
                                if (audio.player != null) {
                                    audio.isActive = false;
                                    audio.player.pause();
                                }
                            }
                        }
                    } else {

                        for (Timer2StoryScenario option : story.scenarios) {
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
                    stop(story);
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
                    stop(story);
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

    public void updateMusicState(HireStory story) {
        try {
            if (story == null) return;

            updateVolume(story);

            StoryAudio audio = story.currentScenario.currentAudio;

            if (story.isCompleted) {
                pause(audio);
            } else if (audio.player != null) {
                if (GameState.isPaused) {
                    if (!audio.player.isPlaying()) {
                        pause(audio);
                    }
                } else {
                    if (!audio.player.isPlaying()) {
                        playAudio(audio);
                    }
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void updateVolume(HireStory story) {
        try {
            if (story == null) return;

            for (HireStoryScenario option : story.scenarios) {
                if (option.scenario.audio != null) {
                    for (StoryAudio audio : option.scenario.audio) {
                        if (audio.player != null) {
                            audio.player.setVolume(GameState.isMute ? 0 : 1);
                        }
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

        Story story = gameScreen.getStory();
        if (story != null) {

            updateMusicState(story);

            if (story.currentInteraction != null) {
                if (story.currentInteraction.interaction instanceof TimerInteraction) {
                    TimerStoryManager storyManager = ((TimerInteraction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        TimerStory interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof Timer2Interaction) {
                    Timer2StoryManager storyManager = ((Timer2Interaction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        Timer2Story interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof WauInteraction) {
                    WauStoryManager storyManager = ((WauInteraction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        WauStory interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof GeneralsInteraction) {
                    GeneralsStoryManager storyManager = ((GeneralsInteraction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        GeneralsStory interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof PictureInteraction) {
                    PictureStoryManager storyManager = ((PictureInteraction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        PictureStory interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof HareInteraction) {
                    HareStoryManager storyManager = ((HareInteraction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        HareStory interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof CannonsInteraction) {
                    CannonsStoryManager storyManager = ((CannonsInteraction) story.currentInteraction.interaction).storyManager;

                    if (storyManager != null) {
                        CannonsStory interactionStory = storyManager.story;
                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }

                if (story.currentInteraction.interaction instanceof ServantsInteraction) {
                    HireStoryManager storyManager = ((ServantsInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null) {
                        HireStory interactionStory = storyManager.story;

                        if (interactionStory != null) {
                            updateMusicState(interactionStory);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        stop(tag);

        Story story = gameScreen.getStory();
        if (story != null) {

            dispose(story);

            if (story.currentInteraction != null) {
                if (story.currentInteraction.interaction instanceof TimerInteraction) {
                    TimerStoryManager storyManager = ((TimerInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof Timer2Interaction) {
                    Timer2StoryManager storyManager = ((Timer2Interaction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof WauInteraction) {
                    WauStoryManager storyManager = ((WauInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof GeneralsInteraction) {
                    GeneralsStoryManager storyManager = ((GeneralsInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof PictureInteraction) {
                    PictureStoryManager storyManager = ((PictureInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof HareInteraction) {
                    HareStoryManager storyManager = ((HareInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof CannonsInteraction) {
                    CannonsStoryManager storyManager = ((CannonsInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }

                if (story.currentInteraction.interaction instanceof ServantsInteraction) {
                    HireStoryManager storyManager = ((ServantsInteraction) story.currentInteraction.interaction).storyManager;
                    if (storyManager != null && storyManager.story != null) {
                        dispose(storyManager.story);
                    }
                }
            }
        }

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }
    }

    public synchronized void dispose(HireStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (HireStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(CannonsStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (CannonsStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(WauStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (WauStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(TimerStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (TimerStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(Timer2Story story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (Timer2StoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(HareStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (HareStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(PictureStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (PictureStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {

                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(GeneralsStory story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (GeneralsStoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {
                    dispose(item);
                }
            }
        }

    }

    public synchronized void dispose(Story story) {
        if (assetManager == null || story == null) return;

        Log.i(tag, "dispose " + story.id);

        for (StoryScenario storyScenario : story.scenarios) {

            if (storyScenario.scenario.audio != null) {
                for (StoryAudio item : storyScenario.scenario.audio) {
                    dispose(item);
                }
            }
        }

    }

    private synchronized void dispose(StoryAudio item) {

        stop(item);

        item.player = null;
        item.isPrepared = false;

        try {

            if (item.resource != null) {
                if (assetManager.isLoaded(item.resource)) {
                    if (assetManager.getReferenceCount(item.resource) == 0) {
                        assetManager.unload(item.resource);
                        item.resource = null;
                    } else {
                        Log.e(tag, item.audio + " dispose ignored");
                    }
                }
            }

        } catch (Throwable ignore) {
        }
    }
}
