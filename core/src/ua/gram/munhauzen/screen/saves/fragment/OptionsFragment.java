package ua.gram.munhauzen.screen.saves.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.saves.ui.OptionsBanner;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class OptionsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final SavesScreen screen;
    public final Save save;
    public FragmentRoot root;
    StoryAudio yesAudio, noAudio;

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

        OptionsBanner banner = new OptionsBanner(this);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        px.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, .3f);
        px.fill();

        Container c = new Container();
        c.setTouchable(Touchable.enabled);
        c.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        root = new FragmentRoot();
        root.addContainer(c);
        root.addContainer(banner);

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

    public void onRemoveClicked() {
        try {
            ExternalFiles.getSaveFile(save.id).delete();

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

    private void playYes() {
        try {

            yesAudio = new StoryAudio();
            yesAudio.audio = "sfx_save_yes";

            screen.audioService.prepareAndPlay(yesAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }
    }

    private void playNo() {
        try {

            noAudio = new StoryAudio();
            noAudio.audio = "sfx_save_no";

            screen.audioService.prepareAndPlay(noAudio);

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
        if (yesAudio != null) {
            screen.audioService.updateVolume(yesAudio);
        }
        if (noAudio != null) {
            screen.audioService.updateVolume(noAudio);
        }
    }

    public void stopAllAudio() {
        if (noAudio != null) {
            screen.audioService.stop(noAudio);
            noAudio = null;
        }
        if (yesAudio != null) {
            screen.audioService.stop(yesAudio);
            yesAudio = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        stopAllAudio();

    }
}