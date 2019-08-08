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
import ua.gram.munhauzen.utils.MathUtils;

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

        saves = new ArrayList<>();
        audioService = new AudioService(game);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("saves/gv_paper_1.png", Texture.class);
        assetManager.load("saves/gv_paper_2.png", Texture.class);
        assetManager.load("saves/gv_paper_3.png", Texture.class);
        assetManager.load("saves/sv_baron.png", Texture.class);

        assetManager.load("ui/banner_fond_3.png", Texture.class);

        updateSaves();
    }

    public void updateSaves() {
        saves.clear();

        for (String saveId : game.gameState.history.saves) {

            Save save = game.databaseManager.loadSave(saveId);

            save.chapter = MathUtils.random(new String[]{
                    "a10", "a11", "a1", null
            });

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

        audioService.dispose();

        layers.dispose();

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
