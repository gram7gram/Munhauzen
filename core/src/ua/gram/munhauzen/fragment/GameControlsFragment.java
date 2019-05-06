package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MainMenuScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameControlsFragment implements Disposable {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final AssetManager assetManager;
    public Image soundButton, menuButton;

    public GameControlsFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new AssetManager();
    }

    public void create() {

        assetManager.load("GameScreen/b_booksound_off.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_on.png", Texture.class);
        assetManager.load("GameScreen/b_bookmenu.png", Texture.class);

        assetManager.finishLoading();

        soundButton = new FitImage(getSoundButtonBackground());

        menuButton = new FitImage(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_bookmenu.png", Texture.class))));

        menuButton.setHeight(MunhauzenGame.WORLD_HEIGHT / 6f);

        soundButton.setHeight(MunhauzenGame.WORLD_HEIGHT / 6f);

        soundButton.setPosition(
                MunhauzenGame.WORLD_WIDTH - soundButton.getWidth() - menuButton.getWidth() - 10,
                MunhauzenGame.WORLD_HEIGHT - soundButton.getHeight()
        );

        menuButton.setPosition(
                MunhauzenGame.WORLD_WIDTH - menuButton.getWidth() - 5,
                MunhauzenGame.WORLD_HEIGHT - menuButton.getHeight()
        );

        soundButton.addAction(Actions.moveBy(0, soundButton.getHeight() * 3 / 5));
        menuButton.addAction(Actions.moveBy(0, menuButton.getHeight() * 3 / 5));

        gameScreen.ui.addActor(soundButton);
        gameScreen.ui.addActor(menuButton);

        addListenersToSoundButton();
        addListenersToMenuButton();
    }

    private Drawable getSoundButtonBackground() {
        Texture texture = GameState.isMute
                ? assetManager.get("GameScreen/b_booksound_off.png", Texture.class)
                : assetManager.get("GameScreen/b_booksound_on.png", Texture.class);

        return new SpriteDrawable(new Sprite(texture));
    }

    private void addListenersToMenuButton() {

        final ClickListener slideDown = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                menuButton.clearListeners();
                menuButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);

                        menuButton.clearActions();

                        gameScreen.game.setScreen(new MainMenuScreen(gameScreen.game));
                        gameScreen.dispose();
                    }
                });

                menuButton.setTouchable(Touchable.disabled);

                menuButton.addAction(
                        Actions.sequence(
                                Actions.moveBy(0, -menuButton.getHeight() * 3 / 5, .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        menuButton.setTouchable(Touchable.enabled);
                                    }
                                }),
                                Actions.delay(5),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i(tag, "Menu ignored, hiding...");

                                        menuButton.setTouchable(Touchable.disabled);

                                        menuButton.addAction(
                                                Actions.sequence(
                                                        Actions.delay(.2f),
                                                        Actions.moveBy(0, menuButton.getHeight() * 3 / 5, .3f),
                                                        Actions.run(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                menuButton.setTouchable(Touchable.enabled);

                                                                addListenersToMenuButton();
                                                            }
                                                        })
                                                )
                                        );
                                    }
                                })
                        )
                );
            }
        };

        menuButton.setTouchable(Touchable.enabled);
        menuButton.clearListeners();
        menuButton.addListener(slideDown);
    }

    private void addListenersToSoundButton() {

        final ClickListener slideDown = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                soundButton.clearListeners();
                soundButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);

                        soundButton.clearActions();

                        GameState.isMute = !GameState.isMute;

                        Log.i(tag, "Sound is " + (GameState.isMute ? "ON" : "OFF"));

                        soundButton.setDrawable(getSoundButtonBackground());

                        soundButton.setTouchable(Touchable.disabled);

                        soundButton.addAction(
                                Actions.sequence(
                                        Actions.delay(.2f),
                                        Actions.moveBy(0, soundButton.getHeight() * 3 / 5, .3f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                soundButton.setTouchable(Touchable.enabled);

                                                addListenersToSoundButton();
                                            }
                                        })
                                )
                        );
                    }
                });

                soundButton.setTouchable(Touchable.disabled);

                soundButton.addAction(
                        Actions.sequence(
                                Actions.moveBy(0, -soundButton.getHeight() * 3 / 5, .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        soundButton.setTouchable(Touchable.enabled);
                                    }
                                }),
                                Actions.delay(5),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i(tag, "Sound ignored, hiding...");

                                        soundButton.setTouchable(Touchable.disabled);

                                        soundButton.addAction(
                                                Actions.sequence(
                                                        Actions.delay(.2f),
                                                        Actions.moveBy(0, soundButton.getHeight() * 3 / 5, .3f),
                                                        Actions.run(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                soundButton.setTouchable(Touchable.enabled);

                                                                addListenersToSoundButton();
                                                            }
                                                        })
                                                )
                                        );
                                    }
                                })
                        )
                );
            }
        };

        soundButton.setTouchable(Touchable.enabled);
        soundButton.clearListeners();
        soundButton.addListener(slideDown);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
