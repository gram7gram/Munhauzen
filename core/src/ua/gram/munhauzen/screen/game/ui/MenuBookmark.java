package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.utils.Log;

public class MenuBookmark extends Table {

    final String tag = getClass().getSimpleName();
    final GameScreen screen;
    public final FixedImage img, tail;
    final float width;

    public MenuBookmark(GameScreen screen) {
        this.screen = screen;

        width = MunhauzenGame.WORLD_WIDTH * .15f;

        img = new FixedImage(screen.assetManager.get("GameScreen/b_bookmenu.png", Texture.class), width);
        tail = new FixedImage(screen.assetManager.get("GameScreen/b_bookmenu_tail.png", Texture.class), width);

        add(img).size(img.width, img.height).row();
        add(tail).size(tail.width, tail.height).row();

        pack();

        addListeners();

        addAction(bookmarkHidden(0));
    }

    private Action bookmarkVisible() {

        img.setTouchable(Touchable.enabled);
        tail.setTouchable(Touchable.enabled);

        return Actions.moveTo(
                MunhauzenGame.WORLD_WIDTH - getWidth() - 10,
                MunhauzenGame.WORLD_HEIGHT - (getHeight()),
                .3f);
    }

    private Action bookmarkHidden(float duration) {

        img.setTouchable(Touchable.disabled);
        tail.setTouchable(Touchable.enabled);

        return Actions.moveTo(
                MunhauzenGame.WORLD_WIDTH - getWidth() - 10,
                MunhauzenGame.WORLD_HEIGHT - (getHeight()) / 2f,
                duration);
    }

    private void onMenuClicked() {

        screen.game.stopCurrentSfx();

        screen.game.sfxService.onBackToMenuClicked();

        screen.navigateTo(new MenuScreen(screen.game));
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

                                        Log.i(tag, "Ignored, slide up...");

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

                onMenuClicked();

                img.setTouchable(Touchable.disabled);
                tail.setTouchable(Touchable.disabled);

            }
        });
    }
}
