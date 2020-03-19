package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.utils.Log;

public class SoundBookmark extends Table {

    final String tag = getClass().getSimpleName();
    final GameScreen screen;
    public final FixedImage img, tail;
    final float width;

    SpriteDrawable soundOn, soundOff, tailOn, tailOff;

    public SoundBookmark(GameScreen screen) {
        this.screen = screen;

        width = MunhauzenGame.WORLD_WIDTH * .15f;

        soundOn = new SpriteDrawable(new Sprite(screen.assetManager.get("GameScreen/b_booksound_on.png", Texture.class)));
        soundOff = new SpriteDrawable(new Sprite(screen.assetManager.get("GameScreen/b_booksound_off.png", Texture.class)));
        tailOn = new SpriteDrawable(new Sprite(screen.assetManager.get("GameScreen/b_booksound_on_tail.png", Texture.class)));
        tailOff = new SpriteDrawable(new Sprite(screen.assetManager.get("GameScreen/b_booksound_off_tail.png", Texture.class)));

        img = new FixedImage();
        tail = new FixedImage();

        updateBackgound();

        add(img).size(img.width, img.height).row();
        add(tail).size(tail.width, tail.height).row();

        pack();

        addListeners();

        addAction(bookmarkHidden(0));
    }

    private Action bookmarkVisible() {

        img.setTouchable(Touchable.enabled);
        tail.setTouchable(Touchable.enabled);

        MenuBookmark bookmark = screen.controlsFragment.menuGroup;

        return Actions.moveTo(
                MunhauzenGame.WORLD_WIDTH - getWidth() - 10 - bookmark.getWidth() - 10,
                MunhauzenGame.WORLD_HEIGHT - (getHeight()),
                .3f);
    }

    private Action bookmarkHidden(float duration) {

        img.setTouchable(Touchable.disabled);
        tail.setTouchable(Touchable.enabled);

        MenuBookmark bookmark = screen.controlsFragment.menuGroup;

        return Actions.moveTo(
                MunhauzenGame.WORLD_WIDTH - getWidth() - 10 - bookmark.getWidth() - 10,
                MunhauzenGame.WORLD_HEIGHT - (getHeight()) / 2f,
                duration);
    }

    private void updateBackgound() {
        img.setBackground(GameState.isMute ? soundOff : soundOn, width);
        tail.setBackground(GameState.isMute ? tailOff : tailOn, width);
    }

    private void onSoundClicked() {

        GameState.isMute = !GameState.isMute;

        Log.i(tag, "Sound is " + (!GameState.isMute ? "ON" : "OFF"));

        screen.game.stopCurrentSfx();

        if (GameState.isMute) {
            screen.game.sfxService.onSoundDisabled();
        } else {
            screen.game.sfxService.onSoundEnabled();
        }
    }

    private void addListeners() {

        final ActorGestureListener slideUp = new ActorGestureListener() {

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                super.fling(event, velocityX, velocityY, button);

                if (velocityY > 100) {
                    start();
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                start();
            }

            private void start() {

                Log.i(tag, "Slide up sound");

                screen.game.sfxService.onBookmarkUp();

                clearActions();
                addAction(
                        Actions.sequence(
                                bookmarkHidden(.3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        addListeners();
                                    }
                                })
                        )
                );
            }
        };

        tail.setTouchable(Touchable.enabled);
        tail.clearListeners();
        tail.addListener(new ActorGestureListener() {

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                super.fling(event, velocityX, velocityY, button);

                if (velocityY < -100) {
                    start();
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                start();
            }

            private void start() {

                Log.i(tag, "Slide down sound");

                screen.game.sfxService.onBookmarkDown();

                img.setTouchable(Touchable.disabled);
                tail.setTouchable(Touchable.disabled);

                tail.clearListeners();
                tail.addListener(slideUp);

                addAction(
                        Actions.sequence(
                                bookmarkVisible(),
                                Actions.delay(5),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i(tag, "Sound ignored, slide up...");

                                        addAction(
                                                Actions.sequence(
                                                        bookmarkHidden(.3f),
                                                        Actions.run(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                addListeners();
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

        img.setTouchable(Touchable.enabled);
        img.clearListeners();
        img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                onSoundClicked();

                updateBackgound();

                img.setTouchable(Touchable.disabled);
                tail.setTouchable(Touchable.disabled);

                clearActions();
                addAction(
                        Actions.sequence(
                                Actions.delay(.5f),
                                bookmarkHidden(.3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        addListeners();
                                    }
                                })
                        )
                );
            }
        });
    }
}
