package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.MunhauzenScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class AdultGateFragment extends Fragment {

    public final MunhauzenScreen screen;
    FragmentRoot root;
    public boolean isFadeIn;
    public boolean isFadeOut;

    final Integer[] questions = {1, 2, 3, 4};
    public final int question;
    public AdultGateBanner banner;
    public AdultIncorrectBanner incorrectBanner;

    public AdultGateFragment(MunhauzenScreen screen) {
        this.screen = screen;

        question = MathUtils.random(questions);
    }

    public void create(Runnable task) {

        loadQuestionTexture();

        banner = new AdultGateBanner(this, task);
        banner.create();

        incorrectBanner = new AdultIncorrectBanner(this);
        incorrectBanner.create();

        Container<?> c = new Container<>();
        c.setTouchable(Touchable.enabled);

        root = new FragmentRoot();
        root.addContainer(c);
        root.addContainer(banner);
        root.addContainer(incorrectBanner);

        c.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        screen.destroyBanners();
                    }
                });

            }
        });

        showQuestion();

        root.setVisible(false);
    }

    public void showQuestion() {
        banner.setVisible(true);
        incorrectBanner.setVisible(false);
    }

    public void showError() {
        banner.setVisible(false);
        incorrectBanner.setVisible(true);
    }

    public void loadQuestionTexture() {
        screen.game.internalAssetManager.load("purchase/adult.png", Texture.class);
        screen.game.internalAssetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.game.internalAssetManager.load("purchase/PG_1.png", Texture.class);
        screen.game.internalAssetManager.load("purchase/PG_2.png", Texture.class);
        screen.game.internalAssetManager.load("purchase/PG_3.png", Texture.class);
        screen.game.internalAssetManager.load("purchase/PG_4.png", Texture.class);

        switch (question) {
            case 1:
                screen.game.internalAssetManager.load("purchase/PG_1.png", Texture.class);
                break;
            case 2:
                screen.game.internalAssetManager.load("purchase/PG_2.png", Texture.class);
                break;
            case 3:
                screen.game.internalAssetManager.load("purchase/PG_3.png", Texture.class);
                break;
            case 4:
                screen.game.internalAssetManager.load("purchase/PG_4.png", Texture.class);
        }

        screen.game.internalAssetManager.finishLoading();
    }

    public Texture getQuestionTexture() {

        switch (question) {
            case 1:
                return screen.game.internalAssetManager.get("purchase/PG_1.png", Texture.class);
            case 2:
                return screen.game.internalAssetManager.get("purchase/PG_2.png", Texture.class);
            case 3:
                return screen.game.internalAssetManager.get("purchase/PG_3.png", Texture.class);
            case 4:
                return screen.game.internalAssetManager.get("purchase/PG_4.png", Texture.class);

        }

        return null;
    }

    public void update() {

    }

    public void fadeIn() {

        if (!isMounted()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));

    }

    public boolean canFadeOut() {
        return isMounted() && root.isVisible() && !isFadeOut;
    }

    public void fadeOut(Runnable task) {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                }),
                Actions.run(task)
        ));
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    @Override
    public Actor getRoot() {
        return root;
    }

}
