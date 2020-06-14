package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.screen.saves.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.saves.fragment.OptionsFragment;
import ua.gram.munhauzen.screen.saves.fragment.SavesFragment;
import ua.gram.munhauzen.screen.saves.ui.SavesLayers;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SavesScreen extends AbstractScreen {

    public AudioService audioService;
    public SavesLayers layers;
    public OptionsFragment optionsFragment;
    public SavesFragment savesFragment;
    public HashMap<String, Save> saves;
    public ControlsFragment controlsFragment;

    public SavesScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = game.internalAssetManager.get("p1.jpg", Texture.class);

        saves = new HashMap<>();
        audioService = new AudioService(game);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("saves/sv_baron.png", Texture.class);
        assetManager.load("saves/icon_question.png", Texture.class);

        assetManager.load("ui/banner_fond_3.png", Texture.class);

        updateSaves();
    }

    public void updateSaves() {
        saves.clear();

        for (String saveId : new String[]{"1", "2", "3", "4"}) {

            try {
                Save save = game.databaseManager.loadSave(saveId);

                saves.put(saveId, save);

                if (save.chapter != null) {

                    Chapter chapter = ChapterRepository.find(game.gameState, save.chapter);

                    assetManager.load(chapter.icon, Texture.class);
                }
            } catch (Throwable e) {
                Log.e(tag, e);
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
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            layers = new SavesLayers();

            ui.addActor(layers);

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            controlsFragment.fadeIn();

            savesFragment = new SavesFragment(this);
            savesFragment.create();

            layers.setContentLayer(savesFragment);

            savesFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void recreateSaves() {

        savesFragment.destroy();

        updateSaves();

        assetManager.finishLoading();

        savesFragment = new SavesFragment(this);
        savesFragment.create();

        layers.setContentLayer(savesFragment);

        savesFragment.root.setVisible(true);
    }

    @Override
    public void fillBackgroundColor() {
        Gdx.gl.glClearColor(137 / 255f, 60 / 255f, 54 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (optionsFragment != null) {
            optionsFragment.update();
        }
        if (savesFragment != null) {
            savesFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        saves.clear();

        if (optionsFragment != null) {
            optionsFragment.destroy();
            optionsFragment = null;
        }

        if (savesFragment != null) {
            savesFragment.destroy();
            savesFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }

        if (layers != null) {
            layers.dispose();
            layers = null;
        }
    }
}
