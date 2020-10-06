package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.utils.Log;

public class GameModeSideButton extends Image {

    private final String tag = getClass().getSimpleName();
    final MenuScreen screen;
    final SpriteDrawable onlineSprite, offlineSprite;

    public GameModeSideButton(final MenuScreen screen) {
        super();

        this.screen = screen;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();
                screen.game.sfxService.onAnyBtnClicked();

                try {

                    screen.openGameModeBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        Texture txt1 = screen.assetManager.get("ui/btn_online.png", Texture.class);
        Texture txt2 = screen.assetManager.get("ui/btn_offline.png", Texture.class);

        onlineSprite = new SpriteDrawable(new Sprite(txt1));
        offlineSprite = new SpriteDrawable(new Sprite(txt2));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (screen.game.gameState.preferences.isOnlineMode) {
            setDrawable(onlineSprite);
        } else {
            setDrawable(offlineSprite);
        }
    }
}
