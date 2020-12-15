package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class SfxService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final InternalAssetManager internalAssetManager;
    final InternalAssetManager independantAssetManager;

    public SfxService(MunhauzenGame game) {
        this.game = game;
        internalAssetManager = new InternalAssetManager();
        independantAssetManager = new InternalAssetManager();
    }

    public void load() {
        internalAssetManager.load("audio/sfx_button_main.aac", Sound.class);
    }

    public void onAnyBtnClicked() {
        playImmediately("sfx_button_main");
    }

    public void onLogoScreenOpened() {
        prepareAndPlayInternal("sfx_logo");
    }

    public void onGoofsSwitchClickedForMunhauzen() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_goofs_BM_1",
                "sfx_menu_goofs_BM_2"
        }));
    }

    public void onGoofsSwitchClickedForDaughter() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_goofs_BD_1",
                "sfx_menu_goofs_BD_2",
                "sfx_menu_goofs_BD_3",
                "sfx_menu_goofs_BD_4",
                "sfx_menu_goofs_BD_5"
        }));
    }

    public void onGalleryArrowClick() {
        prepareAndPlay("sfx_button_arrow");
    }

    public StoryAudio onDemoBannerShown() {
        return prepareAndPlay("sfx_menu_buy");
    }

    public void onTimerBombExploded() {
        prepareAndPlay("sfx_inter_bam");
    }

    public void onAnyDisabledBtnClicked() {
        prepareAndPlay("sfx_button_off");
    }

    public void onBookmarkDown() {
        prepareAndPlay("sfx_bookmark_down");
    }

    public void onBookmarkUp() {
        prepareAndPlay("sfx_bookmark_up");
    }

    public void onDecisionClicked() {
        prepareAndPlay("sfx_cannon");
    }

    public void onProgressScroll() {
        prepareAndPlayIndependant("sfx_audio_scroll_start", true, false);
        prepareAndPlay("sfx_audio_scroll_end", true, true);
    }

    public void onProgressPause() {
        prepareAndPlay("sfx_pause");
    }

    public void onProgressPlay() {
        prepareAndPlay("sfx_play");
    }

    public void onProgressSkip() {
        onAnyBtnClicked();
    }

    public void onListItemClicked() {
        prepareAndPlay("sfx_button_list");
    }

    public StoryAudio onGreetingBannerShown() {
        return prepareAndPlay("sfx_menu_intro");
    }

    public StoryAudio onPurchasePart1() {
        return prepareAndPlay("sfx_menu_demo");
    }

    public StoryAudio onPurchasePart2() {
        return prepareAndPlay("sfx_menu_buy");
    }

    public void onAllImagesUnlocked() {
        prepareAndPlay("sfx_menu_win_gallery");
    }

    public void onAllGoofsUnlocked() {
        prepareAndPlay("sfx_menu_win_gallery");
    }

    public StoryAudio onAllGoofsAndImagesUnlocked() {
        return prepareAndPlay("sfx_menu_win_all");
    }

    public void onAllMenuInventoryUnlocked() {
        prepareAndPlay("sfx_menu_win_items");
    }

    public StoryAudio onPurchaseSuccess() {
        return prepareAndPlayInternal(MathUtils.random(new String[]{
                "sfx_menu_thanks_1",
                "sfx_menu_thanks_2",
                "sfx_menu_thanks_3",
                "sfx_menu_thanks_4",
                "sfx_menu_thanks_5",
                "sfx_menu_thanks_6"
        }));
    }

    public void onLoadingVisited() {
        prepareAndPlayInternal(MathUtils.random(new String[]{
                "sfx_menu_downloading_1",
                "sfx_menu_downloading_2",
                "sfx_menu_downloading_3",
                "sfx_menu_downloading_4",
                "sfx_menu_downloading_5"
        }));
    }

    public void onSoundEnabled() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_sound_on_1", "sfx_menu_sound_on_2",
                "sfx_menu_sound_on_3", "sfx_menu_sound_on_4",
                "sfx_menu_sound_on_5"
        }), false, false);
    }

    public void onSoundDisabled() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_sound_off_1", "sfx_menu_sound_off_2",
                "sfx_menu_sound_off_3", "sfx_menu_sound_off_4",
                "sfx_menu_sound_off_5"
        }), false, false);
    }

    public StoryAudio onShareBannerShown() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_share_1",
                "sfx_menu_share_2",
                "sfx_menu_share_3",
                "sfx_menu_share_4"
        }));
    }

    public StoryAudio onRateBannerShown() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_rate_1",
                "sfx_menu_rate_2",
                "sfx_menu_rate_3",
                "sfx_menu_rate_4"
        }));
    }

    public StoryAudio onThankYouBannerShown() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_rate_1",
                "sfx_menu_rate_2",
                "sfx_menu_rate_3",
                "sfx_menu_rate_4"
        }));
    }

    public StoryAudio onProBannerShown() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_thanks",
                "sfx_menu_thanks_1",
                "sfx_menu_thanks_2",
                "sfx_menu_thanks_3",
                "sfx_menu_thanks_4",
                "sfx_menu_thanks_5",
                "sfx_menu_thanks_6"
        }));
    }

    public void onMenuContinueClicked() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_continue_1",
                "sfx_menu_continue_2",
                "sfx_menu_continue_3",
                "sfx_menu_continue_4"
        }), true, false);
    }

    public void onMenuStartClicked() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_new_1", "sfx_menu_new_2",
                "sfx_menu_new_3", "sfx_menu_new_4"
        }), true, false);
    }

    public StoryAudio onExitClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_exit_question_1", "sfx_menu_exit_question_2",
                "sfx_menu_exit_question_3", "sfx_menu_exit_question_4",
                "sfx_menu_exit_question_5"
        }));
    }

    public StoryAudio onExitYesClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_exit_1", "sfx_menu_exit_2",
                "sfx_menu_exit_3", "sfx_menu_exit_4",
                "sfx_menu_exit_yes_1", "sfx_menu_exit_yes_2"
        }));
    }

    public StoryAudio onExitNoClicked() {
        return prepareAndPlay("sfx_menu_save_no");
    }

    public void onMenuGalleryClicked() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_gallery_1", "sfx_menu_gallery_1"
        }), true, false);
    }

    public void onMenuGoofsClicked() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_goofs_1", "sfx_menu_goofs_2",
                "sfx_menu_goofs_3", "sfx_menu_goofs_4",
                "sfx_menu_goofs_5"
        }), true, false);
    }

    public void onMenuSaveClicked() {
        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_save_1", "sfx_menu_save_2",
                "sfx_menu_save_3"
        }), true, false);
    }

    public void onMenuAuthorsClicked() {
        prepareAndPlayIndependant("sfx_menu_credits", true, false);
    }

    public void onBackToMenuClicked() {
        prepareAndPlayIndependant("sfx_button_back", true, false);

        prepareAndPlayIndependant(MathUtils.random(new String[]{
                "sfx_menu_back_1",
                "sfx_menu_back_2",
                "sfx_menu_back_3",
                "sfx_menu_back_4",
                "sfx_menu_back_BD"
        }), true, false);
    }

    public void onOnePuzzleItemCombined() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_inter_puzzle_one_1",
                "sfx_inter_puzzle_one_2",
                "sfx_inter_puzzle_one_3"
        }));
    }

    public void onTwoPuzzleItemsCombined() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_inter_puzzle_two_1",
                "sfx_inter_puzzle_two_2",
                "sfx_inter_puzzle_two_3"
        }));
    }

    public void onNoInternet() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_connection_fail_BD_1",
                "sfx_menu_connection_fail_BD_2",
                "sfx_menu_connection_fail_BD_3",
                "sfx_menu_connection_fail_BD_4",
                "sfx_menu_connection_fail_BD_5",
        }));
    }

    private StoryAudio prepareAndPlay(String sfx) {
        return prepareAndPlay(sfx, true, false);
    }

    private void prepareAndPlayIndependant(final String sfx, boolean checkVolume, final boolean loop) {

        try {
            if (checkVolume && GameState.isMute) return;

            final Audio audio = AudioRepository.find(game.gameState, sfx);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        independantAssetManager.load(audio.file, Music.class);

                        independantAssetManager.finishLoading();

                        final Music sound = independantAssetManager.get(audio.file, Music.class);
                        sound.setLooping(loop);

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                try {
                                    independantAssetManager.unload(audio.file);
                                } catch (Throwable ignore) {
                                }
                            }
                        });

                        sound.play();

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                }
            }).start();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private StoryAudio prepareAndPlay(String sfx, boolean checkVolume, final boolean loop) {

        try {
            if (checkVolume && GameState.isMute) {
                return null;
            }

            final Audio audio = AudioRepository.find(game.gameState, sfx);

            final StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = sfx;
            storyAudio.duration = audio.duration;
            storyAudio.resource = audio.file;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        internalAssetManager.load(audio.file, Music.class);

                        internalAssetManager.finishLoading();

                        Music sound = internalAssetManager.get(audio.file, Music.class);
                        sound.setLooping(loop);
                        sound.play();

                        storyAudio.player = sound;

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                dispose(storyAudio);
                            }
                        });

                        game.currentSfx = storyAudio;

                    } catch (Throwable ignore) {
                    }

                }
            }).start();

            return storyAudio;

        } catch (Throwable ignore) {
        }

        return null;
    }

    private void playImmediately(String sfx) {
        try {
            if (GameState.isMute) return;

            String file = "audio/" + sfx + ".mp3";

            if (!Gdx.files.internal(file).exists()) {
                file = "audio/" + sfx + ".aac";

                if (!Gdx.files.internal(file).exists()) {
                    Log.e(tag, "Sfx not found in assets: " + sfx);
                    return;
                }
            }

            Sound sound = internalAssetManager.get(file, Sound.class);
            sound.play();

        } catch (Throwable ignore) {
        }
    }

    private StoryAudio prepareAndPlayInternal(String sfx) {

        try {
            if (GameState.isMute) return null;

            String file = "audio/" + sfx + ".mp3";

            if (!Gdx.files.internal(file).exists()) {
                file = "audio/" + sfx + ".aac";

                if (!Gdx.files.internal(file).exists()) {
                    Log.e(tag, "Sfx not found in assets: " + sfx);
                    return null;
                }
            }

            internalAssetManager.load(file, Music.class);

            internalAssetManager.finishLoading();

            Music sound = internalAssetManager.get(file, Music.class);

            final StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = sfx;
            storyAudio.resource = file;
            storyAudio.player = sound;

            sound.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    dispose(storyAudio);
                }
            });

            sound.play();

            game.currentSfx = storyAudio;

            return storyAudio;

        } catch (Throwable ignore) {
        }

        return null;
    }

    public void update() {

        try {
            internalAssetManager.update();
        } catch (Throwable ignore) {
        }

        try {
            independantAssetManager.update();
        } catch (Throwable ignore) {
        }

        updateVolume();
    }

    public void updateVolume() {
        try {

            Array<Music> audio1 = new Array<>();
            internalAssetManager.getAll(Music.class, audio1);

            for (Music item : audio1) {
                item.setVolume(GameState.isMute ? 0 : 1);
            }

            Array<Sound> audio2 = new Array<>();
            internalAssetManager.getAll(Sound.class, audio2);

            for (Sound item : audio2) {
                try {
                    item.setVolume(0, GameState.isMute ? 0 : 1);
                } catch (Throwable ignore) {
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            if (game.currentSfx != null) {
                if (game.currentSfx.player != null) {
                    game.currentSfx.player.setVolume(GameState.isMute ? 0 : 1);
                }
            }
        } catch (Throwable ignore) {
        }
    }

    public void stop() {
        Log.i(tag, "stop");

        try {

            Array<Music> audio1 = new Array<>();
            internalAssetManager.getAll(Music.class, audio1);

            for (Music item : audio1) {
                item.stop();
            }

            Array<Sound> audio2 = new Array<>();
            internalAssetManager.getAll(Sound.class, audio2);

            for (Sound item : audio2) {
                item.stop();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void dispose() {
        Log.e(tag, "dispose");
        try {
            stop();

            internalAssetManager.dispose();
            independantAssetManager.dispose();

        } catch (Throwable ignore) {
        }
    }

    public void dispose(StoryAudio storyAudio) {

        try {
            if (storyAudio.player != null) {
                storyAudio.player.stop();
                storyAudio.player = null;
            }
        } catch (Throwable ignore) {
        }
    }

    public void onLoadOptionClicked() {
        prepareAndPlay("sfx_menu_download_question");
    }

    public void onLoadOptionYesClicked() {
        prepareAndPlay("sfx_menu_download_yes");
    }

    public void onLoadOptionNoClicked() {
        prepareAndPlay("sfx_menu_save_no");
    }

    public void onSaveOptionClicked() {
        prepareAndPlay("sfx_menu_save_question");
    }

    public void onSaveOptionYesClicked() {
        prepareAndPlay("sfx_menu_save_yes");
    }

    public void onSaveOptionNoClicked() {
        prepareAndPlay("sfx_menu_save_no");
    }

    public void onAchievementUnlocked() {
        prepareAndPlay("sfx_fanfare");
    }

    public void onReferralOpened() {
        prepareAndPlay("sfx_menu_invite_7");
    }

    public void onGameModeSelect() {
        try {
            if (GameState.isMute) {
                return;
            }

            final Audio audio = AudioRepository.find(game.gameState, "sfx_menu_mode_select");

            final StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = "sfx_menu_mode_select";
            storyAudio.duration = audio.duration;
            storyAudio.resource = audio.file;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        internalAssetManager.load(audio.file, Music.class);

                        internalAssetManager.finishLoading();

                        Music sound = internalAssetManager.get(audio.file, Music.class);
                        sound.play();

                        storyAudio.player = sound;

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                dispose(storyAudio);
                                onGameModeSelectDescription();
                            }
                        });

                        game.currentSfx = storyAudio;

                    } catch (Throwable ignore) {
                    }

                }
            }).start();

        } catch (Throwable ignore) {
        }
    }

    public void onGameModeSelectDescription() {
        prepareAndPlay("sfx_menu_mode_description");
    }

    public void onGameModeSelectInMenu() {
        try {
            if (GameState.isMute) {
                return;
            }

            final Audio audio = AudioRepository.find(game.gameState, "sfx_menu_mode_menu");

            final StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = "sfx_menu_mode_menu";
            storyAudio.duration = audio.duration;
            storyAudio.resource = audio.file;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        internalAssetManager.load(audio.file, Music.class);

                        internalAssetManager.finishLoading();

                        Music sound = internalAssetManager.get(audio.file, Music.class);
                        sound.play();

                        storyAudio.player = sound;

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                dispose(storyAudio);
                                onGameModeSelectDescription();
                            }
                        });

                        game.currentSfx = storyAudio;

                    } catch (Throwable ignore) {
                    }

                }
            }).start();

        } catch (Throwable ignore) {
        }
    }

    public StoryAudio onGameModeSwitch() {
        return prepareAndPlay("sfx_menu_mode_switch");
    }

    public StoryAudio onGameModeLeave() {
        return prepareAndPlay("sfx_menu_mode_leave");
    }
}
