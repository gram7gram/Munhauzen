package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.screen.saves.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.saves.fragment.SaveDialog;
import ua.gram.munhauzen.screen.saves.fragment.SavesFragment;
import ua.gram.munhauzen.screen.saves.ui.SavesLayers;
import ua.gram.munhauzen.service.AudioService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SavesScreen extends AbstractScreen {

    public AudioService audioService;
    public SavesLayers layers;
    public SaveDialog saveDialog;
    public SavesFragment savesFragment;
    public ArrayList<Save> saves;
    public ControlsFragment controlsFragment;

    public SavesScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);

        saves = new ArrayList<>();
        audioService = new AudioService(game);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("saves/sv_baron.png", Texture.class);

        assetManager.load("ui/banner_fond_3.png", Texture.class);

        updateSaves();
    }

    public void updateSaves() {
        saves.clear();

        for (String saveId : game.gameState.history.saves) {

            Save save = game.databaseManager.loadSave(saveId);

            saves.add(save);

            if (save.chapter != null) {

                Chapter chapter = ChapterRepository.find(game.gameState, save.chapter);

                if (chapter.icon != null) {
                    assetManager.load(chapter.icon, Texture.class);
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        navigateTo(new MenuScreen(game));
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

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

        Gdx.input.setInputProcessor(ui);

    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (saveDialog != null) {
            saveDialog.update();
        }
        if (savesFragment != null) {
            savesFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        saves.clear();

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (saveDialog != null) {
            saveDialog.destroy();
            saveDialog = null;
        }

        if (savesFragment != null) {
            savesFragment.destroy();
            savesFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }
    }
}
