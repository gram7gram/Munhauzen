package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.screen.fails.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.fails.fragment.FailsFragment;
import ua.gram.munhauzen.screen.fails.fragment.GoofsFragment;
import ua.gram.munhauzen.screen.fails.ui.AudioRow;
import ua.gram.munhauzen.screen.fails.ui.FailsLayers;
import ua.gram.munhauzen.service.AudioFailService;
import ua.gram.munhauzen.utils.Log;

public class FailsScreen extends AbstractScreen {

    public FailsLayers layers;
    public FailsFragment failsFragment;
    public ControlsFragment controlsFragment;
    public ArrayList<GalleryFail> failsM, failsD;
    public AudioFailService audioService;
    public GoofsFragment goofsFragment;

    public FailsScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        audioService = new AudioFailService(game);

        failsM = new ArrayList<>();
        failsD = new ArrayList<>();

        background = game.internalAssetManager.get("p1.jpg", Texture.class);

        assetManager.load("ui/playbar_play.png", Texture.class);
        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/b_sound_on.png", Texture.class);
        assetManager.load("ui/b_sound_off.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/b_closed_0.png", Texture.class);
        assetManager.load("gallery/b_opened_0.png", Texture.class);

        assetManager.load("fails/fv_switch_d.png", Texture.class);
        assetManager.load("fails/fv_switch_m.png", Texture.class);

        layers = new FailsLayers();

        ui.addActor(layers);
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            createFails();

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            controlsFragment.fadeIn();

            failsFragment = new FailsFragment(this);
            failsFragment.create();

            layers.setContentLayer(failsFragment);

            failsFragment.fadeIn();

            if (game.gameState.failsState != null) {
                if (!game.gameState.failsState.isGoofsBannerViewed) {
                    game.gameState.failsState.isGoofsBannerViewed = true;

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            openGoofsBanner();
                        }
                    }, .5f);
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openGoofsBanner() {
        try {

            goofsFragment = new GoofsFragment(this);
            goofsFragment.create();

            layers.setBannerLayer(goofsFragment);

            goofsFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void createFails() {

        for (AudioFail audioFail : game.gameState.audioFailRegistry) {

            StoryAudio storyAudio = new StoryAudio();
            storyAudio.audio = audioFail.name;

            GalleryFail fail = new GalleryFail();
            fail.storyAudio = storyAudio;

            fail.isOpened = game.gameState.history.openedFails.contains(audioFail.name);
            fail.isListened = game.gameState.failsState.listenedAudio.contains(audioFail.name);

            if (audioFail.isFailDaughter) {
                failsD.add(fail);
            } else {
                failsM.add(fail);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        game.stopAllAudio();

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
    }

    @Override
    public void fillBackgroundColor() {
        Gdx.gl.glClearColor(137 / 255f, 60 / 255f, 54 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (failsFragment != null) {
            failsFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }

        if (audioService != null) {
            audioService.update();
        }
    }

    public void stopAll() {
        for (GalleryFail fail : failsD) {
            audioService.stop(fail.storyAudio);
            fail.isPlaying = false;
        }

        for (GalleryFail fail : failsM) {
            audioService.stop(fail.storyAudio);
            fail.isPlaying = false;
        }

        audioService.stop();

        if (!isDisposed) {

            game.stopAllAudio();

            for (AudioRow audioRow : failsFragment.audioRows) {
                audioRow.init();
            }
        }
    }

    public void destroyBanners() {
        if (goofsFragment != null) {
            goofsFragment.destroy();
            goofsFragment = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        destroyBanners();

        stopAll();

        failsM.clear();
        failsD.clear();

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (failsFragment != null) {
            failsFragment.destroy();
            failsFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }
    }
}
