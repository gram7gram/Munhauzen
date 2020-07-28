package ua.gram.munhauzen.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashSet;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class SfxService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;
    final InternalAssetManager internalAssetManager;
    final ExpansionAssetManager expansionAssetManager;
    private final HashSet<StoryAudio> activeAudio = new HashSet<>();

    public SfxService(MunhauzenGame game) {
        this.game = game;
        internalAssetManager = new InternalAssetManager();
        expansionAssetManager = new ExpansionAssetManager(game);
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

    public StoryAudio onMenuContinueClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_continue_1",
                "sfx_menu_continue_2",
                "sfx_menu_continue_3",
                "sfx_menu_continue_4"
        }));
    }

    public StoryAudio onMenuStartClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_new_1", "sfx_menu_new_2",
                "sfx_menu_new_3", "sfx_menu_new_4"
        }));
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

    public StoryAudio onMenuGalleryClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_gallery_1", "sfx_menu_gallery_1"
        }));
    }

    public StoryAudio onMenuGoofsClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_goofs_1", "sfx_menu_goofs_2",
                "sfx_menu_goofs_3", "sfx_menu_goofs_4",
                "sfx_menu_goofs_5"
        }));
    }

    public StoryAudio onMenuSaveClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_save_1", "sfx_menu_save_2",
                "sfx_menu_save_3"
        }));
    }

    public StoryAudio onMenuAuthorsClicked() {
        return prepareAndPlay("sfx_menu_credits");
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

    private StoryAudio prepareAndPlay(String sfx) {
        return prepareAndPlay(sfx, true, false);
    }

    private void prepareAndPlayIndependant(final String sfx, boolean checkVolume, final boolean loop) {

        try {
            if (game.expansionAssetManager == null) return;

            if (checkVolume && GameState.isMute) return;

            final Audio audio = AudioRepository.find(game.gameState, sfx);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        if (game.expansionAssetManager == null) return;

                        game.expansionAssetManager.load(audio.file, Music.class);

                        game.expansionAssetManager.finishLoading();

                        Music sound = game.expansionAssetManager.get(audio.file, Music.class);
                        sound.setLooping(loop);

//                        final StoryAudio storyAudio = new StoryAudio();
//                        storyAudio.audio = sfx;
//                        storyAudio.duration = audio.duration;
//                        storyAudio.resource = audio.file;
//                        storyAudio.player = sound;

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
                        expansionAssetManager.load(audio.file, Music.class);

                        expansionAssetManager.finishLoading();

                        Music sound = expansionAssetManager.get(audio.file, Music.class);
                        sound.setLooping(loop);
                        sound.play();

                        storyAudio.player = sound;

                        sound.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                dispose(storyAudio);
                            }
                        });

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
            expansionAssetManager.update();
        } catch (Throwable ignore) {
        }

        try {
            for (StoryAudio storyAudio : activeAudio) {
                if (storyAudio.player != null) {
                    storyAudio.player.setVolume(GameState.isMute ? 0 : 1);
                }
            }

            if (game.currentSfx != null) {
                if (game.currentSfx.player != null) {
                    game.currentSfx.player.setVolume(GameState.isMute ? 0 : 1);
                }
            }
        } catch (Throwable ignore) {
        }
    }

    public void stop() {
        Log.e(tag, "stop");

        try {

            for (StoryAudio storyAudio : activeAudio) {
                dispose(storyAudio);
            }
            activeAudio.clear();

//        internalAssetManager.clear();
            expansionAssetManager.clear();
        } catch (Throwable ignore) {
        }
    }

    public void dispose() {
        Log.e(tag, "dispose");
        try {
            stop();

            internalAssetManager.dispose();
            expansionAssetManager.dispose();

        } catch (Throwable ignore) {
        }
    }

    public void dispose(StoryAudio storyAudio) {

        try {
            if (storyAudio.player != null) {
                storyAudio.player.stop();
                storyAudio.player = null;
            }

            activeAudio.remove(storyAudio);

            if (internalAssetManager.isLoaded(storyAudio.resource))
                internalAssetManager.unload(storyAudio.resource);

            if (expansionAssetManager.isLoaded(storyAudio.resource))
                expansionAssetManager.unload(storyAudio.resource);

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
}
