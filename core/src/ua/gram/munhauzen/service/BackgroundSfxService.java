package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

public class BackgroundSfxService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    StoryAudio activeAudio;
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
        index = new Random().between(-1, sfx.length);
    }

    public void start() {
        Log.i(tag, "start");

        isPlaying = true;

        ++index;

        if (index >= sfx.length) index = 0;

        prepareAndPlay(sfx[index]);
    }

    public void stop() {
        Log.i(tag, "stop");

        isPlaying = false;

        final float duration = 2;
        float interval = .05f;
        final float step = 1f * interval / duration;

        Timer.instance().scheduleTask(new Timer.Task() {

            float progress = 0;

            private void complete() {
                dispose();
                cancel();
            }

            @Override
            public void run() {

                progress += step;

                try {
                    if (activeAudio != null) {
                        float volume = activeAudio.player.getVolume() - progress;
                        if (volume > 0) {
                            activeAudio.player.setVolume(volume);
                        } else {
                            complete();
                        }
                    } else {
                        complete();
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                    complete();
                }

            }

        }, 0, interval);
    }

    private void prepareAndPlay(String sfx) {

        try {
            if (game.expansionAssetManager == null) return;
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
                        game.expansionAssetManager.load(audio.file, Music.class);

                        game.expansionAssetManager.finishLoading();

                        Music sound = game.expansionAssetManager.get(audio.file, Music.class);
                        sound.play();

                        activeAudio.player = sound;

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {

                                dispose();

                                if (isPlaying) {
                                    start();
                                }
                            }
                        });

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        //game.onCriticalError(e);
                    }

                }
            }).start();

        } catch (Throwable e) {
            Log.e(tag, e);

            //game.onCriticalError(e);
        }

    }

    public void update() {
        if (activeAudio != null && activeAudio.player != null && isPlaying) {
            activeAudio.player.setVolume(GameState.isMute ? 0 : 1);
        }
    }

    public void dispose() {
        Log.i(tag, "dispose");

        if (activeAudio != null) {
            dispose(activeAudio);
            activeAudio = null;
        }
    }

    public void dispose(StoryAudio storyAudio) {

        if (storyAudio.player != null) {
            storyAudio.player.stop();
            storyAudio.player = null;
        }

        if (game.expansionAssetManager != null) {
            game.expansionAssetManager.unload(storyAudio.resource);
        }
    }
}
