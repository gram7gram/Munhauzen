package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.utils.Log;

public class ReferalSideButton extends WauAnimation {

    private final String tag = getClass().getSimpleName();

    public ReferalSideButton(final MenuScreen screen, float width) {
        super(screen.assetManager.get("menu/btn_wau_sheet_1x4.png", Texture.class), width);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.sfxService.onAnyBtnClicked();

                try {

                    screen.openReferralBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
