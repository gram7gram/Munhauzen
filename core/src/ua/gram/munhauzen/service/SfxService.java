package ua.gram.munhauzen.service;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.repository.AudioRepository;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class SfxService {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public SfxService(MunhauzenGame game) {
        this.game = game;
    }

    public void onDemoBannerShown() {
        prepareAndPlay("sfx_menu_buy");
    }

    public void onAnyDisabledBtnClicked() {
        prepareAndPlay("sfx_button_off");
    }

    public void onAnyBtnClicked() {
        prepareAndPlay("sfx_button_main");
    }

    public void onListItemClicked() {
        prepareAndPlay("sfx_button_list");
    }

    public void onGreetingBannerShown() {
        prepareAndPlay("sfx_menu_hello");
    }

    public void onAllImagesUnlocked() {
        prepareAndPlay("sfx_menu_win_gallery");
    }

    public void onAllGoofsUnlocked() {
        prepareAndPlay("sfx_menu_win_gallery");
    }

    public void onAllGoofsAndImagesUnlocked() {
        prepareAndPlay("sfx_menu_win_all");
    }

    public void onFirstVisitToMenu() {
        prepareAndPlay("sfx_menu_intro", new Timer.Task() {
            @Override
            public void run() {
                prepareAndPlay("sfx_menu_intro_2");
            }
        });
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
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_sound_on_1", "sfx_menu_sound_on_2",
                "sfx_menu_sound_on_3", "sfx_menu_sound_on_4",
                "sfx_menu_sound_on_5"
        }));
    }

    public void onSoundDisabled() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_sound_off_1", "sfx_menu_sound_off_2",
                "sfx_menu_sound_off_3", "sfx_menu_sound_off_4",
                "sfx_menu_sound_off_5"
        }));
    }

    public void onShareBannerShown() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_share_1", "sfx_menu_share_2",
                "sfx_menu_share_3", "sfx_menu_share_4"
        }));
    }

    public void onRateBannerShown() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_rate_1", "sfx_menu_rate_2",
                "sfx_menu_rate_3", "sfx_menu_rate_4"
        }));
    }

    public void onProBannerShown() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_thanks", "sfx_menu_thanks_1",
                "sfx_menu_thanks_2", "sfx_menu_thanks_3",
                "sfx_menu_thanks_4", "sfx_menu_thanks_5",
                "sfx_menu_thanks_6"
        }));
    }

    public void onMenuContinueClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_continue_1",
                "sfx_menu_continue_2",
                "sfx_menu_continue_3",
                "sfx_menu_continue_4"
        }));
    }

    public void onMenuStartClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_new_1", "sfx_menu_new_2",
                "sfx_menu_new_3", "sfx_menu_new_4"
        }));
    }

    public void onExitClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_exit_question_1", "sfx_menu_exit_question_2",
                "sfx_menu_exit_question_3", "sfx_menu_exit_question_4",
                "sfx_menu_exit_question_5"
        }));
    }

    public int onExitYesClicked() {
        return prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_exit_1", "sfx_menu_exit_2",
                "sfx_menu_exit_3", "sfx_menu_exit_4",
                "sfx_menu_exit_yes_1", "sfx_menu_exit_yes_2"
        }));
    }

    public void onExitNoClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_stay_1", "sfx_menu_stay_2",
                "sfx_menu_stay_3", "sfx_menu_stay_4"
        }));
    }

    public void onMenuGalleryClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_gallery_1", "sfx_menu_gallery_1"
        }));
    }

    public void onMenuGoofsClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_goofs_1", "sfx_menu_goofs_2",
                "sfx_menu_goofs_3", "sfx_menu_goofs_4",
                "sfx_menu_goofs_5"
        }));
    }

    public void onMenuSaveClicked() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_save_1", "sfx_menu_save_2",
                "sfx_menu_save_3"
        }));
    }

    public void onBackToMenuClicked() {
        prepareAndPlay("sfx_button_back");

        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_menu_back_1",
                "sfx_menu_back_2",
                "sfx_menu_back_3",
                "sfx_menu_back_4",
                "sfx_menu_back_BD"
        }));
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

    public void onThreePuzzleItemCombined() {
        prepareAndPlay(MathUtils.random(new String[]{
                "sfx_inter_puzzle_three_1",
                "sfx_inter_puzzle_three_2",
                "sfx_inter_puzzle_three_3"
        }));

    }

    private int prepareAndPlay(String sfx) {
        return prepareAndPlay(sfx, null);
    }

    private int prepareAndPlay(String sfx, final Timer.Task onComplete) {

        try {
            if (game.expansionAssetManager == null) return 0;

            final Audio audio = AudioRepository.find(game.gameState, sfx);

            game.expansionAssetManager.load(audio.file, Music.class);

            game.expansionAssetManager.finishLoading();

            Music sound = game.expansionAssetManager.get(audio.file, Music.class);
            sound.play();

            sound.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    if (game.expansionAssetManager != null) {
                        game.expansionAssetManager.unload(audio.file);

                        if (onComplete != null) {
                            Timer.post(onComplete);
                        }
                    }
                }
            });

            return audio.duration;

        } catch (Throwable e) {
            Log.e(tag, e);

            //game.onCriticalError(e);
        }

        return 0;
    }

    private void prepareAndPlayInternal(String sfx) {

        try {
            if (game.assetManager == null) return;

            final String file = "audio/" + sfx + ".mp3";

            game.assetManager.load(file, Music.class);

            game.assetManager.finishLoading();

            Music sound = game.assetManager.get(file, Music.class);
            sound.play();

            sound.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    if (game.assetManager != null) {
                        game.assetManager.unload(file);
                    }
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);

            game.onCriticalError(e);
        }
    }
}
