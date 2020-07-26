package ua.gram.munhauzen.screen.saves.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.saves.ui.LoadOptionBanner;
import ua.gram.munhauzen.screen.saves.ui.OptionsBanner;
import ua.gram.munhauzen.screen.saves.ui.SaveOptionBanner;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class OptionsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final SavesScreen screen;
    public final Save save;
    public FragmentRoot root;
    OptionsBanner banner;
    SaveOptionBanner saveOptionBanner;
    LoadOptionBanner loadOptionBanner;

    public OptionsFragment(SavesScreen screen, Save save) {
        this.screen = screen;
        this.save = save;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        Log.i(tag, "create");

        banner = new OptionsBanner(this);
        banner.create();
        saveOptionBanner = new SaveOptionBanner(this);
        saveOptionBanner.create();
        loadOptionBanner = new LoadOptionBanner(this);
        loadOptionBanner.create();

        Container<?> c = new Container<>();
        c.setTouchable(Touchable.enabled);

        root = new FragmentRoot();
        root.addContainer(c);
        root.addContainer(banner);
        root.addContainer(saveOptionBanner);
        root.addContainer(loadOptionBanner);

        restoreOptions();

        c.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                Log.i(tag, "root clicked");

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.optionsFragment = null;
                    }
                });
            }
        });
    }

    public void restoreOptions() {
        banner.setVisible(true);
        saveOptionBanner.setVisible(false);
        loadOptionBanner.setVisible(false);
    }

    public void showSaveOption() {
        banner.setVisible(false);
        saveOptionBanner.setVisible(true);
        loadOptionBanner.setVisible(false);
    }

    public void showLoadOption() {
        banner.setVisible(false);
        saveOptionBanner.setVisible(false);
        loadOptionBanner.setVisible(true);
    }

    public void onStartClicked() {
        try {
            GameState state = screen.game.gameState;

            state.setActiveSave(save);

            screen.navigateTo(new GameScreen(screen.game));

        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }
    }

    public void onSaveClicked() {
        try {
            Json json = new Json(JsonWriter.OutputType.json);

            Save copy = json.fromJson(Save.class, json.toJson(screen.game.gameState.activeSave));
            copy.id = save.id;
            copy.updatedAt = DateUtils.now();

            screen.game.databaseManager.persistSave(copy);

            fadeOut(new Runnable() {
                @Override
                public void run() {
                    destroy();

                    screen.recreateSaves();

                    screen.optionsFragment = null;
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }
    }

    public void fadeIn() {
        root.clearActions();

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(0, 20),
                Actions.parallel(
                        Actions.alpha(1, .3f),
                        Actions.moveBy(0, -20, .3f)
                )
        ));
    }

    public void fadeOut(Runnable task) {

        root.clearActions();

        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .3f),
                        Actions.moveBy(0, 20, .3f)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }

    public void update() {

    }
}