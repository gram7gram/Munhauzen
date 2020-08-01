package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.AchievementPopup;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class AchievementFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final GameScreen screen;
    public FragmentRoot root;
    public boolean isFadeIn, isFadeOut;
    AchievementPopup banner;

    public AchievementFragment(GameScreen screen) {
        this.screen = screen;
    }

    public void create(Inventory inventory) {

        screen.assetManager.load("gallery/gv2_statue.png", Texture.class);
        screen.assetManager.load("GameScreen/an_stars_sheet.png", Texture.class);
        screen.assetManager.load(inventory.statueImage, Texture.class);

        screen.assetManager.finishLoading();

        Container<?> backdrop = new Container<>();
        backdrop.setTouchable(Touchable.enabled);

        banner = new AchievementPopup(screen);
        banner.create(inventory);

        root = new FragmentRoot();
        root.addContainer(backdrop);
        root.addContainer(banner);

        backdrop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                try {

                    Log.i(tag, "backdrop clicked");

                    fadeOut();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        root.setName(tag);

        root.setVisible(false);
    }

    public void fadeIn() {

        if (!isMounted()) return;

        Log.i(tag, "fadeIn");

        root.setVisible(true);

        banner.animate();

        root.addAction(Actions.sequence(
                Actions.delay(2),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        fadeOut();
                    }
                })
        ));

        screen.stopCurrentSfx();
        screen.game.sfxService.onAchievementUnlocked();
    }

    public void fadeOut() {

        if (isFadeOut) return;

        isFadeOut = true;
        isFadeIn = false;

        root.setTouchable(Touchable.disabled);
        root.setVisible(true);

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.alpha(0, .2f),
                        Actions.visible(false),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;

                                screen.removeAchievementFragment();
                            }
                        })
                )
        );
    }

    @Override
    public Actor getRoot() {
        return root;
    }

}
