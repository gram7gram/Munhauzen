package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.utils.Log;

public class ReferalSideButton extends Image {

    private final String tag = getClass().getSimpleName();

    public ReferalSideButton(final MenuScreen screen) {
        super(screen.assetManager.get("menu/btn_referral.png", Texture.class));

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.sfxService.onAnyBtnClicked();

                try {


//                    MunhauzenGame.referralService.setReferralCount(MunhauzenGame.referralInterface.getRefferralCount());

                    screen.openReferralBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
