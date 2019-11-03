package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.CrownAnimation;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;
import ua.gram.munhauzen.utils.Log;

public class StartButton extends MenuButton {

    public StartButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.start_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Timer.Task task = new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            screen.game.gameState.setActiveSave(new Save());

                            screen.game.databaseManager.persist(screen.game.gameState);

                            screen.stopCurrentSfx();
                            screen.game.currentSfx = screen.game.sfxService.onMenuStartClicked();

                            screen.scaleAndNavigateTo(new GameScreen(screen.game));
                        } catch (Throwable e) {
                            Log.e(tag, e);

                            screen.onCriticalError(e);
                        }
                    }
                };

                if (!screen.game.gameState.menuState.isContinueEnabled) {
                    Timer.instance().postTask(task);
                } else {

                    Gdx.input.setInputProcessor(screen.ui);
                    screen.isUILocked = false;

                    screen.openStartWarningBanner(task);
                }
            }
        });
    }

    @Override
    IconAnimation createAnimationIcon() {
        return new CrownAnimation(screen, this);
    }
}
