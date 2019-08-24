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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SaveDialog extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    public final SavesScreen screen;
    public final Save save;
    public FragmentRoot root;
    PrimaryButton startBtn, saveBtn;
    StoryAudio yesAudio, noAudio;

    public SaveDialog(SavesScreen screen, Save save) {
        this.game = screen.game;
        this.screen = screen;
        this.save = save;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        Log.i(tag, "create");

        saveBtn = game.buttonBuilder.primary("Save", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "save clicked");

                try {
                    root.setTouchable(Touchable.disabled);

                    stopAllAudio();

                    playYes();

                    if (yesAudio == null) {
                        onSaveClicked();
                    } else {
                        Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                onSaveClicked();
                            }
                        }, yesAudio.duration / 1000f);
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        startBtn = game.buttonBuilder.danger("Start", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "start clicked");

                try {
                    root.setTouchable(Touchable.disabled);

                    stopAllAudio();

                    playNo();

                    if (noAudio == null) {
                        onStartClicked();
                    } else {
                        Timer.instance().scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                onStartClicked();
                            }
                        }, noAudio.duration / 1000f);
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        Label title = new Label("What do you need?", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setAlignment(Align.center);

        Table content = new Table();
        content.pad(50, 100, 50, 100);

        content.add(title).center().colspan(2).expandX().padBottom(30)
                .width(MunhauzenGame.WORLD_WIDTH * .6f)
                .row();

        content.add(saveBtn).left()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);

        content.add(startBtn).right()
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f);

        content.setBackground(new SpriteDrawable(new Sprite(
                screen.assetManager.get("ui/banner_fond_3.png", Texture.class)
        )));

        Container<Table> container = new Container<>(content);
        container.pad(MunhauzenGame.WORLD_WIDTH * .05f);
        container.setTouchable(Touchable.enabled);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        px.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, .3f);
        px.fill();

        container.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                Log.i(tag, "root clicked");

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.saveDialog = null;
                    }
                });
            }
        });

        root = new FragmentRoot();
        root.addContainer(container);

        root.setVisible(false);
    }

    private void onStartClicked() {
        try {
            History history = game.gameState.history;

            history.activeSaveId = save.id;
            history.activeSave = save;

            game.setScreen(new GameScreen(game));
            screen.dispose();

        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }
    }

    private void onSaveClicked() {
        try {
            History history = game.gameState.history;

            game.databaseManager.persistSave(history.activeSave, ExternalFiles.getSaveFile(save.id));

            screen.updateSaves();

            screen.savesFragment.root.invalidate();

            fadeOut(new Runnable() {
                @Override
                public void run() {
                    destroy();

                    screen.saveDialog = null;
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
            yesAudio.name = "sfx_save_yes";

            screen.audioService.prepareAndPlay(yesAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

//            screen.onCriticalError(e);
        }
    }

    private void playNo() {
        try {

            noAudio = new StoryAudio();
            noAudio.name = "sfx_save_no";

            screen.audioService.prepareAndPlay(noAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

//            screen.onCriticalError(e);
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

        History history = game.gameState.history;

        startBtn.setDisabled(save.chapter == null);
        saveBtn.setDisabled(history.activeSave.chapter == null);
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