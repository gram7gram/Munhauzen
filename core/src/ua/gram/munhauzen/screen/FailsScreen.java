package ua.gram.munhauzen.screen;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.screen.fails.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.fails.fragment.FailsFragment;
import ua.gram.munhauzen.screen.fails.ui.FailsLayers;
import ua.gram.munhauzen.service.AudioFailService;

public class FailsScreen extends AbstractScreen {

    public FailsLayers layers;
    public FailsFragment failsFragment;
    public ControlsFragment controlsFragment;
    public ArrayList<GalleryFail> failsM, failsD;
    public AudioFailService audioService;

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

        createFails();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        controlsFragment.fadeIn();

        failsFragment = new FailsFragment(this);
        failsFragment.create();

        layers.setContentLayer(failsFragment);

        failsFragment.fadeIn();
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

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
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
    }

    @Override
    public void dispose() {
        super.dispose();

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
