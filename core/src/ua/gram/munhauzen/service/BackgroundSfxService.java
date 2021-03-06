package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

public class BackgroundSfxService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    StoryAudio activeAudio;
    final InternalAssetManager internalAssetManager;
    public boolean isPlaying;

    int index;
    final String[] sfx = {
            "sfx_menu_music_1",
            "sfx_menu_music_2",
            "sfx_menu_music_3",
            "sfx_menu_music_4",
            "sfx_menu_music_5",
            "sfx_menu_music_6",
            "sfx_menu_music_7",
            "sfx_menu_music_8"
    };

    public BackgroundSfxService(MunhauzenGame game) {
        this.game = game;
        internalAssetManager = new InternalAssetManager();
    }

    public void start() {
        Log.i(tag, "start");

        isPlaying = true;

        index = new Random().between(0, sfx.length - 1);

        prepareAndPlay(sfx[index]);
    }

    public void fade() {
        Log.i(tag, "stop");

        isPlaying = false;

        final float duration = 2;
        float interval = .05f;
        final float step = 1f * interval / duration;

        Timer.instance().scheduleTask(new Timer.Task() {

            float progress = 0;

            private void complete() {
                stop();
                cancel();
            }

            @Override
            public void run() {

                progress += step;

                try {
                    if (activeAudio != null && activeAudio.player != null) {
                        float volume = activeAudio.player.getVolume() - progress;
                        if (volume > 0) {
                            activeAudio.player.setVolume(volume);
                        } else {
                            complete();
                        }
                    } else {
                        complete();
                    }
                } catch (Throwable ignore) {
                    complete();
                }

            }

        }, 0, interval);
    }

    private void prepareAndPlay(String sfx) {

        try {
            if (GameState.isMute) return;

            final Audio audio = AudioRepository.find(game.gameState, sfx);

            activeAudio = new StoryAudio();
            activeAudio.audio = sfx;
            activeAudio.duration = audio.duration;
            activeAudio.resource = audio.file;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        if (activeAudio == null || internalAssetManager == null) {
                            return;
                        }

                        internalAssetManager.load(audio.file, Music.class);

                        internalAssetManager.finishLoading();

                        Music sound = internalAssetManager.get(audio.file, Music.class);

                        if (sound == null) return;

                        sound.play();

                        activeAudio.player = sound;

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {

                                stop();

                                if (isPlaying) {
                                    start();
                                }
                            }
                        });

                    } catch (Throwable ignore) {
                    }

                }
            }).start();

        } catch (Throwable ignore) {
        }

    }

    public void update() {

        try {
            internalAssetManager.update();
        } catch (Throwable ignore) {
        }

        try {
            if (activeAudio != null && activeAudio.player != null && isPlaying) {
                activeAudio.player.setVolume(GameState.isMute ? 0 : 1);
            }

        } catch (Throwable ignore) {
        }
    }

    public void stop() {

        if (activeAudio != null) {
            dispose(activeAudio);
            activeAudio = null;
        }

        internalAssetManager.clear();

        isPlaying = false;
    }

    public void dispose() {
        Log.i(tag, "dispose");

        try {

            stop();

            internalAssetManager.dispose();

        } catch (Throwable ignore) {
        }
    }

    public void dispose(StoryAudio storyAudio) {

        try {
            if (storyAudio.player != null) {
                storyAudio.player.stop();
                storyAudio.player = null;
            }

            internalAssetManager.unload(storyAudio.resource);

        } catch (Throwable ignore) {
        }
    }
}
