package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.MunhauzenScreen;

public class GameModeFragment extends BannerFragment<MunhauzenScreen> {

    public GameModeFragment(MunhauzenScreen screen) {
        super(screen);
    }

    @Override
    public void create() {
        throw new GdxRuntimeException("Not supported");
    }

    public void create(Runnable action, Runnable soundAction) {

        screen.game.internalAssetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.game.internalAssetManager.load("ui/banner_version.png", Texture.class);
        screen.game.internalAssetManager.finishLoading();

        Banner<?> banner = new GameModeBanner(this, action);
        banner.create();

        createRoot();

        root.addContainer(banner);

        screen.game.stopCurrentSfx();
        soundAction.run();
    }

    @Override
    public void onBackDropClicked() {
        //ignore
    }
}
