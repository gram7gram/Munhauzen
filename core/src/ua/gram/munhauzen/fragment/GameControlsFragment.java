package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MainMenuScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final AssetManager assetManager;
    public Image soundButton, soundTailButton, menuButton, menuTailButton;
    public Group soundGroup, menuGroup, root;

    public GameControlsFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new AssetManager();
    }

    public void create() {

        assetManager.load("GameScreen/b_booksound_off.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_on.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_tail.png", Texture.class);
        assetManager.load("GameScreen/b_bookmenu.png", Texture.class);
        assetManager.load("GameScreen/b_bookmenu_tail.png", Texture.class);

        assetManager.finishLoading();

        soundButton = new FitImage(getSoundButtonBackground());

        soundTailButton = new FitImage(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_booksound_tail.png", Texture.class))));

        menuButton = new FitImage(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_bookmenu.png", Texture.class))));

        menuTailButton = new FitImage(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_bookmenu_tail.png", Texture.class))));

        float width = MunhauzenGame.WORLD_WIDTH * .15f;
        float menuScale = 1f * width / menuButton.getWidth();
        float soundScale = 1f * width / soundButton.getWidth();
        float menuTailScale = 1f * width / menuTailButton.getWidth();
        float soundTailScale = 1f * width / soundTailButton.getWidth();

        menuButton.setWidth(width);
        menuButton.setHeight(menuButton.getHeight() * menuScale);

        menuTailButton.setWidth(width);
        menuTailButton.setHeight(menuTailButton.getHeight() * menuTailScale);

        soundButton.setWidth(width);
        soundButton.setHeight(soundButton.getHeight() * soundScale);

        soundTailButton.setWidth(width);
        soundTailButton.setHeight(soundTailButton.getHeight() * soundTailScale);

        soundButton.setPosition(
                MunhauzenGame.WORLD_WIDTH - soundButton.getWidth() - menuButton.getWidth() - 10,
                MunhauzenGame.WORLD_HEIGHT - soundButton.getHeight()
        );
        soundTailButton.setPosition(
                soundButton.getX(),
                soundButton.getY() - soundTailButton.getHeight()
        );

        menuButton.setPosition(
                MunhauzenGame.WORLD_WIDTH - menuButton.getWidth() - 5,
                MunhauzenGame.WORLD_HEIGHT - menuButton.getHeight()
        );
        menuTailButton.setPosition(
                menuButton.getX(),
                menuButton.getY() - menuTailButton.getHeight()
        );

        soundGroup = new Group();
        soundGroup.addActor(soundButton);
        soundGroup.addActor(soundTailButton);

        menuGroup = new Group();
        menuGroup.addActor(menuButton);
        menuGroup.addActor(menuTailButton);

        soundGroup.addAction(Actions.moveBy(0, soundButton.getHeight()));
        menuGroup.addAction(Actions.moveBy(0, menuButton.getHeight()));

        root = new Group();
        root.addActor(soundGroup);
        root.addActor(menuGroup);

        root.setName(tag);

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

        final ClickListener slideUp = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "Slide up menu");

                menuGroup.clearActions();
                menuGroup.addAction(
                        Actions.sequence(
                                Actions.moveBy(0, menuButton.getHeight(), .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        addListenersToMenuButton();
                                    }
                                })
                        )
                );
            }
        };

        menuTailButton.setTouchable(Touchable.enabled);
        menuTailButton.clearListeners();
        menuTailButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "Slide down menu");

                menuButton.setTouchable(Touchable.disabled);
                menuTailButton.setTouchable(Touchable.disabled);

                menuTailButton.clearListeners();
                menuTailButton.addListener(slideUp);

                menuGroup.addAction(
                        Actions.sequence(
                                Actions.moveBy(0, -menuButton.getHeight(), .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        menuButton.setTouchable(Touchable.enabled);
                                        menuTailButton.setTouchable(Touchable.enabled);
                                    }
                                }),
                                Actions.delay(5),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i(tag, "Menu ignored, slide up...");

                                        menuButton.setTouchable(Touchable.disabled);
                                        menuTailButton.setTouchable(Touchable.disabled);

                                        menuGroup.addAction(
                                                Actions.sequence(
                                                        Actions.moveBy(0, menuButton.getHeight(), .3f),
                                                        Actions.run(new Runnable() {
                                                            @Override
                                                            public void run() {
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
        });

        menuButton.setTouchable(Touchable.enabled);
        menuButton.clearListeners();
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "Goto menu");

                menuGroup.clearActions();

                gameScreen.game.setScreen(new MainMenuScreen(gameScreen.game));
                gameScreen.dispose();
            }
        });
    }

    private void addListenersToSoundButton() {

        final ClickListener slideUp = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "Slide up sound");

                soundGroup.clearActions();
                soundGroup.addAction(
                        Actions.sequence(
                                Actions.moveBy(0, soundButton.getHeight(), .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        addListenersToSoundButton();
                                    }
                                })
                        )
                );
            }
        };

        soundTailButton.setTouchable(Touchable.enabled);
        soundTailButton.clearListeners();
        soundTailButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "Slide down sound");

                soundButton.setTouchable(Touchable.disabled);
                soundTailButton.setTouchable(Touchable.disabled);

                soundTailButton.clearListeners();
                soundTailButton.addListener(slideUp);

                soundGroup.addAction(
                        Actions.sequence(
                                Actions.moveBy(0, -soundButton.getHeight(), .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        soundButton.setTouchable(Touchable.enabled);
                                        soundTailButton.setTouchable(Touchable.enabled);
                                    }
                                }),
                                Actions.delay(5),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i(tag, "Sound ignored, slide up...");

                                        soundButton.setTouchable(Touchable.disabled);
                                        soundTailButton.setTouchable(Touchable.disabled);

                                        soundGroup.addAction(
                                                Actions.sequence(
                                                        Actions.moveBy(0, soundButton.getHeight(), .3f),
                                                        Actions.run(new Runnable() {
                                                            @Override
                                                            public void run() {
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
        });

        soundButton.setTouchable(Touchable.enabled);
        soundButton.clearListeners();
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                soundGroup.clearActions();

                GameState.isMute = !GameState.isMute;

                Log.i(tag, "Sound is " + (!GameState.isMute ? "ON" : "OFF"));

                soundButton.setDrawable(getSoundButtonBackground());

                soundButton.setTouchable(Touchable.disabled);
                soundTailButton.setTouchable(Touchable.disabled);

                soundGroup.clearActions();
                soundGroup.addAction(
                        Actions.sequence(
                                Actions.delay(.5f),
                                Actions.moveBy(0, soundButton.getHeight(), .3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        addListenersToSoundButton();
                                    }
                                })
                        )
                );
            }
        });
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        menuGroup.remove();
        soundGroup.remove();
    }
}
