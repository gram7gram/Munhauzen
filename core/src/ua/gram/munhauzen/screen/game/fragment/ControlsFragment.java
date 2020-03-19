package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.MenuBookmark;
import ua.gram.munhauzen.screen.game.ui.SoundBookmark;
import ua.gram.munhauzen.ui.Fragment;

public class ControlsFragment extends Fragment {

    private final GameScreen gameScreen;
    public SoundBookmark soundGroup;
    public MenuBookmark menuGroup;

    public ControlsFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void create() {

        gameScreen.assetManager.load("GameScreen/b_booksound_off.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_booksound_on.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_booksound_on_tail.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_booksound_off_tail.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_bookmenu.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_bookmenu_tail.png", Texture.class);

        gameScreen.assetManager.finishLoading();

        menuGroup = new MenuBookmark(gameScreen);
        soundGroup = new SoundBookmark(gameScreen);

        gameScreen.ui.addActor(soundGroup);
        gameScreen.ui.addActor(menuGroup);
    }

    public void fadeOut() {

        menuGroup.setTouchable(Touchable.disabled);
        soundGroup.setTouchable(Touchable.disabled);
        soundGroup.setVisible(true);
        menuGroup.setVisible(true);

        soundGroup.clearActions();
        soundGroup.addAction(
                Actions.sequence(
                        Actions.alpha(0, .2f),
                        Actions.visible(false)
                )
        );

        menuGroup.clearActions();
        menuGroup.addAction(
                Actions.sequence(
                        Actions.alpha(0, .2f),
                        Actions.visible(false)
                )
        );
    }

    @Override
    public void destroy() {
        super.destroy();

        if (soundGroup != null)
            soundGroup.remove();

        if (soundGroup != null)
            menuGroup.remove();

    }

    @Override
    public Actor getRoot() {
        return null;
    }


}
